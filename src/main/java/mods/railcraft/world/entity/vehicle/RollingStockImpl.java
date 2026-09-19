package mods.railcraft.world.entity.vehicle;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.joml.Vector2d;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import mods.railcraft.api.carts.Linkable;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.carts.Side;
import mods.railcraft.api.carts.Train;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.api.event.CartLinkEvent;
import mods.railcraft.attachment.RailcraftAttachmentTypes;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import mods.railcraft.world.level.block.track.ElevatorTrackBlock;
import mods.railcraft.world.level.block.track.behaivor.HighSpeedTrackUtil;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.NameAndId;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

public class RollingStockImpl implements RollingStock, ValueIOSerializable {

  private static final double LINK_DRAG = 0.95;
  private static final float MAX_DISTANCE = 8F;
  private static final float STIFFNESS = 0.7F;
  private static final float HS_STIFFNESS = 0.7F;
  private static final float DAMPING = 0.4F;
  private static final float HS_DAMPING = 0.3F;
  private static final float FORCE_LIMITER = 6F;
  private static final int DIMENSION_TIMEOUT_TICKS = 10 * SharedConstants.TICKS_PER_SECOND;

  private static final Logger LOGGER = LogUtils.getLogger();

  private final AbstractMinecart minecart;

  @Nullable
  private TrainImpl train;

  @Nullable
  private RollingStock frontLink;
  @Nullable
  private RollingStock backLink;
  @Nullable
  private UUID unresolvedBackLink;
  @Nullable
  private UUID unresolvedFrontLink;

  private boolean backAutoLinkEnabled;
  private boolean frontAutoLinkEnabled;
  private LaunchState launchState = LaunchState.LANDED;
  private int elevatorRemainingTicks;
  private int preventMountRemainingTicks;
  private int derailedRemainingTicks;
  private boolean explosionPending;
  private boolean highSpeed;

  private int primaryLinkTimeoutTicks;
  private int secondaryLinkTimeoutTicks;

  public RollingStockImpl(IAttachmentHolder holder) {
    if (!(holder instanceof AbstractMinecart minecart)) {
      throw new IllegalArgumentException("holder must be instance of AbstractMinecart.");
    }
    this.minecart = minecart;
  }

  @Override
  public boolean hasLink(Side side) {
    return switch (side) {
      case FRONT -> this.frontLink != null;
      case BACK -> this.backLink != null;
    };
  }

  @Override
  public Optional<RollingStock> linkAt(Side side) {
    this.resolveLinks();
    return Optional.ofNullable(switch (side) {
      case FRONT -> this.frontLink;
      case BACK -> this.backLink;
    });
  }

  private void setLink(Side side, @Nullable RollingStock minecart) {
    switch (side) {
      case FRONT -> this.frontLink = minecart;
      case BACK -> this.backLink = minecart;
    }
  }

  private void resolveLinks() {
    if (this.unresolvedBackLink != null) {
      this.resolveLink(this.unresolvedBackLink)
          .ifPresent(cart -> {
            this.backLink = cart;
            this.unresolvedBackLink = null;
          });
    }

    if (this.unresolvedFrontLink != null) {
      this.resolveLink(this.unresolvedFrontLink)
          .ifPresent(cart -> {
            this.frontLink = cart;
            this.unresolvedFrontLink = null;
          });
    }
  }

  private Optional<RollingStock> resolveLink(UUID minecartId) {
    var level = (ServerLevel) this.minecart.level();
    var entity = level.getEntity(minecartId);
    return entity instanceof AbstractMinecart minecart
        ? Optional.ofNullable(minecart.getCapability(CAPABILITY))
            .filter(cart -> {
              var result = cart.isLinkedWith(this);
              if (!result) {
                LOGGER.warn("Link mismatch between {} and {} (link was missing on {})",
                    this.minecart, cart.entity(), cart.entity());
              }
              return result;
            })
        : Optional.empty();
  }

  @Override
  public Optional<Side> sideOf(RollingStock rollingStock) {
    Objects.requireNonNull(rollingStock, "rollingStock cannot be null.");

    if (this.unresolvedBackLink != null
        && rollingStock.entity().getUUID().equals(this.unresolvedBackLink)) {
      this.unresolvedBackLink = null;
      this.backLink = rollingStock;
      return Optional.of(Side.BACK);
    }

    if (this.unresolvedFrontLink != null
        && rollingStock.entity().getUUID().equals(this.unresolvedFrontLink)) {
      this.unresolvedFrontLink = null;
      this.frontLink = rollingStock;
      return Optional.of(Side.FRONT);
    }

    if (this.backLink == rollingStock) {
      return Optional.of(Side.BACK);
    }

    if (this.frontLink == rollingStock) {
      return Optional.of(Side.FRONT);
    }

    return Optional.empty();
  }

  @Override
  public boolean link(RollingStock rollingStock) {
    var maxDistance = this.getLinkageDistanceSq(rollingStock);
    if (this == rollingStock
        || this.isSameTrainAs(rollingStock)
        || !this.isEnd()
        || !rollingStock.isEnd()
        || this.entity().distanceToSqr(rollingStock.entity()) > maxDistance) {
      return false;
    }

    if (!this.isLinkableWith(rollingStock) || !rollingStock.isLinkableWith(this)) {
      return false;
    }

    var train = this.train();
    Objects.requireNonNull(rollingStock.train()).copyTo(Objects.requireNonNull(train));

    Side linkSide = null;
    for (var side : Side.values()) {
      if (this.disabledSide().filter(side::equals).isPresent()
          || rollingStock.disabledSide().map(Side::opposite).filter(side::equals).isPresent()) {
        continue;
      }

      if (!this.hasLink(side) && !rollingStock.hasLink(side.opposite())) {
        linkSide = side;
        break;
      }

      if (!this.hasLink(side.opposite())
          && !rollingStock.hasLink(side.opposite())
          && this.swapLinks(side)) {
        linkSide = side;
        break;
      }

      if (!this.hasLink(side)
          && !rollingStock.hasLink(side)
          && rollingStock.swapLinks(side.opposite())) {
        linkSide = side;
        break;
      }
    }
    if (linkSide == null) {
      return false;
    }

    this.completeLink(rollingStock, linkSide);
    rollingStock.completeLink(this, linkSide.opposite());

    train.copyTo(Objects.requireNonNull(this.train()));

    NeoForge.EVENT_BUS.post(new CartLinkEvent.Link(this, rollingStock));
    return true;
  }

  @Override
  public void completeLink(RollingStock rollingStock, Side side) {
    this.setLink(side, rollingStock);
    this.setAutoLinkEnabled(side, false);
    if (this.minecart instanceof Linkable handler) {
      handler.linked(rollingStock);
    }
  }

  @Override
  public void removeLink(Side side) {
    this.linkAt(side).ifPresent(linked -> {
      this.setLink(side, null);
      if (this.minecart instanceof Linkable handler) {
        handler.unlinked(linked);
      }
    });
  }

  @Override
  public boolean unlink(Side side) {
    var linkedCart = this.linkAt(side).orElse(null);
    if (linkedCart == null) {
      return false;
    }

    linkedCart.sideOf(this).ifPresent(linkedCart::removeLink);
    this.removeLink(side);

    NeoForge.EVENT_BUS.post(new CartLinkEvent.Unlink(this, linkedCart));
    return true;
  }

  @Override
  public boolean swapLinks(Side side) {
    var next = switch (side) {
      case FRONT -> this.frontLink;
      case BACK -> this.backLink;
    };

    if (next != null && !next.swapLinks(side)) {
      return false;
    }

    // Can't swap links if one side is disabled.
    if (this.disabledSide().isPresent()) {
      return false;
    }

    var oldFront = this.frontLink;
    this.frontLink = this.backLink;
    this.backLink = oldFront;
    return true;
  }

  @Override
  public boolean isAutoLinkEnabled(Side side) {
    return switch (side) {
      case BACK -> this.backAutoLinkEnabled;
      case FRONT -> this.frontAutoLinkEnabled;
    };
  }

  @Override
  public boolean setAutoLinkEnabled(Side side, boolean enabled) {
    if (enabled && this.disabledSide().filter(side::equals).isPresent()) {
      return false;
    }
    switch (side) {
      case BACK -> this.backAutoLinkEnabled = enabled;
      case FRONT -> this.frontAutoLinkEnabled = enabled;
    }
    return true;
  }

  @Override
  public boolean isLaunched() {
    return this.launchState == LaunchState.LAUNCHED;
  }

  @Override
  public void launch() {
    this.launchState = LaunchState.LAUNCHING;
    this.minecart.setData(RailcraftAttachmentTypes.CAN_USE_RAIL, false);
  }

  @Override
  public int getElevatorRemainingTicks() {
    return this.elevatorRemainingTicks;
  }

  @Override
  public void setElevatorRemainingTicks(int elevatorRemainingTicks) {
    this.elevatorRemainingTicks = elevatorRemainingTicks;
  }

  @Override
  public boolean isMountable() {
    return this.preventMountRemainingTicks <= 0;
  }

  @Override
  public void setPreventMountRemainingTicks(int preventMountRemainingTicks) {
    this.preventMountRemainingTicks = preventMountRemainingTicks;
  }

  @Override
  public boolean isDerailed() {
    return this.derailedRemainingTicks > 0;
  }

  @Override
  public void setDerailedRemainingTicks(int derailedRemainingTicks) {
    this.derailedRemainingTicks = derailedRemainingTicks;
  }

  @Override
  public void primeExplosion() {
    this.explosionPending = true;
  }

  @Override
  public boolean isHighSpeed() {
    return this.highSpeed;
  }

  @Override
  public void checkHighSpeed(BlockPos blockPos) {
    var currentMotion = this.minecart.getDeltaMovement();
    if (this.highSpeed) {
      HighSpeedTrackUtil.checkSafetyAndExplode(this.level(), blockPos, this.minecart);
      return;
    }

    if (!HighSpeedTrackUtil.isTrackSafeForHighSpeed(this.level(), blockPos, this.minecart)) {
      this.limitSpeed();
      return;
    }

    if (Math.abs(currentMotion.x()) > HIGH_SPEED_THRESHOLD) {
      double motionX = Math.copySign(HIGH_SPEED_THRESHOLD, currentMotion.x());
      this.minecart.setDeltaMovement(motionX, currentMotion.y(), currentMotion.z());
      this.highSpeed = true;
    }

    if (Math.abs(currentMotion.z()) > HIGH_SPEED_THRESHOLD) {
      double motionZ = Math.copySign(HIGH_SPEED_THRESHOLD, currentMotion.z());
      this.minecart.setDeltaMovement(currentMotion.x(), currentMotion.y(), motionZ);
      this.highSpeed = true;
    }
  }

  private void limitSpeed() {
    var motion = this.minecart.getDeltaMovement();
    var motionX = Math.copySign(Math.min(HIGH_SPEED_THRESHOLD, Math.abs(motion.x())), motion.x());
    var motionZ = Math.copySign(Math.min(HIGH_SPEED_THRESHOLD, Math.abs(motion.z())), motion.z());
    this.minecart.setDeltaMovement(motionX, motion.y(), motionZ);
  }

  @Override
  public AbstractMinecart entity() {
    return this.minecart;
  }

  public boolean isFront() {
    this.resolveLinks();
    return this.frontLink == null;
  }

  private boolean validateTrainOwnership() {
    var front = this.isFront();
    if (!front && this.train != null) {
      this.train = null;
    }
    if (front && this.train == null) {
      this.train = TrainImpl.create(this);
    }
    return front;
  }

  @Nullable
  @Override
  public Train train() {
    return this.validateTrainOwnership() ? this.train : this.frontLink.train();
  }

  @Override
  public void removed(Entity.RemovalReason reason) {
    if (reason.shouldDestroy()) {
      this.unlinkAll();
    }
  }

  @Override
  public void tick() {
    if (this.level().isClientSide()) {
      return;
    }

    this.adjustCart();

    if (this.preventMountRemainingTicks > 0) {
      this.preventMountRemainingTicks--;
    }

    if (this.elevatorRemainingTicks < ElevatorTrackBlock.ELEVATOR_TIMER) {
      this.minecart.setNoGravity(false);
    }

    if (this.elevatorRemainingTicks > 0) {
      this.elevatorRemainingTicks--;
    }

    if (this.derailedRemainingTicks > 0) {
      this.derailedRemainingTicks--;
    }

    if (this.explosionPending) {
      this.explosionPending = false;
      MinecartUtil.explodeCart(this.entity());
    }

    if (this.highSpeed) {
      if (MinecartUtil.cartVelocityIsLessThan(this.entity(), EXPLOSION_SPEED_THRESHOLD)) {
        this.highSpeed = false;
      } else if (this.launchState == LaunchState.LANDED) {
        HighSpeedTrackUtil.checkSafetyAndExplode(this.level(),
            this.minecart.blockPosition(), this.entity());
      }
    }

    if (BaseRailBlock.isRail(this.level(), this.minecart.blockPosition())) {
      this.minecart.fallDistance = 0;
      if (this.minecart.isVehicle()) {
        this.minecart.getPassengers().forEach(p -> p.fallDistance = 0);
      }
      if (this.launchState == LaunchState.LAUNCHED) {
        this.land();
      }
    } else if (this.launchState == LaunchState.LAUNCHING) {
      this.launchState = LaunchState.LAUNCHED;
      this.minecart.setData(RailcraftAttachmentTypes.CAN_USE_RAIL, true);
    } else if (this.launchState == LaunchState.LAUNCHED && this.minecart.onGround()) {
      this.land();
    }

    Vec3 motion = this.minecart.getDeltaMovement();

    double motionX = Math.copySign(Math.min(Math.abs(motion.x()), 9.5), motion.x());
    double motionY = Math.copySign(Math.min(Math.abs(motion.y()), 9.5), motion.y());
    double motionZ = Math.copySign(Math.min(Math.abs(motion.z()), 9.5), motion.z());

    this.minecart.setDeltaMovement(motionX, motionY, motionZ);
  }

  /**
   * Inspects the links and determines if any physics adjustments need to be made.
   */
  private void adjustCart() {
    if (this.isLaunched() || this.isOnElevator()) {
      return;
    }

    var linkedA = this.maintainLink(Side.BACK);
    var linkedB = this.maintainLink(Side.FRONT);
    var linked = linkedA || linkedB;

    // Drag
    if (linked && !this.isHighSpeed()) {
      this.minecart.setDeltaMovement(
          this.minecart.getDeltaMovement().multiply(LINK_DRAG, 1.0D, LINK_DRAG));
    }

    // Speed & End Drag
    if (this.validateTrainOwnership()) {
      this.train.refreshMaxSpeed();
      // if (linked && !(cart instanceof EntityLocomotive)) {
      // double drag = 0.97;
      // cart.motionX *= drag;
      // cart.motionZ *= drag;
      // }
    }
  }

  /**
   * This is where the physics magic actually gets performed. It uses Spring Forces and Damping
   * Forces to maintain a fixed distance between carts.
   *
   * @return {@code true} if linked, {@code false} otherwise
   */
  public boolean maintainLink(Side linkSide) {
    var linkedStock = this.linkAt(linkSide).orElse(null);
    if (linkedStock == null) {
      return false;
    }

    if (linkedStock.isLaunched() || linkedStock.isOnElevator()) {
      return false;
    }

    var linkedEntity = linkedStock.entity();

    var sameDimension = this.level().dimension().equals(linkedEntity.level().dimension());

    var unlink = false;
    switch (linkSide) {
      case BACK -> {
        if (sameDimension) {
          this.primaryLinkTimeoutTicks = 0;
        } else if (++this.primaryLinkTimeoutTicks > DIMENSION_TIMEOUT_TICKS) {
          unlink = true;
        } else {
          return true;
        }
      }
      case FRONT -> {
        if (sameDimension) {
          this.secondaryLinkTimeoutTicks = 0;
        } else if (++this.secondaryLinkTimeoutTicks > DIMENSION_TIMEOUT_TICKS) {
          unlink = true;
        } else {
          return true;
        }
      }
    };

    if (unlink) {
      LOGGER.debug("Linked rolling stock in separate dimension, unlinking: {}", linkedEntity);
      this.unlink(linkSide);
      return false;
    }

    double dist = this.minecart.distanceTo(linkedEntity);
    if (dist > MAX_DISTANCE) {
      LOGGER.debug("Max distance exceeded, unlinking: {}", linkedEntity);
      this.unlink(linkSide);
      return false;
    }

    var adj1 = this.canCartBeAdjustedBy(linkedStock);
    var adj2 = linkedStock.canCartBeAdjustedBy(this);

    var cart1Pos = new Vector2d(this.minecart.getX(), this.minecart.getZ());
    var cart2Pos = new Vector2d(linkedEntity.getX(), linkedEntity.getZ());

    var sub = cart2Pos.sub(cart1Pos);
    var unit = sub.equals(0, 0) ? sub : sub.normalize(); // Check for NaN

    // Spring force

    float optDist = this.getOptimalDistance(linkedStock);
    double stretch = dist - optDist;
    // stretch = Math.max(0.0, stretch);
    // if(Math.abs(stretch) > 0.5) {
    // stretch *= 2;
    // }

    var highSpeed = this.isHighSpeed();

    var stiffness = highSpeed ? HS_STIFFNESS : STIFFNESS;
    var springX = stiffness * stretch * unit.x();
    var springZ = stiffness * stretch * unit.y();

    springX = limitForce(springX);
    springZ = limitForce(springZ);

    if (adj1) {
      this.minecart.setDeltaMovement(this.minecart.getDeltaMovement().add(springX, 0.0D, springZ));
    }

    if (adj2) {
      linkedEntity
          .setDeltaMovement(linkedEntity.getDeltaMovement().subtract(springX, 0.0D, springZ));
    }

    // Damping
    var cart1Vel = new Vector2d(
        this.minecart.getDeltaMovement().x(),
        this.minecart.getDeltaMovement().z());
    var cart2Vel = new Vector2d(
        linkedEntity.getDeltaMovement().x(),
        linkedEntity.getDeltaMovement().z());

    var dot = cart2Vel.sub(cart1Vel).dot(unit);

    var damping = highSpeed ? HS_DAMPING : DAMPING;
    var dampX = damping * dot * unit.x();
    var dampZ = damping * dot * unit.y();

    dampX = limitForce(dampX);
    dampZ = limitForce(dampZ);

    if (adj1) {
      this.minecart.setDeltaMovement(this.minecart.getDeltaMovement().add(dampX, 0.0D, dampZ));
    }

    if (adj2) {
      linkedEntity.setDeltaMovement(linkedEntity.getDeltaMovement().subtract(dampX, 0.0D, dampZ));
    }

    return true;
  }

  private void land() {
    this.launchState = LaunchState.LANDED;
    this.minecart.setData(RailcraftAttachmentTypes.MAX_SPEED_AIR_LATERAL,
        Optional.of(RailcraftConstants.DEFAULT_MAX_SPEED_AIR_LATERAL));
    this.minecart.setData(RailcraftAttachmentTypes.MAX_SPEED_AIR_VERTICAL, RailcraftConstants.DEFAULT_MAX_SPEED_AIR_VERTICAL);
    this.minecart.setData(RailcraftAttachmentTypes.AIR_DRAG, RailcraftConstants.DEFAULT_AIR_DRAG);
  }

  private float getOptimalDistance(RollingStock rollingStock) {
    float dist = 0;
    if (this.minecart instanceof Linkable handler)
      dist += handler.getOptimalDistance(rollingStock);
    else
      dist += OPTIMAL_LINK_DISTANCE;
    if (rollingStock.entity() instanceof Linkable handler)
      dist += handler.getOptimalDistance(this);
    else
      dist += OPTIMAL_LINK_DISTANCE;
    return dist;
  }

  private static double limitForce(double force) {
    return Math.copySign(Math.min(Math.abs(force), FORCE_LIMITER), force);
  }

  private float getLinkageDistanceSq(RollingStock rollingStock) {
    float dist = 0;
    if (this.minecart instanceof Linkable handler) {
      dist += handler.getLinkageDistance(rollingStock);
    } else {
      dist += MAX_LINK_DISTANCE;
    }
    if (rollingStock.entity() instanceof Linkable handler) {
      dist += handler.getLinkageDistance(this);
    } else {
      dist += MAX_LINK_DISTANCE;
    }
    return dist * dist;
  }

  @Override
  public Optional<NameAndId> owner() {
    return this.entity() instanceof Locomotive loco ? loco.getOwner() : Optional.empty();
  }

  @Override
  public void serialize(ValueOutput valueOutput) {
    if (this.train != null) {
      //TODO: TEST
      this.train.serialize(valueOutput.child(CompoundTagKeys.TRAIN));
    }

    if (this.unresolvedBackLink != null) {
      valueOutput.store(CompoundTagKeys.BACK_LINK, UUIDUtil.CODEC, this.unresolvedBackLink);
    } else if (this.backLink != null) {
      valueOutput.store(CompoundTagKeys.BACK_LINK, UUIDUtil.CODEC, this.backLink.entity().getUUID());
    }

    if (this.unresolvedFrontLink != null) {
      valueOutput.store(CompoundTagKeys.FRONT_LINK, UUIDUtil.CODEC, this.unresolvedFrontLink);
    } else if (this.frontLink != null) {
      valueOutput.store(CompoundTagKeys.FRONT_LINK, UUIDUtil.CODEC, this.frontLink.entity().getUUID());
    }

    valueOutput.putBoolean(CompoundTagKeys.BACK_AUTO_LINK_ENABLED, this.backAutoLinkEnabled);
    valueOutput.putBoolean(CompoundTagKeys.FRONT_AUTO_LINK_ENABLED, this.frontAutoLinkEnabled);

    valueOutput.putString(CompoundTagKeys.LAUNCH_STATE, this.launchState.getName());
    valueOutput.putInt(CompoundTagKeys.ELEVATOR_REMAINING_TICKS, this.elevatorRemainingTicks);
    valueOutput.putInt(CompoundTagKeys.PREVENT_MOUNT_REMAINING_TICKS, this.preventMountRemainingTicks);
    valueOutput.putInt(CompoundTagKeys.DERAILED_REMAINING_TICKS, this.derailedRemainingTicks);
    valueOutput.putBoolean(CompoundTagKeys.EXPLOSION_PENDING, this.explosionPending);
    valueOutput.putBoolean(CompoundTagKeys.HIGH_SPEED, this.highSpeed);
  }

  @Override
  public void deserialize(ValueInput valueInput) {
    this.train = null;
    valueInput.child(CompoundTagKeys.TRAIN).ifPresent(train -> {
      this.train = TrainImpl.deserialize(train, this);
    });

    this.unresolvedBackLink = valueInput.read(CompoundTagKeys.BACK_LINK, UUIDUtil.CODEC).orElse(null);
    this.unresolvedFrontLink = valueInput.read(CompoundTagKeys.FRONT_LINK, UUIDUtil.CODEC).orElse(null);

    this.backAutoLinkEnabled = valueInput.getBooleanOr(CompoundTagKeys.BACK_AUTO_LINK_ENABLED, false);
    this.frontAutoLinkEnabled = valueInput.getBooleanOr(CompoundTagKeys.FRONT_AUTO_LINK_ENABLED, false);

    this.launchState = valueInput.read(CompoundTagKeys.LAUNCH_STATE, LaunchState.CODEC).orElse(LaunchState.LANDED);
    this.elevatorRemainingTicks = valueInput.getIntOr(CompoundTagKeys.ELEVATOR_REMAINING_TICKS, 0);
    this.preventMountRemainingTicks = valueInput.getIntOr(CompoundTagKeys.PREVENT_MOUNT_REMAINING_TICKS, 0);
    this.derailedRemainingTicks = valueInput.getIntOr(CompoundTagKeys.DERAILED_REMAINING_TICKS, 0);
    this.explosionPending = valueInput.getBooleanOr(CompoundTagKeys.EXPLOSION_PENDING, false);
    this.highSpeed = valueInput.getBooleanOr(CompoundTagKeys.HIGH_SPEED, false);
  }
}

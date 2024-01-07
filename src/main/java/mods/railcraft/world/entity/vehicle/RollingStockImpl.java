package mods.railcraft.world.entity.vehicle;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2d;
import org.slf4j.Logger;
import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import mods.railcraft.Railcraft;
import mods.railcraft.api.carts.Linkable;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.carts.Side;
import mods.railcraft.api.carts.Train;
import mods.railcraft.api.event.CartLinkEvent;
import mods.railcraft.util.attachment.RailcraftAttachmentTypes;
import mods.railcraft.util.attachment.RollingStockDataAttachment;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import mods.railcraft.world.level.block.track.ElevatorTrackBlock;
import mods.railcraft.world.level.block.track.behaivor.HighSpeedTrackUtil;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;

public class RollingStockImpl implements RollingStock {

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

  private int primaryLinkTimeoutTicks, secondaryLinkTimeoutTicks;

  public RollingStockImpl(AbstractMinecart minecart) {
    this.minecart = minecart;
  }

  private RollingStockDataAttachment getDataAttachment() {
    return this.minecart.getData(RailcraftAttachmentTypes.ROLLING_STOCK_DATA.get());
  }

  @Override
  public boolean hasLink(Side side) {
    return this.getDataAttachment().hasLink(side);
  }

  @Override
  public Optional<RollingStock> linkAt(Side side) {
    return this.getDataAttachment().linkAt(this::resolveLink, side);
  }

  private Optional<RollingStock> resolveLink(UUID minecartId) {
    var level = (ServerLevel) this.level();
    var entity = level.getEntity(minecartId);
    if (entity == null)
      return Optional.empty();
    return Optional.ofNullable(entity.getCapability(CAPABILITY))
        .filter(cart -> {
          var result = cart.isLinkedWith(this);
          if (!result) {
            LOGGER.warn("Link mismatch between {} and {} (link was missing on {})",
                this.minecart, cart.entity(), cart.entity());
          }
          return result;
        });
  }

  @Override
  public Optional<Side> sideOf(RollingStock rollingStock) {
    Objects.requireNonNull(rollingStock, "rollingStock cannot be null.");
    return this.getDataAttachment().sideOf(rollingStock);
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
    rollingStock.train().copyTo(train);

    Side linkSide = null;
    for (var side : Side.values()) {
      if (this.disabledSide().filter(side::equals).isPresent()
          || rollingStock.disabledSide().map(Side::opposite).filter(side::equals).isPresent()) {
        continue;
      }

      if (this.hasLink(side) && rollingStock.hasLink(side)) {
        // Reverse our train if both carts are facing each other
        if (!this.swapLinks(side) && !rollingStock.swapLinks(side)) {
          return false;
        }
      }

      linkSide = side;
    }
    if (linkSide == null) {
      return false;
    }

    this.completeLink(rollingStock, linkSide);
    rollingStock.completeLink(this, linkSide.opposite());

    train.copyTo(this.train());

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

  @Nullable
  private RollingStock getLink(Side side) {
    return this.getDataAttachment().getLink(side);
  }

  private void setLink(Side side, @Nullable RollingStock rollingStock) {
    this.getDataAttachment().setLink(side, rollingStock);
  }

  @Override
  public boolean swapLinks(Side side) {
    var next = this.getLink(side);
    if (next != null && !next.swapLinks(side)) {
      return false;
    }

    // Can't swap links if one side is disabled.
    if (this.disabledSide().isPresent()) {
      return false;
    }

    var oldFront = this.getLink(Side.FRONT);
    this.setLink(Side.FRONT, this.getLink(Side.BACK));
    this.setLink(Side.BACK, oldFront);
    return true;
  }

  @Override
  public boolean isAutoLinkEnabled(Side side) {
    return this.getDataAttachment().isAutoLinkEnabled(side);
  }

  @Override
  public boolean setAutoLinkEnabled(Side side, boolean enabled) {
    if (enabled && this.disabledSide().filter(side::equals).isPresent()) {
      return false;
    }
    return this.getDataAttachment().setAutoLinkEnabled(side, enabled);
  }

  @Override
  public boolean isLaunched() {
    return this.getDataAttachment().checkLaunchState(LaunchState.LAUNCHED);
  }

  @Override
  public void launch() {
    this.getDataAttachment().setLaunchState(LaunchState.LAUNCHING);
    this.minecart.setCanUseRail(false);
  }

  @Override
  public int getElevatorRemainingTicks() {
    return this.getDataAttachment().getElevatorRemainingTicks();
  }

  @Override
  public void setElevatorRemainingTicks(int elevatorRemainingTicks) {
    this.getDataAttachment().setElevatorRemainingTicks(elevatorRemainingTicks);
  }

  @Override
  public boolean isMountable() {
    return this.getDataAttachment().isMountable();
  }

  @Override
  public void setPreventMountRemainingTicks(int preventMountRemainingTicks) {
    this.getDataAttachment().setPreventMountRemainingTicks(preventMountRemainingTicks);
  }

  @Override
  public boolean isDerailed() {
    return this.getDataAttachment().isDerailed();
  }

  @Override
  public void setDerailedRemainingTicks(int derailedRemainingTicks) {
    this.getDataAttachment().setDerailedRemainingTicks(derailedRemainingTicks);
  }

  @Override
  public void primeExplosion() {
    this.getDataAttachment().setExplosionPending(true);
  }

  @Override
  public boolean isHighSpeed() {
    return this.getDataAttachment().isHighSpeed();
  }

  @Override
  public void checkHighSpeed(BlockPos blockPos) {
    var currentMotion = this.minecart.getDeltaMovement();
    if (this.isHighSpeed()) {
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
      this.setHighSpeed(true);
    }

    if (Math.abs(currentMotion.z()) > HIGH_SPEED_THRESHOLD) {
      double motionZ = Math.copySign(HIGH_SPEED_THRESHOLD, currentMotion.z());
      this.minecart.setDeltaMovement(currentMotion.x(), currentMotion.y(), motionZ);
      this.setHighSpeed(true);
    }
  }

  private void limitSpeed() {
    var motion = this.minecart.getDeltaMovement();
    var motionX = Math.copySign(Math.min(HIGH_SPEED_THRESHOLD, Math.abs(motion.x())), motion.x());
    var motionZ = Math.copySign(Math.min(HIGH_SPEED_THRESHOLD, Math.abs(motion.z())), motion.z());
    this.minecart.setDeltaMovement(motionX, motion.y(), motionZ);
  }

  private void setHighSpeed(boolean highSpeed) {
    this.getDataAttachment().setHighSpeed(highSpeed);
  }

  @Override
  public AbstractMinecart entity() {
    return this.minecart;
  }

  @Override
  public boolean isFront() {
    return this.getDataAttachment().isFront(this::resolveLink);
  }

  private boolean validateTrainOwnership() {
    return this.getDataAttachment()
        .validateTrainOwnership(this::resolveLink, this.minecart.getUUID());
  }

  @Override
  @Nullable
  public Train train() {
    return this.getDataAttachment().train(this::resolveLink, this.minecart.getUUID());
  }

  @Override
  public void removed(Entity.RemovalReason reason) {
    if (reason.shouldDestroy()) {
      this.forceChunk(false);
      this.unlinkAll();
    } else {
      this.forceChunk(this.train().size(this.level()) > 1);
    }
  }

  private void forceChunk(boolean add) {
    if (this.level() instanceof ServerLevel level) {
      var chunk = this.minecart.chunkPosition();
      Railcraft.CHUNK_CONTROLLER
          .forceChunk(level, this.minecart.getUUID(), chunk.x, chunk.z, add, true);
    }
  }

  @Override
  public void tick() {
    if (this.level().isClientSide()) {
      return;
    }

    this.adjustCart();

    if (!this.isMountable()) {
      this.getDataAttachment().decrementPreventMountRemainingTicks();
    }

    if (this.getElevatorRemainingTicks() < ElevatorTrackBlock.ELEVATOR_TIMER) {
      this.minecart.setNoGravity(false);
    }

    this.getDataAttachment().decrementElevatorRemainingTicks();

    this.getDataAttachment().decrementDerailedRemainingTicks();

    if (this.getDataAttachment().getExplosionPending()) {
      this.getDataAttachment().setExplosionPending(false);
      MinecartUtil.explodeCart(this.entity());
    }

    if (this.isHighSpeed()) {
      if (MinecartUtil.cartVelocityIsLessThan(this.entity(), EXPLOSION_SPEED_THRESHOLD)) {
        this.setHighSpeed(false);
      } else if (this.getDataAttachment().checkLaunchState(LaunchState.LANDED)) {
        HighSpeedTrackUtil
            .checkSafetyAndExplode(this.level(), this.minecart.blockPosition(), this.entity());
      }
    }

    // Fix flip
    var distance = Mth.degreesDifference(this.minecart.getYRot(), this.minecart.yRotO);
    var cutoff = 120F;
    if (distance < -cutoff || distance >= cutoff) {
      this.minecart.setYRot(this.minecart.getYRot() + 180.0F);
      this.minecart.flipped = !this.minecart.flipped;
      this.minecart.setYRot(this.minecart.getYRot() % 360.0F);
    }

    if (BaseRailBlock.isRail(this.level(), this.minecart.blockPosition())) {
      this.minecart.fallDistance = 0;
      if (this.minecart.isVehicle()) {
        this.minecart.getPassengers().forEach(p -> p.fallDistance = 0);
      }
      if (this.isLaunched()) {
        this.land();
      }
    } else if (this.getDataAttachment().checkLaunchState(LaunchState.LAUNCHING)) {
      this.getDataAttachment().setLaunchState(LaunchState.LAUNCHED);
      this.minecart.setCanUseRail(true);
    } else if (this.isLaunched() && this.minecart.onGround()) {
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
      this.getDataAttachment().trainRefreshMaxSpeed(this.level());
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
    this.getDataAttachment().setLaunchState(LaunchState.LANDED);
    this.minecart.setMaxSpeedAirLateral(AbstractMinecart.DEFAULT_MAX_SPEED_AIR_LATERAL);
    this.minecart.setMaxSpeedAirVertical(AbstractMinecart.DEFAULT_MAX_SPEED_AIR_VERTICAL);
    this.minecart.setDragAir(AbstractMinecart.DEFAULT_AIR_DRAG);
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
  public Optional<GameProfile> owner() {
    return this.entity() instanceof Locomotive loco ? loco.getOwner() : Optional.empty();
  }
}

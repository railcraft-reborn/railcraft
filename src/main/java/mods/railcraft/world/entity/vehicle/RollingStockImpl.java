package mods.railcraft.world.entity.vehicle;

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
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.api.event.CartLinkEvent;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import mods.railcraft.world.level.block.track.ElevatorTrackBlock;
import mods.railcraft.world.level.block.track.behaivor.HighSpeedTrackUtil;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.world.ForgeChunkManager;

public class RollingStockImpl implements RollingStock, INBTSerializable<CompoundTag> {

  public static final ResourceLocation KEY = Railcraft.rl("rolling_stock");

  private static final double LINK_DRAG = 0.95;

  private static final float MAX_DISTANCE = 8F;
  private static final float STIFFNESS = 0.7F;
  private static final float HS_STIFFNESS = 0.7F;
  private static final float DAMPING = 0.4F;
  private static final float HS_DAMPING = 0.3F;
  private static final float FORCE_LIMITER = 6F;
  private static final int DIMENSION_TIMEOUT_TICKS = 10 * SharedConstants.TICKS_PER_SECOND;

  private static final Logger logger = LogUtils.getLogger();

  private final AbstractMinecart minecart;

  @Nullable
  private RollingStock frontLink;
  @Nullable
  private RollingStock backLink;

  // Server only
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

  @Nullable
  private TrainImpl train;

  public RollingStockImpl(AbstractMinecart minecart) {
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
          .ifPresent(cart -> this.backLink = cart);
      this.unresolvedBackLink = null;
    }

    if (this.unresolvedFrontLink != null) {
      this.resolveLink(this.unresolvedFrontLink)
          .ifPresent(cart -> this.frontLink = cart);
      this.unresolvedFrontLink = null;
    }
  }

  private Optional<RollingStock> resolveLink(UUID minecartId) {
    var level = (ServerLevel) this.minecart.level();
    var entity = level.getEntity(minecartId);
    return entity instanceof AbstractMinecart minecart
        ? minecart.getCapability(CAPABILITY)
            .filter(cart -> {
              var result = cart.isLinkedWith(this);
              if (!result) {
                logger.warn("Link mismatch between {} and {} (link was missing on {1})",
                    this.minecart, cart.entity());
              }
              return result;
            })
        : Optional.empty();
  }

  @Override
  public Optional<Side> sideOf(RollingStock rollingStock) {
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

    MinecraftForge.EVENT_BUS.post(new CartLinkEvent.Link(this, rollingStock));
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

    MinecraftForge.EVENT_BUS.post(new CartLinkEvent.Unlink(this, linkedCart));
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
    this.minecart.setCanUseRail(false);
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

  @Override
  public CompoundTag serializeNBT() {
    var tag = new CompoundTag();

    if (this.train != null) {
      tag.put("train", this.train.toTag());
    }

    if (this.backLink != null) {
      tag.putUUID("backLink", this.backLink.entity().getUUID());
    }

    if (this.frontLink != null) {
      tag.putUUID("frontLink", this.frontLink.entity().getUUID());
    }

    tag.putBoolean("backAutoLinkEnabled", this.backAutoLinkEnabled);
    tag.putBoolean("frontAutoLinkEnabled", this.frontAutoLinkEnabled);

    tag.putString("launchState", this.launchState.getName());
    tag.putInt("elevatorRemainingTicks", this.elevatorRemainingTicks);
    tag.putInt("preventMountRemainingTicks", this.preventMountRemainingTicks);
    tag.putInt("derailedRemainingTicks", this.derailedRemainingTicks);
    tag.putBoolean("explosionPending", this.explosionPending);
    tag.putBoolean("highSpeed", this.highSpeed);

    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag tag) {
    this.train = tag.contains("train", Tag.TAG_COMPOUND)
        ? TrainImpl.fromTag(tag.getCompound("train"), this)
        : null;
    this.unresolvedBackLink = tag.hasUUID("backLink")
        ? tag.getUUID("backLink")
        : null;
    this.unresolvedFrontLink = tag.hasUUID("frontLink")
        ? tag.getUUID("frontLink")
        : null;

    this.backAutoLinkEnabled = tag.getBoolean("primaryAutoLinkEnabled");
    this.frontAutoLinkEnabled = tag.getBoolean("secondaryAutoLinkEnabled");

    this.launchState = LaunchState.getByName(tag.getString("launchState"))
        .orElse(LaunchState.LANDED);
    this.elevatorRemainingTicks = tag.getInt("elevatorRemainingTicks");
    this.preventMountRemainingTicks = tag.getInt("preventMountRemainingTicks");
    this.derailedRemainingTicks = tag.getInt("derailedRemainingTicks");
    this.explosionPending = tag.getBoolean("explosionPending");
    this.highSpeed = tag.getBoolean("highSpeed");
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

  @Override
  public Train train() {
    return this.validateTrainOwnership() ? this.train : this.frontLink.train();
  }

  @Override
  public void removed(Entity.RemovalReason reason) {
    if (reason.shouldDestroy()) {
      this.forceChunk(false);
      this.unlinkAll();
    } else {
      this.forceChunk(this.train().size() > 1);
    }
  }

  private void forceChunk(boolean add) {
    if (this.level() instanceof ServerLevel level) {
      var chunk = this.minecart.chunkPosition();
      ForgeChunkManager.forceChunk(level, RailcraftConstants.ID, this.minecart.getUUID(),
          chunk.x, chunk.z, add, false);
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
      if (this.launchState == LaunchState.LAUNCHED) {
        this.land();
      }
    } else if (this.launchState == LaunchState.LAUNCHING) {
      this.launchState = LaunchState.LAUNCHED;
      this.minecart.setCanUseRail(true);
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

    // Centroid
    // List<BlockPos> points =
    // Train.streamCarts(cart).map(Entity::getPosition).collect(Collectors.toList());
    // Vec2D centroid = new Vec2D(MathTools.centroid(points));
    //
    // Vec2D cartPos = new Vec2D(cart);
    // Vec2D unit = Vec2D.unit(cartPos, centroid);
    //
    // double amount = 0.2;
    // double pushX = amount * unit.getX();
    // double pushZ = amount * unit.getY();
    //
    // pushX = limitForce(pushX);
    // pushZ = limitForce(pushZ);
    //
    // cart.motionX += pushX;
    // cart.motionZ += pushZ;

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
  public boolean maintainLink(Side linkType) {
    var cart2 = this.linkAt(linkType).orElse(null);
    if (cart2 == null) {
      return false;
    }

    if (cart2.isLaunched() || cart2.isOnElevator()) {
      return false;
    }

    var cart2Entity = cart2.entity();

    var sameDimension = this.level().dimension().equals(cart2Entity.level().dimension());

    var unlink = false;
    switch (linkType) {
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
      logger.debug("Linked minecart in separate dimension, unlinking: {}", cart2Entity);
      this.unlink(linkType);
      return false;
    }

    double dist = this.minecart.distanceTo(cart2Entity);
    if (dist > MAX_DISTANCE) {
      logger.debug("Max distance exceeded, unlinking: {}", cart2Entity);
      this.unlink(linkType);
      return false;
    }

    var adj1 = this.canCartBeAdjustedBy(cart2);
    var adj2 = cart2.canCartBeAdjustedBy(this);

    var cart1Pos = new Vector2d(this.minecart.getX(), this.minecart.getZ());
    var cart2Pos = new Vector2d(cart2Entity.getX(), cart2Entity.getZ());

    var sub = cart2Pos.sub(cart1Pos);
    var unit = sub.equals(0, 0) ? sub : sub.normalize(); //Check for NaN

    // Energy transfer

    // double transX = TRANSFER * (cart2.motionX - cart1.motionX);
    // double transZ = TRANSFER * (cart2.motionZ - cart1.motionZ);
    //
    // transX = limitForce(transX);
    // transZ = limitForce(transZ);
    //
    // if(adj1) {
    // cart1.motionX += transX;
    // cart1.motionZ += transZ;
    // }
    //
    // if(adj2) {
    // cart2.motionX -= transX;
    // cart2.motionZ -= transZ;
    // }

    // Spring force

    float optDist = this.getOptimalDistance(cart2);
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
      cart2Entity.setDeltaMovement(cart2Entity.getDeltaMovement().subtract(springX, 0.0D, springZ));
    }

    // Damping
    var cart1Vel = new Vector2d(
        this.minecart.getDeltaMovement().x(),
        this.minecart.getDeltaMovement().z());
    var cart2Vel = new Vector2d(
        cart2Entity.getDeltaMovement().x(),
        cart2Entity.getDeltaMovement().z());

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
      cart2Entity.setDeltaMovement(cart2Entity.getDeltaMovement().subtract(dampX, 0.0D, dampZ));
    }

    return true;
  }

  private void land() {
    this.launchState = LaunchState.LANDED;
    this.minecart.setMaxSpeedAirLateral(AbstractMinecart.DEFAULT_MAX_SPEED_AIR_LATERAL);
    this.minecart.setMaxSpeedAirVertical(AbstractMinecart.DEFAULT_MAX_SPEED_AIR_VERTICAL);
    this.minecart.setDragAir(AbstractMinecart.DEFAULT_AIR_DRAG);
  }

  /**
   * Returns the optimal distance between two linked carts that the LinkageHandler will attempt to
   * maintain at all times.
   *
   * @param cart1 AbstractMinecartEntity
   * @param cart2 AbstractMinecartEntity
   * @return The optimal distance
   */
  private float getOptimalDistance(RollingStock cart2) {
    float dist = 0;
    if (this.minecart instanceof Linkable handler)
      dist += handler.getOptimalDistance(cart2);
    else
      dist += OPTIMAL_LINK_DISTANCE;
    if (cart2.entity() instanceof Linkable handler)
      dist += handler.getOptimalDistance(this);
    else
      dist += OPTIMAL_LINK_DISTANCE;
    return dist;
  }

  private static double limitForce(double force) {
    return Math.copySign(Math.min(Math.abs(force), FORCE_LIMITER), force);
  }

  /**
   * Returns the square of the max distance two carts can be and still be linkable.
   *
   * @param cart1 First Cart
   * @param cart2 Second Cart
   * @return The square of the linkage distance
   */
  private float getLinkageDistanceSq(RollingStock cart2) {
    float dist = 0;
    if (this.minecart instanceof Linkable handler) {
      dist += handler.getLinkageDistance(cart2);
    } else {
      dist += MAX_LINK_DISTANCE;
    }
    if (cart2.entity() instanceof Linkable handler) {
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

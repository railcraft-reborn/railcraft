package mods.railcraft.world.entity.vehicle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.carts.LinkageManager;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.mixin.AbstractMinecartMixin;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.MathTools;
import mods.railcraft.util.MiscTools;
import mods.railcraft.util.RCEntitySelectors;
import mods.railcraft.util.Vec2D;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.track.ElevatorTrackBlock;
import mods.railcraft.world.level.block.track.behaivor.HighSpeedTools;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.IMinecartCollisionHandler;

public class MinecartHandler implements IMinecartCollisionHandler {

  private static final float OPTIMAL_DISTANCE = 1.28f;
  private static final float COEF_SPRING = 0.2f;
  private static final float COEF_SPRING_PLAYER = 0.5f;
  private static final float COEF_RESTITUTION = 0.2f;
  private static final float COEF_DAMPING = 0.4f;
  private static final float CART_LENGTH = 1.22f;
  private static final float CART_WIDTH = 0.98f;
  private static final float COLLISION_EXPANSION = 0.2f;
  private static final int MAX_INTERACT_DIST_SQ = 5 * 5;

  public static final Map<EntityType<?>, EntityType<?>> cartReplacements = new HashMap<>();

  public boolean isDerailed(AbstractMinecart cart) {
    return cart.getPersistentData().getInt(CartConstants.TAG_DERAIL) > 0;
  }

  public boolean canMount(AbstractMinecart cart) {
    return cart.getPersistentData().getInt(CartConstants.TAG_PREVENT_MOUNT) <= 0;
  }

  @Override
  public void onEntityCollision(AbstractMinecart cart, Entity other) {
    if (cart.level.isClientSide() || cart.hasPassenger(other)
        || !other.isAlive() || !cart.isAlive()) {
      return;
    }

    LinkageManager lm = RailcraftLinkageManager.INSTANCE;
    AbstractMinecart link = lm.getLinkedCartA(cart);
    if (link != null && (link == other || link.hasPassenger(other))) {
      return;
    }
    link = lm.getLinkedCartB(cart);
    if (link != null && (link == other || link.hasPassenger(other))) {
      return;
    }

    boolean isLiving = other instanceof LivingEntity;
    boolean isPlayer = other instanceof Player;

    if (isPlayer && ((Player) other).isSpectator()) {
      return;
    }

    if (other instanceof AbstractMinecart) {
      RailcraftLinkageManager.INSTANCE.tryAutoLink(cart, (AbstractMinecart) other);
    }

    this.testHighSpeedCollision(cart, other);

    if (isLiving
        && cart.level.getBlockState(cart.blockPosition()).is(RailcraftBlocks.ELEVATOR_TRACK.get())
        && other.getBoundingBox().minY < cart.getBoundingBox().maxY) {
      other.move(MoverType.SELF,
          new Vec3(0, cart.getBoundingBox().maxY - other.getBoundingBox().minY, 0));
      other.setOnGround(true);
    }

    // TODO Config entry? ( Go for it -CJ )
    if (MiscTools.RANDOM.nextFloat() < 0.001f) {
      List<AbstractMinecart> carts = EntitySearcher.findMinecarts().around(cart)
          .and(EntitySelector.ENTITY_STILL_ALIVE, RCEntitySelectors.NON_MECHANICAL)
          .in(cart.level);
      if (carts.size() >= 12) {
        primeToExplode(cart);
      }
    }

    Vec3 cartMotion = cart.getDeltaMovement();

    // TODO: needs more thought in regards to passenger handling
    if (isLiving && !isPlayer && cart.canBeRidden() && !(other instanceof IronGolem)
        && cartMotion.x() * cartMotion.x() + cartMotion.z() * cartMotion.z() > 0.001D
        && !cart.isVehicle()
        && !other.isPassenger()) {
      if (canMount(cart)) {
        other.startRiding(cart);
      }
    }

    if (isLiving
        && cart.level.getBlockState(cart.blockPosition())
            .is(RailcraftBlocks.ELEVATOR_TRACK.get())) {
      return;
    }

    Vec2D cartPos = new Vec2D(cart);
    Vec2D otherPos = new Vec2D(other);

    Vec2D unit = Vec2D.subtract(otherPos, cartPos);
    unit.normalize();

    double distance = cart.distanceTo(other);
    double depth = distance - OPTIMAL_DISTANCE;

    double forceX = 0;
    double forceZ = 0;

    if (depth < 0) {
      double spring = isPlayer ? COEF_SPRING_PLAYER : COEF_SPRING;
      double penaltyX = spring * depth * unit.getX();
      double penaltyZ = spring * depth * unit.getY();

      forceX += penaltyX;
      forceZ += penaltyZ;

      if (!isPlayer) {
        double impulseX = unit.getX();
        double impulseZ = unit.getY();
        impulseX *= -(1.0 + COEF_RESTITUTION);
        impulseZ *= -(1.0 + COEF_RESTITUTION);

        Vec2D cartVel = new Vec2D(cart.getDeltaMovement());
        Vec2D otherVel = new Vec2D(other.getDeltaMovement());

        double dot = Vec2D.subtract(otherVel, cartVel).dotProduct(unit);

        impulseX *= dot;
        impulseZ *= dot;
        impulseX *= 0.5;
        impulseZ *= 0.5;

        forceX -= impulseX;
        forceZ -= impulseZ;
      }
    }

    if (other instanceof AbstractMinecart) {
      AbstractMinecart otherCart = (AbstractMinecart) other;
      if (!cart.isPoweredCart() || otherCart.isPoweredCart()) {
        if (!TrackUtil.isCartLocked(cart)) {
          cart.setDeltaMovement(cart.getDeltaMovement().add(forceX, 0, forceZ));
        }
      }
      if (!otherCart.isPoweredCart() || cart.isPoweredCart()) {
        if (!TrackUtil.isCartLocked(otherCart)) {
          other.setDeltaMovement(other.getDeltaMovement().add(-forceX, 0, -forceZ));
        }
      }
    } else {
      Vec2D cartVel = new Vec2D(cart.getDeltaMovement());
      cartVel.add(forceX, forceZ);
      Vec2D otherVel = new Vec2D(other.getDeltaMovement());
      otherVel.subtract(forceX, forceZ);

      double dot = Vec2D.subtract(otherVel, cartVel).dotProduct(unit);

      double dampX = COEF_DAMPING * dot * unit.getX();
      double dampZ = COEF_DAMPING * dot * unit.getY();

      forceX += dampX;
      forceZ += dampZ;

      if (!isPlayer) {
        other.setDeltaMovement(other.getDeltaMovement().add(-forceX, 0.0D, -forceZ));
      }
      if (!TrackUtil.isCartLocked(cart)) {
        cart.setDeltaMovement(cart.getDeltaMovement().add(forceX, 0, forceZ));
      }
    }
  }

  private void testHighSpeedCollision(AbstractMinecart cart, Entity other) {
    boolean highSpeed = HighSpeedTools.isTravellingHighSpeed(cart);
    if (highSpeed) {
      if (other instanceof AbstractMinecart
          && Train.areInSameTrain(cart, (AbstractMinecart) other)) {
        return;
      }
      if (Train.streamCarts(cart).anyMatch(c -> c.hasPassenger(other))) {
        return;
      }

      if (other instanceof AbstractMinecart) {
        boolean otherHighSpeed =
            HighSpeedTools.isTravellingHighSpeed((AbstractMinecart) other);
        if (!otherHighSpeed || (cart.getDeltaMovement().x() > 0 ^ other.getDeltaMovement().x() > 0)
            || (cart.getDeltaMovement().z() > 0 ^ other.getDeltaMovement().z() > 0)) {
          this.primeToExplode(cart);
          return;
        }
      }

      if (!other.isAlive() || RailcraftConfig.server.highSpeedTrackIgnoredEntities.get()
          .contains(other.getType().getRegistryName().toString())) {
        return;
      }

      this.primeToExplode(cart);
    }
  }

  private void primeToExplode(AbstractMinecart cart) {
    cart.getPersistentData().putBoolean(CartConstants.TAG_EXPLODE, true);
  }

  @Override
  @Nullable
  public AABB getCollisionBox(AbstractMinecart cart, Entity other) {
    if (other instanceof ItemEntity && RailcraftConfig.server.cartsCollideWithItems.get()) {
      return other.getBoundingBox().inflate(-0.01);
    }
    return other.isPushable() ? other.getBoundingBox().inflate(-COLLISION_EXPANSION) : null;
  }

  @Override
  public AABB getMinecartCollisionBox(AbstractMinecart cart) {
    var yaw = Math.toRadians(cart.getYRot());
    var diff = ((CART_LENGTH - CART_WIDTH) / 2.0) + MinecartHandler.COLLISION_EXPANSION;
    var x = diff * Math.abs(Math.cos(yaw));
    var z = diff * Math.abs(Math.sin(yaw));
    return cart.getBoundingBox().inflate(x, MinecartHandler.COLLISION_EXPANSION, z);
  }

  @Override
  @Nullable
  public AABB getBoundingBox(AbstractMinecart cart) {
    if (cart == null || !cart.isAlive()) {
      return null;
    }
    if (RailcraftConfig.server.solidCarts.get()) {
      return cart.getBoundingBox();
    }
    return null;
  }

  /**
   * @see {@link AbstractMinecartMixin}
   * @param cart
   */
  public void handleTick(AbstractMinecart cart) {
    CompoundTag data = cart.getPersistentData();

    // Fix flip
    float distance = MathTools.getDistanceBetweenAngles(cart.getYRot(), cart.yRotO);
    float cutoff = 120F;
    if (distance < -cutoff || distance >= cutoff) {
      cart.setYRot(cart.getYRot() + 180.0F);
      cart.flipped = !cart.flipped;
      cart.setYRot(cart.getYRot() % 360.0F);
    }

    // if (SeasonPlugin.isGhostTrain(cart)) {
    // cart.setGlowing(true);
    // data.putBoolean("ghost", true);
    // } else
    if (data.getBoolean("ghost")) {
      cart.setGlowingTag(false);
      data.putBoolean("ghost", false);
    }

    int launched = data.getInt(CartConstants.TAG_LAUNCHED);
    if (BaseRailBlock.isRail(cart.level, cart.blockPosition())) {
      cart.fallDistance = 0;
      if (cart.isVehicle()) {
        cart.getPassengers().forEach(p -> p.fallDistance = 0);
      }
      if (launched > 1) {
        this.land(cart);
      }
    } else if (launched == 1) {
      data.putInt(CartConstants.TAG_LAUNCHED, 2);
      cart.setCanUseRail(true);
    } else if (launched > 1 && cart.isOnGround()) {
      this.land(cart);
    }

    int mountPrevention = data.getInt(CartConstants.TAG_PREVENT_MOUNT);
    if (mountPrevention > 0) {
      mountPrevention--;
      data.putInt(CartConstants.TAG_PREVENT_MOUNT, mountPrevention);
    }

    byte elevator = data.getByte(CartConstants.TAG_ELEVATOR);
    if (elevator < ElevatorTrackBlock.ELEVATOR_TIMER) {
      cart.setNoGravity(false);
    }
    if (elevator > 0) {
      elevator--;
      data.putByte(CartConstants.TAG_ELEVATOR, elevator);
    }

    byte derail = data.getByte(CartConstants.TAG_DERAIL);
    if (derail > 0) {
      derail--;
      data.putByte(CartConstants.TAG_DERAIL, derail);
      // nothing ever sets it to false, so why set it true here?
      // if (derail == 0) {
      // cart.setCanUseRail(true);
      // }
    }

    if (data.getBoolean(CartConstants.TAG_EXPLODE)) {
      cart.getPersistentData().putBoolean(CartConstants.TAG_EXPLODE, false);
      CartTools.explodeCart(cart);
    }

    if (data.getBoolean(CartConstants.TAG_HIGH_SPEED)) {
      if (CartTools.cartVelocityIsLessThan(cart, HighSpeedTools.SPEED_EXPLODE)) {
        data.putBoolean(CartConstants.TAG_HIGH_SPEED, false);
      } else if (data.getInt(CartConstants.TAG_LAUNCHED) == 0) {
        HighSpeedTools.checkSafetyAndExplode(cart.level, cart.blockPosition(), cart);
      }
    }

    Vec3 motion = cart.getDeltaMovement();

    double motionX = Math.copySign(Math.min(Math.abs(motion.x()), 9.5), motion.x());
    double motionY = Math.copySign(Math.min(Math.abs(motion.y()), 9.5), motion.y());
    double motionZ = Math.copySign(Math.min(Math.abs(motion.z()), 9.5), motion.z());

    cart.setDeltaMovement(motionX, motionY, motionZ);
  }

  private void land(AbstractMinecart cart) {
    cart.getPersistentData().putInt(CartConstants.TAG_LAUNCHED, 0);
    cart.setMaxSpeedAirLateral(AbstractMinecart.DEFAULT_MAX_SPEED_AIR_LATERAL);
    cart.setMaxSpeedAirVertical(AbstractMinecart.DEFAULT_MAX_SPEED_AIR_VERTICAL);
    cart.setDragAir(AbstractMinecart.DEFAULT_AIR_DRAG);
  }

  public boolean handleInteract(AbstractMinecart cart, Player player) {
    if (!(cart instanceof TunnelBore) && player.distanceToSqr(cart) > MAX_INTERACT_DIST_SQ) {
      return true;
    }
    if (!cart.isAlive()) {
      return true;
    }
    if (cart.canBeRidden()) {
      // Don't try to ride a cart if we are riding something else already
      if (player.getVehicle() != null && player.getVehicle() != cart) {
        return true;
      }
      // This prevents players from spam clicking to instantly climb elevators stacked with carts
      if (player.getVehicle() != cart && player.onClimbable()) {
        return true;
      }
    }
    if (!player.hasLineOfSight(cart)) {
      return true;
    }
    return false;
  }

  public boolean handleSpawn(Level level, AbstractMinecart cart) {
    EntityType<?> cartType = cartReplacements.get(cart.getType());
    if (cartType == null) {
      return false;
    }

    Entity replacement = cartType.create(level);
    replacement.moveTo(cart.getX(), cart.getY(), cart.getZ());
    level.addFreshEntity(replacement);
    return true;
  }
}

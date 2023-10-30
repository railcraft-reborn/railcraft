package mods.railcraft.world.entity.vehicle;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector2d;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.carts.Side;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.ModEntitySelector;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.IMinecartCollisionHandler;
import net.neoforged.neoforge.registries.ForgeRegistries;

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

  @Override
  public void onEntityCollision(AbstractMinecart cart, Entity other) {
    var level = cart.level();

    if (level.isClientSide() || cart.hasPassenger(other)
        || !other.isAlive() || !cart.isAlive()) {
      return;
    }

    var rollingStock = RollingStock.getOrThrow(cart);

    var link = rollingStock.linkAt(Side.BACK)
        .map(RollingStock::entity)
        .orElse(null);
    if (link != null && (link == other || link.hasPassenger(other))) {
      return;
    }
    link = rollingStock.linkAt(Side.FRONT)
        .map(RollingStock::entity)
        .orElse(null);
    if (link != null && (link == other || link.hasPassenger(other))) {
      return;
    }

    var isLiving = other instanceof LivingEntity;
    var isPlayer = other instanceof Player;

    if (other instanceof Player otherPlayer && otherPlayer.isSpectator()) {
      return;
    }

    if (other instanceof AbstractMinecart otherCart) {
      rollingStock.tryAutoLink(RollingStock.getOrThrow(otherCart));
    }

    this.testHighSpeedCollision(rollingStock, other);

    if (isLiving
        && level.getBlockState(cart.blockPosition()).is(RailcraftBlocks.ELEVATOR_TRACK.get())
        && other.getBoundingBox().minY < cart.getBoundingBox().maxY) {
      other.move(MoverType.SELF,
          new Vec3(0, cart.getBoundingBox().maxY - other.getBoundingBox().minY, 0));
      other.setOnGround(true);
    }

    // TODO Config entry? ( Go for it -CJ )
    if (level.getRandom().nextFloat() < 0.001f) {
      var carts = EntitySearcher.findMinecarts()
          .around(cart)
          .and(EntitySelector.ENTITY_STILL_ALIVE, ModEntitySelector.NON_MECHANICAL)
          .list(level);
      if (carts.size() >= 12) {
        rollingStock.primeExplosion();
      }
    }

    Vec3 cartMotion = cart.getDeltaMovement();

    // TODO: needs more thought in regards to passenger handling
    if (isLiving && !isPlayer && cart.canBeRidden() && !(other instanceof IronGolem)
        && cartMotion.x() * cartMotion.x() + cartMotion.z() * cartMotion.z() > 0.001D
        && !cart.isVehicle()
        && !other.isPassenger()) {
      if (rollingStock.isMountable()) {
        other.startRiding(cart);
      }
    }

    if (isLiving && level.getBlockState(cart.blockPosition())
        .is(RailcraftBlocks.ELEVATOR_TRACK.get())) {
      return;
    }

    var sub = new Vector2d(other.getX(), other.getZ()).sub(cart.getX(), cart.getZ());
    var unit = sub.equals(0, 0) ? sub : sub.normalize(); //Check for NaN

    double distance = cart.distanceTo(other);
    double depth = distance - OPTIMAL_DISTANCE;

    double forceX = 0;
    double forceZ = 0;

    if (depth < 0) {
      double spring = isPlayer ? COEF_SPRING_PLAYER : COEF_SPRING;
      double penaltyX = spring * depth * unit.x();
      double penaltyZ = spring * depth * unit.y();

      forceX += penaltyX;
      forceZ += penaltyZ;

      if (!isPlayer) {
        double impulseX = unit.x();
        double impulseZ = unit.y();
        impulseX *= -(1.0 + COEF_RESTITUTION);
        impulseZ *= -(1.0 + COEF_RESTITUTION);

        var cartVel = new Vector2d(cart.getDeltaMovement().x(), cart.getDeltaMovement().z());
        var otherVel = new Vector2d(other.getDeltaMovement().x(), other.getDeltaMovement().z());

        double dot = otherVel.sub(cartVel).dot(unit);

        impulseX *= dot;
        impulseZ *= dot;
        impulseX *= 0.5;
        impulseZ *= 0.5;

        forceX -= impulseX;
        forceZ -= impulseZ;
      }
    }

    if (other instanceof AbstractMinecart otherCart) {
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
      var cartVel = new Vector2d(cart.getDeltaMovement().x(), cart.getDeltaMovement().z())
          .add(forceX, forceZ);
      var otherVel = new Vector2d(other.getDeltaMovement().x(), other.getDeltaMovement().z())
          .sub(forceX, forceZ);

      double dot = otherVel.sub(cartVel).dot(unit);

      double dampX = COEF_DAMPING * dot * unit.x();
      double dampZ = COEF_DAMPING * dot * unit.y();

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

  private void testHighSpeedCollision(RollingStock rollingStock, Entity other) {
    var cart = rollingStock.entity();
    if (rollingStock.isHighSpeed()) {
      var otherExtension = other instanceof AbstractMinecart otherCart
          ? RollingStock.getOrThrow(otherCart)
          : null;

      if (otherExtension != null && rollingStock.isSameTrainAs(otherExtension)) {
        return;
      }

      if (rollingStock.train().entities().anyMatch(c -> c.hasPassenger(other))) {
        return;
      }

      if (otherExtension != null) {
        var otherHighSpeed = otherExtension.isHighSpeed();
        if (!otherHighSpeed || (cart.getDeltaMovement().x() > 0 ^ other.getDeltaMovement().x() > 0)
            || (cart.getDeltaMovement().z() > 0 ^ other.getDeltaMovement().z() > 0)) {
          rollingStock.primeExplosion();
          return;
        }
      }

      if (!other.isAlive() || RailcraftConfig.SERVER.highSpeedTrackIgnoredEntities.get()
          .contains(ForgeRegistries.ENTITY_TYPES.getKey(other.getType()).toString())) {
        return;
      }

      rollingStock.primeExplosion();
    }
  }

  @Override
  @Nullable
  public AABB getCollisionBox(AbstractMinecart cart, Entity other) {
    if (other instanceof ItemEntity && RailcraftConfig.SERVER.cartsCollideWithItems.get()) {
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
    if (RailcraftConfig.SERVER.solidCarts.get()) {
      return cart.getBoundingBox();
    }
    return null;
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
    return !player.hasLineOfSight(cart);
  }
}

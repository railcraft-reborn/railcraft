package mods.railcraft.world.entity;

import java.util.List;
import javax.annotation.Nullable;
import mods.railcraft.Railcraft;
import mods.railcraft.api.carts.CartUtil;
import mods.railcraft.api.carts.ILinkageManager;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.carts.CartConstants;
import mods.railcraft.carts.CartTools;
import mods.railcraft.carts.LinkageManager;
import mods.railcraft.carts.Train;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.MathTools;
import mods.railcraft.util.MiscTools;
import mods.railcraft.util.RCEntitySelectors;
import mods.railcraft.util.TrackTools;
import mods.railcraft.util.Vec2D;
import mods.railcraft.util.inventory.InvTools;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.track.ElevatorTrackBlock;
import mods.railcraft.world.level.block.track.behaivor.HighSpeedTools;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IMinecartCollisionHandler;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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

  public boolean isDerailed(AbstractMinecartEntity cart) {
    return cart.getPersistentData().getInt(CartConstants.TAG_DERAIL) > 0;
  }

  public boolean canMount(AbstractMinecartEntity cart) {
    return cart.getPersistentData().getInt(CartConstants.TAG_PREVENT_MOUNT) <= 0;
  }

  @SubscribeEvent
  public void onItemUse(PlayerInteractEvent.RightClickBlock event) {
    PlayerEntity player = event.getPlayer();
    World world = player.level;
    if (world.isClientSide())
      return;

    ItemStack itemStack = event.getItemStack();
    if (!itemStack.isEmpty()) {
      Item item = itemStack.getItem();
      if (CartTools.vanillaCartItemMap.containsKey(item)) {
        event.setUseItem(Event.Result.DENY);
        AbstractMinecartEntity placedCart = CartTools.placeCart(
            CartTools.vanillaCartItemMap.get(item),
            player.getGameProfile(), itemStack, (ServerWorld) world,
            event.getPos());
        if (placedCart != null && !player.isCreative())
          InvTools.dec(itemStack);
      }
    }
  }

  @Override
  public void onEntityCollision(AbstractMinecartEntity cart, Entity other) {
    if (cart.level.isClientSide() || cart.hasPassenger(other) || !other.isAlive()
        || !cart.isAlive())
      return;

    ILinkageManager lm = LinkageManager.INSTANCE;
    AbstractMinecartEntity link = lm.getLinkedCartA(cart);
    if (link != null && (link == other || link.hasPassenger(other)))
      return;
    link = lm.getLinkedCartB(cart);
    if (link != null && (link == other || link.hasPassenger(other)))
      return;

    boolean isLiving = other instanceof LivingEntity;
    boolean isPlayer = other instanceof PlayerEntity;

    if (isPlayer && ((PlayerEntity) other).isSpectator()) {
      return;
    }

    Vector3d cartMotion = cart.getDeltaMovement();

    // TODO: needs more thought in regards to passenger handling
    if (isLiving && !isPlayer && cart.canBeRidden() && !(other instanceof IronGolemEntity)
        && cartMotion.x() * cartMotion.x() + cartMotion.z() * cartMotion.z() > 0.001D
        && !cart.isVehicle() && !other.isPassenger()) {
      if (canMount(cart))
        other.startRiding(cart);
    }

    if (isLiving && cart.level.getBlockState(cart.blockPosition()).is(
        RailcraftBlocks.ELEVATOR_TRACK.get()))
      return;

    // System.out.println(cart.getClass().getSimpleName() + ": " + cart.entityId + " collided with "
    // + other.getClass().getSimpleName() + ": " + other.entityId);
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

    if (other instanceof AbstractMinecartEntity) {
      AbstractMinecartEntity otherCart = (AbstractMinecartEntity) other;
      if (!cart.isPoweredCart() || otherCart.isPoweredCart())
        if (!TrackUtil.isCartLockedDown(cart))
          cart.setDeltaMovement(cart.getDeltaMovement().add(forceX, 0, forceZ));
      if (!otherCart.isPoweredCart() || cart.isPoweredCart())
        if (!TrackUtil.isCartLockedDown(otherCart))
          other.setDeltaMovement(other.getDeltaMovement().add(-forceX, 0, -forceZ));
    } else {
      // if(isPlayer) {
      // forceX += Math.abs(cart.motionX - other.motionX) / 2;
      // forceZ += Math.abs(cart.motionZ - other.motionZ) / 2;
      // }
      // System.out.printf("forceX=%f, forceZ=%f%n", forceX, forceZ);
      Vec2D cartVel = new Vec2D(cart.getDeltaMovement());
      cartVel.add(forceX, forceZ);
      Vec2D otherVel = new Vec2D(other.getDeltaMovement());
      otherVel.subtract(forceX, forceZ);

      double dot = Vec2D.subtract(otherVel, cartVel).dotProduct(unit);

      double dampX = COEF_DAMPING * dot * unit.getX();
      double dampZ = COEF_DAMPING * dot * unit.getY();

      forceX += dampX;
      forceZ += dampZ;

      // System.out.printf("dampX=%f, dampZ=%f%n", dampX, dampZ);
      if (!isPlayer)
        other.setDeltaMovement(other.getDeltaMovement().add(-forceX, 0.0D, -forceZ));
      if (!TrackUtil.isCartLockedDown(cart))
        cart.setDeltaMovement(cart.getDeltaMovement().add(forceX, 0, forceZ));
    }
  }

  @Override
  public @Nullable AxisAlignedBB getCollisionBox(AbstractMinecartEntity cart, Entity other) {
    // return null;
    // if (other instanceof AbstractMinecartEntity && Train.areInSameTrain(cart,
    // (AbstractMinecartEntity) other))
    // return null;
    // if (Train.streamCarts(cart).anyMatch(c -> c.isPassenger(other)))
    // return null;
    if (other instanceof ItemEntity && Railcraft.serverConfig.cartsCollideWithItems.get())
      return other.getBoundingBox().inflate(-0.01);
    return other.isPushable() ? other.getBoundingBox().inflate(-COLLISION_EXPANSION) : null;
  }

  @Override
  public AxisAlignedBB getMinecartCollisionBox(AbstractMinecartEntity cart) {
    // return new AxisAlignedBB(0, 0, 0,0,0,0);
    // return cart.getEntityBoundingBox().expand(MinecartHooks.COLLISION_EXPANSION, 0,
    // MinecartHooks.COLLISION_EXPANSION);
    double yaw = Math.toRadians(cart.yRot);
    double diff = ((CART_LENGTH - CART_WIDTH) / 2.0) + MinecartHandler.COLLISION_EXPANSION;
    double x = diff * Math.abs(Math.cos(yaw));
    double z = diff * Math.abs(Math.sin(yaw));
    return cart.getBoundingBox().inflate(x, MinecartHandler.COLLISION_EXPANSION, z);
  }

  @Override
  public @Nullable AxisAlignedBB getBoundingBox(AbstractMinecartEntity cart) {
    if (cart == null || !cart.isAlive())
      return null;
    if (Railcraft.serverConfig.solidCarts.get())
      return cart.getBoundingBox();
    return null;
  }

  private void land(AbstractMinecartEntity cart) {
    cart.getPersistentData().putInt(CartConstants.TAG_LAUNCHED, 0);
    cart.setMaxSpeedAirLateral(AbstractMinecartEntity.DEFAULT_MAX_SPEED_AIR_LATERAL);
    cart.setMaxSpeedAirVertical(AbstractMinecartEntity.DEFAULT_MAX_SPEED_AIR_VERTICAL);
    cart.setDragAir(AbstractMinecartEntity.DEFAULT_AIR_DRAG);
  }

  public void handleTick(AbstractMinecartEntity cart) {
    CompoundNBT data = cart.getPersistentData();

    // Fix flip
    float distance = MathTools.getDistanceBetweenAngles(cart.yRot, cart.yRotO);
    float cutoff = 120F;
    if (distance < -cutoff || distance >= cutoff) {
      cart.yRot += 180.0F;
      cart.flipped = !cart.flipped;
      cart.yRot = cart.yRot % 360.0F;
    }


    // if (SeasonPlugin.isGhostTrain(cart)) {
    // cart.setGlowing(true);
    // data.putBoolean("ghost", true);
    // } else
    if (data.getBoolean("ghost")) {
      cart.setGlowing(false);
      data.putBoolean("ghost", false);
    }

    Block block = cart.level.getBlockState(cart.blockPosition()).getBlock();
    int launched = data.getInt(CartConstants.TAG_LAUNCHED);
    if (TrackTools.isRail(block)) {
      cart.fallDistance = 0;
      if (cart.isVehicle())
        cart.getPassengers().forEach(p -> p.fallDistance = 0);
      if (launched > 1)
        land(cart);
    } else if (launched == 1) {
      data.putInt(CartConstants.TAG_LAUNCHED, 2);
      cart.setCanUseRail(true);
    } else if (launched > 1 && cart.isOnGround())
      land(cart);

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

    if (data.getBoolean(CartConstants.TAG_HIGH_SPEED))
      if (CartTools.cartVelocityIsLessThan(cart, HighSpeedTools.SPEED_EXPLODE))
        data.putBoolean(CartConstants.TAG_HIGH_SPEED, false);
      else if (data.getInt(CartConstants.TAG_LAUNCHED) == 0)
        HighSpeedTools.checkSafetyAndExplode(cart.level, cart.blockPosition(), cart);


    Vector3d motion = cart.getDeltaMovement();

    double motionX = Math.copySign(Math.min(Math.abs(motion.x()), 9.5), motion.x());
    double motionY = Math.copySign(Math.min(Math.abs(motion.y()), 9.5), motion.y());
    double motionZ = Math.copySign(Math.min(Math.abs(motion.z()), 9.5), motion.z());

    cart.setDeltaMovement(motionX, motionY, motionZ);

    // List<LivingEntity> entities = cart.world.getEntitiesWithinAABB(LivingEntity.class,
    // getMinecartCollisionBox(cart).grow(COLLISION_EXPANSION));
    //
    // for (LivingEntity entity : entities) {
    // if (!cart.isPassenger(entity) && entity.canBePushed()) {
    // cart.applyEntityCollision(entity);
    // }
    // }
  }

  // TODO is this required?
  public void onMinecartEntityCollision(AbstractMinecartEntity cart, Entity other) {
    if (other instanceof PlayerEntity && ((PlayerEntity) other).isSpectator())
      return;

    if (cart.hasPassenger(other))
      return;

    if (other instanceof AbstractMinecartEntity)
      LinkageManager.INSTANCE.tryAutoLink(cart, (AbstractMinecartEntity) other);

    testHighSpeedCollision(cart, other);

    if (cart.getCollisionHandler() != this
        && other instanceof LivingEntity
        && cart.level.getBlockState(cart.blockPosition()).is(
            RailcraftBlocks.ELEVATOR_TRACK.get())
        && other.getBoundingBox().minY < cart.getBoundingBox().maxY) {
      other.move(MoverType.SELF, new Vector3d(0,
          cart.getBoundingBox().maxY - other.getBoundingBox().minY, 0));
      other.setOnGround(true);
    }

    // TODO Config entry? ( Go for it -CJ )
    if (MiscTools.RANDOM.nextFloat() < 0.001f) {
      List<AbstractMinecartEntity> carts = EntitySearcher.findMinecarts()
          .around(cart)
          .and(EntityPredicates.ENTITY_STILL_ALIVE, RCEntitySelectors.NON_MECHANICAL)
          .in(cart.level);
      if (carts.size() >= 12)
        primeToExplode(cart);
    }
  }

  private void testHighSpeedCollision(AbstractMinecartEntity cart, Entity other) {
    boolean highSpeed = HighSpeedTools.isTravellingHighSpeed(cart);
    if (highSpeed) {
      if (other instanceof AbstractMinecartEntity
          && Train.areInSameTrain(cart, (AbstractMinecartEntity) other))
        return;
      if (Train.streamCarts(cart).anyMatch(c -> c.hasPassenger(other)))
        return;

      if (other instanceof AbstractMinecartEntity) {
        boolean otherHighSpeed =
            HighSpeedTools.isTravellingHighSpeed((AbstractMinecartEntity) other);
        if (!otherHighSpeed || (cart.getDeltaMovement().x() > 0 ^ other.getDeltaMovement().x() > 0)
            || (cart.getDeltaMovement().z() > 0 ^ other.getDeltaMovement().z() > 0)) {
          primeToExplode(cart);
          return;
        }
      }

      if (!other.isAlive()
          || Railcraft.serverConfig.highSpeedTrackIgnoredEntities.get()
              .contains(other.getType().getRegistryName().toString()))
        return;

      primeToExplode(cart);
    }
  }

  private void primeToExplode(AbstractMinecartEntity cart) {
    cart.getPersistentData().putBoolean(CartConstants.TAG_EXPLODE, true);
  }

  public boolean handleInteract(AbstractMinecartEntity cart, PlayerEntity player) {
    if (!CartUtil.doesCartHaveOwner(cart))
      CartUtil.setCartOwner(cart, player);

    if (!(cart instanceof TunnelBoreEntity) && player.distanceToSqr(cart) > MAX_INTERACT_DIST_SQ) {
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
    if (!player.canSee(cart)) {
      return true;
    }
    return false;
  }

  // Substitutes vanilla minecart spawns.
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onMinecartSpawn(EntityJoinWorldEvent event) {
    Entity entity = event.getEntity();
    EntityType<?> cartType = CartTools.classReplacements.get(entity.getType());
    if (cartType == null) {
      return;
    }

    Entity replacement = cartType.create(event.getWorld());
    replacement.load(entity.saveWithoutId(new CompoundNBT()));
    entity.remove();
    event.setCanceled(true);
    event.getWorld().addFreshEntity(replacement);
  }

  @SubscribeEvent
  public void handleEntityLeaveWorld(EntityLeaveWorldEvent event) {
    Entity entity = event.getEntity();
    // Fix links for killed carts
    // Unloaded entities are not "isDead"
    if (!entity.level.isClientSide() && !entity.isAlive()
        && entity instanceof AbstractMinecartEntity) {
      // We only mark Trains for deletion here, this event seems to be called from outside the
      // server thread.
      Train.killTrain(((AbstractMinecartEntity) entity));
      LinkageManager.INSTANCE.breakLinks((AbstractMinecartEntity) entity);
    }
  }
}

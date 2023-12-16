package mods.railcraft.world.entity.vehicle;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.core.RailcraftFakePlayer;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.world.item.CartItem;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class MinecartUtil {

  private MinecartUtil() {}

  /**
   * Returns the cart's "speed". It is not capped by the carts max speed, it instead returns the
   * cart's "potential" speed. Used by collision and linkage logic. Do not use this to determine how
   * fast a cart is currently moving.
   *
   * @return speed
   */
  public static double getCartSpeedUncapped(Vec3 deltaMovement) {
    return Math.sqrt(getCartSpeedUncappedSquared(deltaMovement));
  }

  public static double getCartSpeedUncappedSquared(Vec3 deltaMovement) {
    return deltaMovement.x() * deltaMovement.x() + deltaMovement.z() * deltaMovement.z();
  }

  public static boolean cartVelocityIsLessThan(AbstractMinecart minecart, float velocity) {
    return cartVelocityIsLessThan(minecart.getDeltaMovement(), velocity);
  }

  public static boolean cartVelocityIsLessThan(Vec3 deltaMovement, float velocity) {
    return Math.abs(deltaMovement.x()) < velocity
        && Math.abs(deltaMovement.z()) < velocity;
  }

  public static void explodeCart(AbstractMinecart cart) {
    if (!cart.isAlive()) {
      return;
    }
    cart.setDeltaMovement(0, cart.getDeltaMovement().y(), 0);

    if (cart.level().isClientSide()) {
      return;
    }
    removePassengers(cart, cart.getX(), cart.getY() + 1.5D, cart.getZ());
    cart.level().explode(cart, cart.getX(), cart.getY(), cart.getZ(), 3F,
        Level.ExplosionInteraction.TNT);
    if (cart.level().getRandom().nextInt(2) == 0) {
      cart.kill();
    }
  }

  public static List<UUID> getMinecartUUIDsAt(Level level, BlockPos pos, float sensitivity) {
    return getMinecartUUIDsAt(level, pos.getX(), pos.getY(), pos.getZ(), sensitivity);
  }

  public static List<UUID> getMinecartUUIDsAt(Level level, int x, int y, int z, float sensitivity) {
    sensitivity = Math.min(sensitivity, 0.49f);
    return level
        .getEntitiesOfClass(AbstractMinecart.class,
            new AABB(x + sensitivity, y + sensitivity, z + sensitivity,
                x + 1 - sensitivity,
                y + 1 - sensitivity, z + 1 - sensitivity),
            EntitySelector.ENTITY_STILL_ALIVE)
        .stream()
        .map(Entity::getUUID)
        .collect(Collectors.toList());
  }


  public static void removePassengers(AbstractMinecart cart) {
    removePassengers(cart, cart.getX(), cart.getY(), cart.getZ());
  }

  public static void removePassengers(AbstractMinecart cart, double x, double y, double z) {
    var passengers = cart.getPassengers();
    cart.ejectPassengers();
    for (var entity : passengers) {
      if (entity instanceof ServerPlayer player) {
        player.setPos(x, y, z);
      } else {
        entity.moveTo(x, y, z, entity.getYRot(), entity.getXRot());
      }
    }
  }

  public static ServerPlayer getFakePlayer(AbstractMinecart cart) {
    return RailcraftFakePlayer.get((ServerLevel) cart.level(), cart.getX(), cart.getY(),
        cart.getZ());
  }

  public static ServerPlayer getFakePlayerWith(AbstractMinecart cart, ItemStack stack) {
    ServerPlayer player = getFakePlayer(cart);
    player.setItemInHand(InteractionHand.MAIN_HAND, stack);
    return player;
  }

  /**
   * Checks if the entity is in range to render.
   */
  public static boolean isInRangeToRenderDist(AbstractMinecart entity, double distance) {
    double range = entity.getBoundingBox().getSize();

    if (Double.isNaN(range)) {
      range = 1.0D;
    }

    range = range * 64.0D * CartConstants.RENDER_DIST_MULTIPLIER;
    return distance < range * range;
  }

  /**
   * Returns a minecart from a persistent UUID. Only returns carts from the same world.
   *
   * @param id Cart's persistent UUID
   * @return AbstractMinecartEntity
   */
  @Nullable
  public static AbstractMinecart getCartFromUUID(@Nullable Level level,
      @Nullable UUID id) {
    if (level == null || id == null) {
      return null;
    }

    if (level instanceof ServerLevel serverLevel) {
      var entity = serverLevel.getEntity(id);
      return entity instanceof AbstractMinecart cart && entity.isAlive() ? cart : null;
    }

    return getClientCartFromUUID(level, id);
  }

  private static AbstractMinecart getClientCartFromUUID(Level level, UUID id) {
    if (!(level instanceof ClientLevel clientLevel))
      return null;
    // for performance reasons
    // noinspection Convert2streamapi
    for (Entity entity : clientLevel.entitiesForRendering()) {
      if (entity instanceof AbstractMinecart abstractMinecart && entity.isAlive()
          && entity.getUUID().equals(id))
        return abstractMinecart;
    }
    return null;
  }

  public static boolean startBoost(AbstractMinecart cart, BlockPos pos,
      RailShape dir, double startBoost) {
    var level = cart.level();
    if (dir == RailShape.EAST_WEST) {
      if (Block.canSupportCenter(level, pos.west(), Direction.EAST)) {
        Vec3 motion = cart.getDeltaMovement();
        cart.setDeltaMovement(startBoost, motion.y(), motion.z());
        return true;

      } else if (Block.canSupportCenter(level, pos.east(), Direction.WEST)) {
        Vec3 motion = cart.getDeltaMovement();
        cart.setDeltaMovement(-startBoost, motion.y(), motion.z());
        return true;
      }
    } else if (dir == RailShape.NORTH_SOUTH) {
      if (Block.canSupportCenter(level, pos.north(), Direction.SOUTH)) {
        Vec3 motion = cart.getDeltaMovement();
        cart.setDeltaMovement(motion.x(), motion.y(), startBoost);
        return true;
      } else if (Block.canSupportCenter(level, pos.south(), Direction.NORTH)) {
        Vec3 motion = cart.getDeltaMovement();
        cart.setDeltaMovement(motion.x(), motion.y(), -startBoost);
        return true;
      }
    }
    return false;
  }

  public static void smackCart(AbstractMinecart cart, Player smacker,
      float smackVelocity) {
    smackCart(cart, cart, smacker, smackVelocity);
  }

  public static void smackCart(AbstractMinecart respect, AbstractMinecart cart,
      Player smacker,
      float smackVelocity) {
    cart.setDeltaMovement(
        cart.getDeltaMovement().add(Math.copySign(smackVelocity, respect.getX() - smacker.getX()),
            0, Math.copySign(smackVelocity, respect.getZ() - smacker.getZ())));
  }

  public static void initCartPos(AbstractMinecart entity, double x, double y, double z) {
    entity.setPos(x, y, z);
    entity.setOldPosAndRot();
    entity.setDeltaMovement(Vec3.ZERO);
  }

  @Nullable
  public static AbstractMinecart placeCart(ItemStack cartItem, ServerLevel level, BlockPos pos) {
    if (cartItem.isEmpty()) {
      return null;
    }
    var blockState = level.getBlockState(pos);
    if (!TrackUtil.isStraightTrackAt(level, pos)) {
      return null;
    }

    if (EntitySearcher.findMinecarts().at(pos).list(level).isEmpty()) {
      var trackShape = TrackUtil.getTrackDirection(level, pos, blockState);
      double h = trackShape.isAscending() ? 0.5 : 0.0;

      var cartStack = cartItem.copy();
      AbstractMinecart cart = null;

      if (cartItem.getItem() instanceof CartItem railcraftCartItem) {
        cart = railcraftCartItem.getMinecartFactory().createMinecart(cartStack, pos.getX() + 0.5,
            pos.getY() + 0.0625D + h, pos.getZ() + 0.5, level);
      } else if (cartItem.getItem() instanceof MinecartItem minecartItem) {
        cart = AbstractMinecart.createMinecart(level, pos.getX() + 0.5,
            pos.getY() + 0.0625D + h, pos.getZ() + 0.5, minecartItem.type, cartItem, null);
      }

      if (cart == null) {
        return null;
      }

      if (cartStack.hasCustomHoverName())
        cart.setCustomName(cartStack.getDisplayName());
      level.addFreshEntity(cart);
      return cart;
    }
    return null;
  }
}

package mods.railcraft.carts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import com.mojang.authlib.GameProfile;
import mods.railcraft.api.carts.CartUtil;
import mods.railcraft.api.core.RailcraftConstantsAPI;
import mods.railcraft.api.core.RailcraftFakePlayer;
import mods.railcraft.api.item.MinecartPlacer;
import mods.railcraft.plugins.PlayerPlugin;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.MiscTools;
import mods.railcraft.util.TrackTools;
import mods.railcraft.world.level.block.track.behaivor.HighSpeedTools;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public final class CartTools {

  public static final Map<Item, EntityType<?>> vanillaCartItemMap = new HashMap<>();
  public static final Map<EntityType<?>, EntityType<?>> classReplacements =
      new HashMap<>();

  /**
   * Spawns a new cart entity using the provided item.
   * <p/>
   * The backing item must implement {@code IMinecartItem} and/or extend {@code ItemMinecart}.
   * <p/>
   * Generally Forge requires all cart items to extend ItemMinecart.
   *
   * @param owner The player name that should used as the owner
   * @param cart An ItemStack containing a cart item, will not be changed by the function
   * @param world The World object
   * @return the cart placed or null if failed
   * @see MinecartPlacer , ItemMinecart
   */
  @Nullable
  public static AbstractMinecartEntity placeCart(GameProfile owner, ItemStack cart,
      ServerWorld world, BlockPos pos) {
    if (cart.isEmpty())
      return null;
    cart = cart.copy();

    EntityType<?> vanillaType = vanillaCartItemMap.get(cart.getItem());
    if (vanillaType != null)
      return placeCart(vanillaType, owner, cart, world, pos);

    return CartUtil.placeCart(owner, cart, world, pos);
  }

  public static @Nullable AbstractMinecartEntity placeCart(EntityType<?> cartType,
      GameProfile owner, ItemStack cartStack, ServerWorld world, BlockPos pos) {
    BlockState state = world.getBlockState(pos);
    if (AbstractRailBlock.isRail(state))
      if (EntitySearcher.findMinecarts().around(pos).in(world).isEmpty()) {
        RailShape trackShape = TrackTools.getTrackDirectionRaw(state);
        double h = 0.0D;
        if (trackShape.isAscending())
          h = 0.5D;

        Entity entity = cartType.create(world);
        if (entity instanceof AbstractMinecartEntity) {
          AbstractMinecartEntity cart = (AbstractMinecartEntity) entity;
          initCartPos(cart, pos.getX() + 0.5, pos.getY() + 0.0625D + h, pos.getZ() + 0.5);
          if (entity instanceof RailcraftCart)
            ((RailcraftCart) entity).initEntityFromItem(cartStack);
          if (cartStack.hasCustomHoverName())
            cart.setCustomName(cartStack.getHoverName());
          CartUtil.setCartOwner(cart, owner);
          world.addFreshEntity(cart);
          return cart;
        }
      }
    return null;
  }

  public static void explodeCart(AbstractMinecartEntity cart) {
    if (!cart.isAlive())
      return;
    HighSpeedTools.setTravellingHighSpeed(cart, false);
    cart.setDeltaMovement(0, cart.getDeltaMovement().y(), 0);

    if (cart.level.isClientSide())
      return;
    removePassengers(cart, cart.getX(), cart.getY() + 1.5D, cart.getZ());
    cart.level.explode(cart, cart.getX(), cart.getY(), cart.getZ(), 3F, Explosion.Mode.DESTROY);
    if (MiscTools.RANDOM.nextInt(2) == 0)
      cart.remove();
  }

  public static boolean cartVelocityIsLessThan(AbstractMinecartEntity cart, float vel) {
    return Math.abs(cart.getDeltaMovement().x()) < vel
        && Math.abs(cart.getDeltaMovement().z()) < vel;
  }

  public static List<UUID> getMinecartUUIDsAt(World world, BlockPos pos, float sensitivity) {
    return getMinecartUUIDsAt(world, pos.getX(), pos.getY(), pos.getZ(), sensitivity);
  }

  public static List<UUID> getMinecartUUIDsAt(World world, int x, int y, int z, float sensitivity) {
    sensitivity = Math.min(sensitivity, 0.49f);
    return world
        .getEntitiesOfClass(AbstractMinecartEntity.class,
            new AxisAlignedBB(x + sensitivity, y + sensitivity, z + sensitivity,
                x + 1 - sensitivity,
                y + 1 - sensitivity, z + 1 - sensitivity),
            EntityPredicates.ENTITY_STILL_ALIVE)
        .stream()
        .map(Entity::getUUID)
        .collect(Collectors.toList());
  }

  public static void addPassenger(AbstractMinecartEntity cart, Entity passenger) {
    passenger.startRiding(cart);
  }

  public static void removePassengers(AbstractMinecartEntity cart) {
    removePassengers(cart, cart.getX(), cart.getY(), cart.getZ());
  }

  public static void removePassengers(AbstractMinecartEntity cart, double x, double y, double z) {
    List<Entity> passengers = cart.getPassengers();
    cart.ejectPassengers();
    for (Entity entity : passengers) {
      if (entity instanceof ServerPlayerEntity) {
        ServerPlayerEntity player = ((ServerPlayerEntity) entity);
        player.setPos(x, y, z);
      } else
        entity.moveTo(x, y, z, entity.yRot, entity.xRot);
    }
  }

  public static PlayerEntity getCartOwnerEntity(AbstractMinecartEntity cart) {
    GameProfile owner = CartUtil.getCartOwner(cart);
    PlayerEntity player = null;
    if (!RailcraftConstantsAPI.UNKNOWN_PLAYER.equals(owner.getName()))
      player = PlayerPlugin.getPlayer(cart.level, owner);
    if (player == null)
      player = getFakePlayer(cart);
    return player;
  }

  public static ServerPlayerEntity getFakePlayer(AbstractMinecartEntity cart) {
    return RailcraftFakePlayer.get((ServerWorld) cart.level, cart.getX(), cart.getY(), cart.getZ());
  }

  public static ServerPlayerEntity getFakePlayerWith(AbstractMinecartEntity cart, ItemStack stack) {
    ServerPlayerEntity player = getFakePlayer(cart);
    player.setItemInHand(Hand.MAIN_HAND, stack);
    return player;
  }

  /**
   * Checks if the entity is in range to render.
   */
  public static boolean isInRangeToRenderDist(AbstractMinecartEntity entity, double distance) {
    double range = entity.getBoundingBox().getSize();

    if (Double.isNaN(range)) {
      range = 1.0D;
    }

    range = range * 64.0D * CartConstants.RENDER_DIST_MULTIPLIER;
    return distance < range * range;
  }

  public static List<String> getDebugOutput(AbstractMinecartEntity cart) {
    List<String> debug = new ArrayList<>();
    debug.add("Railcraft Minecart Data Dump");
    String cartInfo;
    if (cart.level.getGameRules().getBoolean(GameRules.RULE_REDUCEDDEBUGINFO)) {
      cartInfo = String.format("%s[\'%s\'/%d, l=\'%s\']", cart.getClass().getSimpleName(),
          cart.getName(), cart.getId(), cart.level.dimension().toString());
    } else {
      cartInfo = cart.toString();
    }
    debug.add("Object: " + cartInfo);
    debug.add("UUID: " + cart.getUUID());
    debug.add("Owner: " + CartUtil.getCartOwner(cart).getName());
    LinkageManager lm = LinkageManager.INSTANCE;
    debug.add("LinkA: " + lm.getLinkA(cart));
    debug.add("LinkB: " + lm.getLinkB(cart));
    debug.add(
        "Train: " + Train.get(cart).map(Train::getId).map(UUID::toString).orElse("NA on Client"));
    Train.get(cart).ifPresent(train -> {
      debug.add("Train Carts:");
      for (UUID uuid : train.getCarts()) {
        debug.add("  " + uuid);
      }
    });
    return debug;
  }

  /**
   * Returns a minecart from a persistent UUID. Only returns carts from the same world.
   *
   * @param id Cart's persistent UUID
   * @return AbstractMinecartEntity
   */
  public static @Nullable AbstractMinecartEntity getCartFromUUID(@Nullable World world,
      @Nullable UUID id) {
    if (world == null || id == null)
      return null;
    if (world instanceof ServerWorld) {
      Entity entity = ((ServerWorld) world).getEntity(id);
      if (entity instanceof AbstractMinecartEntity && entity.isAlive()) {
        return (AbstractMinecartEntity) entity;
      }
    } else {
      return getClientCartFromUUID(world, id);
    }
    return null;
  }

  private static AbstractMinecartEntity getClientCartFromUUID(World world, UUID id) {
    ClientWorld clientWorld = (ClientWorld) world;
    // for performance reasons
    // noinspection Convert2streamapi
    for (Entity entity : clientWorld.entitiesForRendering()) {
      if (entity instanceof AbstractMinecartEntity && entity.isAlive()
          && entity.getUUID().equals(id))
        return (AbstractMinecartEntity) entity;
    }
    return null;
  }

  public static boolean startBoost(AbstractMinecartEntity cart, BlockPos pos,
      RailShape dir, double startBoost) {
    World world = cart.level;
    if (dir == RailShape.EAST_WEST) {
      if (Block.canSupportCenter(world, pos.west(), Direction.EAST)) {
        Vector3d motion = cart.getDeltaMovement();
        cart.setDeltaMovement(startBoost, motion.y(), motion.z());
        return true;

      } else if (Block.canSupportCenter(world, pos.east(), Direction.WEST)) {
        Vector3d motion = cart.getDeltaMovement();
        cart.setDeltaMovement(-startBoost, motion.y(), motion.z());
        return true;
      }
    } else if (dir == RailShape.NORTH_SOUTH) {
      if (Block.canSupportCenter(world, pos.north(), Direction.SOUTH)) {
        Vector3d motion = cart.getDeltaMovement();
        cart.setDeltaMovement(motion.x(), motion.y(), startBoost);
        return true;
      } else if (Block.canSupportCenter(world, pos.south(), Direction.NORTH)) {
        Vector3d motion = cart.getDeltaMovement();
        cart.setDeltaMovement(motion.x(), motion.y(), -startBoost);
        return true;
      }
    }
    return false;
  }

  public static void smackCart(AbstractMinecartEntity cart, PlayerEntity smacker,
      float smackVelocity) {
    smackCart(cart, cart, smacker, smackVelocity);
  }

  public static void smackCart(AbstractMinecartEntity respect, AbstractMinecartEntity cart,
      PlayerEntity smacker,
      float smackVelocity) {
    cart.setDeltaMovement(
        cart.getDeltaMovement().add(Math.copySign(smackVelocity, respect.getX() - smacker.getX()),
            0, Math.copySign(smackVelocity, respect.getZ() - smacker.getZ())));
  }

  public static void initCartPos(AbstractMinecartEntity entity, double x, double y, double z) {
    entity.setPosAndOldPos(x, y, z);
    entity.setDeltaMovement(Vector3d.ZERO);
  }
}

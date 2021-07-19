/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.carts;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import com.mojang.authlib.GameProfile;
import mods.railcraft.api.core.RailcraftConstantsAPI;
import mods.railcraft.api.core.RailcraftFakePlayer;
import mods.railcraft.api.items.IMinecartItem;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.MinecartItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants.NBT;

public final class CartToolsAPI {

  private static ILinkageManager linkageManager = new ILinkageManager() {};
  private static ITrainTransferHelper transferHelper = new ITrainTransferHelper() {};

  public static ITrainTransferHelper transferHelper() {
    return transferHelper;
  }

  /**
   * Returns an instance of ILinkageManager.
   * <p/>
   * Will return null if Railcraft is not installed.
   *
   * @return an instance of ILinkageManager
   */
  public static ILinkageManager linkageManager() {
    return linkageManager;
  }

  /**
   * Sets a carts owner.
   * <p/>
   * The is really only needed by the bukkit ports.
   */
  public static void setCartOwner(AbstractMinecartEntity cart, PlayerEntity owner) {
    setCartOwner(cart, owner.getGameProfile());
  }

  /**
   * Sets a carts owner.
   * <p/>
   * The is really only needed by the bukkit ports.
   */
  public static void setCartOwner(AbstractMinecartEntity cart, GameProfile owner) {
    if (!cart.level.isClientSide()) {
      CompoundNBT data = cart.getPersistentData();
      data.put("owner", NBTUtil.writeGameProfile(new CompoundNBT(),
          new GameProfile(owner.getId(), owner.getName())));
      // if (owner.getName() != null)
      // data.putString("owner", owner.getName());
      // if (owner.getId() != null)
      // data.putString("ownerId", owner.getId().toString());
    }
  }

  /**
   * Gets a carts owner. (player.username)
   * <p/>
   * The is really only needed by the bukkit ports.
   */
  public static GameProfile getCartOwner(AbstractMinecartEntity cart) {
    CompoundNBT data = cart.getPersistentData();

    if (data.contains("owner", NBT.TAG_COMPOUND)) {
      GameProfile profile = NBTUtil.readGameProfile(data.getCompound("owner"));
      if (profile != null) {
        return profile;
      }
    }

    String ownerName = RailcraftConstantsAPI.UNKNOWN_PLAYER;
    if (data.contains("owner", NBT.TAG_STRING))
      ownerName = data.getString("owner");

    UUID ownerId = null;
    if (data.contains("ownerId"))
      ownerId = UUID.fromString(data.getString("ownerId"));
    return new GameProfile(ownerId, ownerName);
  }

  /**
   * Does the cart have a owner?
   * <p/>
   * The is really only needed by the bukkit ports.
   */
  public static boolean doesCartHaveOwner(AbstractMinecartEntity cart) {
    CompoundNBT data = cart.getPersistentData();
    return data.contains("owner");
  }

  /**
   * Spawns a new cart entity using the provided item.
   * <p/>
   * The backing item must implement {@code IMinecartItem} and/or extend {@code MinecartItem}.
   * <p/>
   * Generally Forge requires all cart items to extend MinecartItem.
   *
   * @param owner The player name that should used as the owner
   * @param cart An ItemStack containing a cart item, will not be changed by the function
   * @param world The World object
   * @return the cart placed or null if failed
   * @see IMinecartItem, MinecartItem
   */
  public static @Nullable AbstractMinecartEntity placeCart(GameProfile owner, ItemStack cart,
      ServerWorld world, BlockPos pos) {
    cart = cart.copy();
    if (cart.getItem() instanceof IMinecartItem) {
      IMinecartItem mi = (IMinecartItem) cart.getItem();
      return mi.placeCart(owner, cart, world, pos);
    } else if (cart.getItem() instanceof MinecartItem)
      try {
        ActionResultType placed = cart.getItem().useOn(
            new ItemUseContext(RailcraftFakePlayer.get(world, pos, cart, Hand.MAIN_HAND),
                Hand.MAIN_HAND,
                new BlockRayTraceResult(Vector3d.ZERO, Direction.DOWN, pos, false)));
        if (placed == ActionResultType.SUCCESS) {
          List<AbstractMinecartEntity> carts = getMinecartsAt(world, pos, 0.3f);
          if (!carts.isEmpty()) {
            setCartOwner(carts.get(0), owner);
            return carts.get(0);
          }
        }
      } catch (Exception e) {
        return null;
      }

    return null;
  }

  // Most of these functions have been replaced internally by the EntitySearcher, but they remain
  // here in the API
  // for historic purposes and the fact that the EntitySearcher isn't part of the API.

  public static boolean isMinecartOnRailAt(World world, BlockPos pos, float sensitivity) {
    return isMinecartOnRailAt(world, pos, sensitivity, AbstractMinecartEntity.class);
  }

  public static boolean isMinecartOnRailAt(World world, BlockPos pos, float sensitivity,
      Class<? extends AbstractMinecartEntity> type) {
    return AbstractRailBlock.isRail(world, pos) && isMinecartAt(world, pos, sensitivity, type);
  }

  public static boolean isMinecartOnAnySide(World world, BlockPos pos, float sensitivity) {
    return isMinecartOnAnySide(world, pos, sensitivity, AbstractMinecartEntity.class);
  }

  public static boolean isMinecartOnAnySide(World world, BlockPos pos, float sensitivity,
      Class<? extends AbstractMinecartEntity> type) {
    return Arrays.stream(Direction.values())
        .anyMatch(side -> !getMinecartsOnSide(world, pos, sensitivity, side, type).isEmpty());
  }

  public static boolean isMinecartAt(World world, BlockPos pos, float sensitivity) {
    return isMinecartAt(world, pos, sensitivity, AbstractMinecartEntity.class);
  }

  public static boolean isMinecartAt(World world, BlockPos pos, float sensitivity,
      Class<? extends AbstractMinecartEntity> type) {
    return !getMinecartsAt(world, pos, sensitivity, type).isEmpty();
  }

  public static List<AbstractMinecartEntity> getMinecartsOnAllSides(World world, BlockPos pos,
      float sensitivity) {
    return getMinecartsOnAllSides(world, pos, sensitivity, AbstractMinecartEntity.class);
  }

  public static <T extends AbstractMinecartEntity> List<T> getMinecartsOnAllSides(World world,
      BlockPos pos, float sensitivity, Class<T> type) {
    return Arrays.stream(Direction.values())
        .flatMap(side -> getMinecartsOnSide(world, pos, sensitivity, side, type).stream())
        .collect(Collectors.toList());
  }

  public static boolean isMinecartOnSide(World world, BlockPos pos, float sensitivity,
      Direction side) {
    return getMinecartOnSide(world, pos, sensitivity, side) != null;
  }

  public static boolean isMinecartOnSide(World world, BlockPos pos, float sensitivity,
      Direction side, Class<? extends AbstractMinecartEntity> type) {
    return getMinecartOnSide(world, pos, sensitivity, side, type) != null;
  }

  public static List<AbstractMinecartEntity> getMinecartsOnSide(World world, BlockPos pos,
      float sensitivity, Direction side) {
    return getMinecartsOnSide(world, pos, sensitivity, side, AbstractMinecartEntity.class);
  }

  public static <T extends AbstractMinecartEntity> List<T> getMinecartsOnSide(World world,
      BlockPos pos, float sensitivity, Direction side, Class<T> type) {
    return getMinecartsAt(world, pos.relative(side), sensitivity, type);
  }

  public static @Nullable AbstractMinecartEntity getMinecartOnSide(World world, BlockPos pos,
      float sensitivity, Direction side) {
    return getMinecartOnSide(world, pos, sensitivity, side, AbstractMinecartEntity.class);
  }

  public static @Nullable <T extends AbstractMinecartEntity> T getMinecartOnSide(World world,
      BlockPos pos, float sensitivity, Direction side, Class<T> type) {
    return getMinecartsOnSide(world, pos, sensitivity, side, type).stream().findAny().orElse(null);
  }

  /**
   * @param sensitivity Controls the size of the search box, ranges from (-inf, 0.49].
   */
  public static List<AbstractMinecartEntity> getMinecartsAt(World world, BlockPos pos,
      float sensitivity) {
    return getMinecartsAt(world, pos, sensitivity, AbstractMinecartEntity.class);
  }

  public static <T extends AbstractMinecartEntity> List<T> getMinecartsAt(World world, BlockPos pos,
      float sensitivity, Class<T> type) {
    sensitivity = Math.min(sensitivity, 0.49f);
    return world.getEntitiesOfClass(type,
        new AxisAlignedBB(pos.getX() + sensitivity, pos.getY(), pos.getZ() + sensitivity,
            pos.getX() + 1 - sensitivity, pos.getY() + 1 - sensitivity,
            pos.getZ() + 1 - sensitivity),
        EntityPredicates.ENTITY_STILL_ALIVE);
  }

  public static List<AbstractMinecartEntity> getMinecartsIn(World world, BlockPos p1, BlockPos p2) {
    return getMinecartsIn(world, p1, p2, AbstractMinecartEntity.class);
  }

  public static <T extends AbstractMinecartEntity> List<T> getMinecartsIn(World world, BlockPos p1,
      BlockPos p2, Class<T> type) {
    return world.getEntitiesOfClass(type,
        new AxisAlignedBB(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ()),
        EntityPredicates.ENTITY_STILL_ALIVE);
  }

  /**
   * Returns the cart's "speed". It is not capped by the carts max speed, it instead returns the
   * cart's "potential" speed. Used by collision and linkage logic. Do not use this to determine how
   * fast a cart is currently moving.
   *
   * @return speed
   */
  public static double getCartSpeedUncapped(AbstractMinecartEntity cart) {
    return Math.sqrt(getCartSpeedUncappedSquared(cart));
  }

  public static double getCartSpeedUncappedSquared(AbstractMinecartEntity cart) {
    Vector3d deltaMovement = cart.getDeltaMovement();
    return deltaMovement.x() * deltaMovement.x() + deltaMovement.z() * deltaMovement.z();
  }

  public static boolean cartVelocityIsLessThan(AbstractMinecartEntity cart, float vel) {
    return Math.abs(cart.getDeltaMovement().x()) < vel
        && Math.abs(cart.getDeltaMovement().z()) < vel;
  }

  private CartToolsAPI() {}
}

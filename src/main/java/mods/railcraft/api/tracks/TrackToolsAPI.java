/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.tracks;

import javax.annotation.Nullable;
import mods.railcraft.api.core.RailcraftFakePlayer;
import mods.railcraft.api.items.ITrackItem;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.state.Property;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * A number of utility functions related to rails.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public final class TrackToolsAPI {
  /**
   * This variable is replaced with an instance of BlockTrackOutfitted at runtime, assuming it is
   * enabled. Avoid relying on the return values of these functions unless you've checked that the
   * block in the world is indeed an instance of IBlockTrackOutfitted.
   */
  public static IBlockTrackOutfitted blockTrackOutfitted;

  /**
   * Check if the block at the location is a Track.
   */
  public static boolean isRailBlockAt(IBlockReader world, BlockPos pos) {
    return world.getBlockState(pos).getBlock() instanceof AbstractRailBlock;
  }

  public static BlockState setShape(AbstractRailBlock block, @Nullable RailShape trackShape) {
    @SuppressWarnings("deprecation")
    Property<RailShape> property = block.getShapeProperty();
    BlockState state = block.defaultBlockState();
    if (trackShape != null && property.getPossibleValues().contains(trackShape))
      state = state.setValue(property, trackShape);
    return state;
  }

  /**
   * Attempts to place a rail of the type provided. There is no need to verify that the ItemStack
   * contains a valid rail prior to calling this function.
   * <p/>
   * The function takes care of that and will return false if the ItemStack is not a valid
   * ITrackItem or an ItemBlock who's id will return true when passed to
   * AbstractRailBlock.isRailBlock(itemID).
   * <p/>
   * That means this function can place any Railcraft or vanilla rail and has at least a decent
   * chance of being able to place most third party rails.
   *
   * @param stack The ItemStack containing the rail
   * @param world The World object
   * @return true if successful
   * @see ITrackItem
   */
  public static boolean placeRailAt(ItemStack stack, ServerWorld world, BlockPos pos,
      RailShape trackShape) {
    if (stack.getItem() instanceof ITrackItem)
      return ((ITrackItem) stack.getItem()).placeTrack(stack.copy(),
          RailcraftFakePlayer.get(world, pos.relative(Direction.UP)), world, pos, trackShape);
    if (stack.getItem() instanceof BlockItem) {
      Block block = ((BlockItem) stack.getItem()).getBlock();
      if (block instanceof AbstractRailBlock) {
        BlockState blockState = setShape((AbstractRailBlock) block, trackShape);
        boolean success = world.setBlockAndUpdate(pos, blockState);
        if (success) {
          SoundType soundType = block.getSoundType(blockState, world, pos, null);
          world.playSound(null, pos,
              soundType.getPlaceSound(),
              SoundCategory.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F,
              soundType.getPitch() * 0.8F);
        }
        return success;
      }
    }
    return false;
  }

  public static boolean placeRailAt(ItemStack stack, ServerWorld world, BlockPos pos) {
    return placeRailAt(stack, world, pos, RailShape.NORTH_SOUTH);
  }

  /**
   * Returns true if the ItemStack contains a valid Railcraft Track item.
   * <p/>
   * Will return false is passed a vanilla rail.
   *
   * @param stack The ItemStack to test
   * @return true if rail
   * @see ITrackItem
   */
  public static boolean isTrackItem(@Nullable ItemStack stack) {
    return stack != null && stack.getItem() instanceof ITrackItem;
  }

  /**
   * Checks to see if a cart is being held by a ITrackLockdown.
   *
   * @param cart The cart to check
   * @return True if being held
   */
  public static boolean isCartLockedDown(AbstractMinecartEntity cart) {
    BlockPos pos = cart.blockPosition();

    if (AbstractRailBlock.isRail(cart.level, pos.below()))
      pos = pos.below();

    TileEntity tile = cart.level.getBlockEntity(pos);
    if (tile instanceof IOutfittedTrackTile) {
      ITrackKitInstance track = ((IOutfittedTrackTile) tile).getTrackKitInstance();
      return track instanceof ITrackKitLockdown
          && ((ITrackKitLockdown) track).isCartLockedDown(cart);
    }
    return false;
  }

  public static int countAdjacentTracks(World world, BlockPos pos) {
    return (int) Direction.Plane.HORIZONTAL.stream()
        .filter(side -> isTrackFuzzyAt(world, pos.relative(side)))
        .count();
  }

  public static boolean isTrackFuzzyAt(World world, BlockPos pos) {
    return AbstractRailBlock.isRail(world, pos)
        || (AbstractRailBlock.isRail(world, pos.above())
            || AbstractRailBlock.isRail(world, pos.below()));
  }

  @Nullable
  public static TrackKit getTrackKit(World world, BlockPos pos) {
    TileEntity blockEntity = world.getBlockEntity(pos);
    return blockEntity instanceof IOutfittedTrackTile
        ? ((IOutfittedTrackTile) blockEntity).getTrackKitInstance().getTrackKit()
        : null;
  }

  public static TrackType getTrackType(ItemStack stack) {
    return stack.getItem() instanceof TrackTypeLike
        ? ((TrackTypeLike) stack.getItem()).getTrackType(stack)
        : null;
  }

  private TrackToolsAPI() {}
}

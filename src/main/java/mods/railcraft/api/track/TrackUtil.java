/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.track;

import javax.annotation.Nullable;
import mods.railcraft.api.core.RailcraftFakePlayer;
import mods.railcraft.api.item.TrackPlacer;
import mods.railcraft.api.item.TrackTypeLike;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

/**
 * A number of utility functions related to rails.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public final class TrackUtil {

  public static BlockState setShape(BaseRailBlock block, @Nullable RailShape trackShape) {
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
   * @see TrackPlacer
   */
  public static boolean placeRailAt(ItemStack stack, ServerLevel world, BlockPos pos,
      RailShape trackShape) {
    if (stack.getItem() instanceof TrackPlacer)
      return ((TrackPlacer) stack.getItem()).placeTrack(stack.copy(),
          RailcraftFakePlayer.get(world, pos.relative(Direction.UP)), world, pos, trackShape);
    if (stack.getItem() instanceof BlockItem) {
      Block block = ((BlockItem) stack.getItem()).getBlock();
      if (block instanceof BaseRailBlock) {
        BlockState blockState = setShape((BaseRailBlock) block, trackShape);
        boolean success = world.setBlockAndUpdate(pos, blockState);
        if (success) {
          SoundType soundType = block.getSoundType(blockState, world, pos, null);
          world.playSound(null, pos,
              soundType.getPlaceSound(),
              SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F,
              soundType.getPitch() * 0.8F);
        }
        return success;
      }
    }
    return false;
  }

  public static boolean placeRailAt(ItemStack stack, ServerLevel world, BlockPos pos) {
    return placeRailAt(stack, world, pos, RailShape.NORTH_SOUTH);
  }

  /**
   * Returns true if the ItemStack contains a valid Railcraft Track item.
   * <p/>
   * Will return false is passed a vanilla rail.
   *
   * @param stack The ItemStack to test
   * @return true if rail
   * @see TrackPlacer
   */
  public static boolean isTrackItem(@Nullable ItemStack stack) {
    return stack != null && stack.getItem() instanceof TrackPlacer;
  }

  /**
   * Checks to see if a cart is being held by a ITrackLockdown.
   *
   * @param cart The cart to check
   * @return True if being held
   */
  public static boolean isCartLocked(AbstractMinecart cart) {
    BlockPos pos = cart.blockPosition();

    if (BaseRailBlock.isRail(cart.level, pos.below())) {
      pos = pos.below();
    }

    BlockState blockState = cart.level.getBlockState(pos);
    return blockState.getBlock() instanceof LockingTrack &&
        ((LockingTrack) blockState.getBlock()).isCartLocked(cart);
  }

  public static int countAdjacentTracks(Level world, BlockPos pos) {
    return (int) Direction.Plane.HORIZONTAL.stream()
        .filter(side -> isTrackFuzzyAt(world, pos.relative(side)))
        .count();
  }

  public static boolean isTrackFuzzyAt(Level world, BlockPos pos) {
    return BaseRailBlock.isRail(world, pos)
        || (BaseRailBlock.isRail(world, pos.above())
            || BaseRailBlock.isRail(world, pos.below()));
  }

  public static TrackType getTrackType(ItemStack stack) {
    return stack.getItem() instanceof TrackTypeLike
        ? ((TrackTypeLike) stack.getItem()).getTrackType(stack)
        : null;
  }

  private TrackUtil() {}
}

/*------------------------------------------------------------------------------
 Copyright (c) Railcraft Reborn, 2023+

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.track;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.core.RailcraftFakePlayer;
import mods.railcraft.api.item.TrackPlacer;
import mods.railcraft.api.item.TrackTypeLike;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;

/**
 * A number of utility functions related to rails.
 */
public final class TrackUtil {

  private TrackUtil() {}

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
   * @param level The World object
   * @return true if successful
   * @see TrackPlacer
   */
  public static boolean placeRailAt(ItemStack stack, ServerLevel level,
      BlockPos pos, RailShape railShape) {
    if (stack.getItem() instanceof TrackPlacer placer) {
      return placer.placeTrack(stack.copy(),
          RailcraftFakePlayer.get(level, pos.relative(Direction.UP)), level, pos, railShape);
    }

    if (stack.getItem() instanceof BlockItem blockItem
        && blockItem.getBlock() instanceof BaseRailBlock railBlock) {
      var blockState = setShape(railBlock, railShape);
      boolean success = level.setBlockAndUpdate(pos, blockState);
      if (success) {
        var soundType = railBlock.getSoundType(blockState, level, pos, null);
        level.playSound(null, pos,
            soundType.getPlaceSound(),
            SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F,
            soundType.getPitch() * 0.8F);
      }
      return success;
    }

    return false;
  }

  public static boolean placeRailAt(ItemStack stack, ServerLevel level, BlockPos pos) {
    return placeRailAt(stack, level, pos, RailShape.NORTH_SOUTH);
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
    var pos = cart.blockPosition();
    if (BaseRailBlock.isRail(cart.level(), pos.below())) {
      pos = pos.below();
    }

    var blockState = cart.level().getBlockState(pos);
    return blockState.getBlock() instanceof LockingTrack lockingTrack &&
        lockingTrack.isCartLocked(cart);
  }

  public static int countAdjacentTracks(Level level, BlockPos pos) {
    return (int) Direction.Plane.HORIZONTAL.stream()
        .filter(side -> isTrackFuzzyAt(level, pos.relative(side)))
        .count();
  }

  public static boolean isTrackFuzzyAt(Level level, BlockPos pos) {
    return BaseRailBlock.isRail(level, pos)
        || BaseRailBlock.isRail(level, pos.above())
        || BaseRailBlock.isRail(level, pos.below());
  }

  public static TrackType getTrackType(ItemStack stack) {
    return stack.getItem() instanceof TrackTypeLike trackTypeLike
        ? trackTypeLike.getTrackType(stack)
        : null;
  }

  public static boolean isStraightTrackAt(BlockGetter level, BlockPos pos) {
    return BaseRailBlock.isRail(level.getBlockState(pos))
        && !RailShapeUtil.isTurn(getTrackDirection(level, pos));
  }

  public static boolean isRail(ItemStack stack) {
    return isRail(stack.getItem());
  }

  @SuppressWarnings("deprecation")
  public static boolean isRail(Item item) {
    return item instanceof TrackPlacer
        || (item instanceof BlockItem blockItem
            && blockItem.getBlock() instanceof BaseRailBlock
            && blockItem.getBlock().builtInRegistryHolder().is(BlockTags.RAILS));
  }

  public static RailShape getTrackDirection(BlockGetter level, BlockPos pos,
      BlockState state) {
    return getTrackDirection(level, pos, state, null);
  }

  public static RailShape getTrackDirection(BlockGetter level, BlockPos pos) {
    return getTrackDirection(level, pos, (AbstractMinecart) null);
  }

  public static RailShape getTrackDirection(BlockGetter level, BlockPos pos,
      @Nullable AbstractMinecart cart) {
    return getTrackDirection(level, pos, level.getBlockState(pos), cart);
  }

  public static RailShape getTrackDirection(BlockGetter level, BlockPos pos,
      BlockState state, @Nullable AbstractMinecart cart) {
    return asRailBlock(state.getBlock()).getRailDirection(state, level, pos, cart);
  }

  public static RailShape getRailShapeRaw(BlockGetter level, BlockPos pos) {
    return getRailShapeRaw(level.getBlockState(pos));
  }

  public static RailShape getRailShapeRaw(BlockState blockState) {
    return blockState.getValue(getRailShapeProperty(blockState.getBlock()));
  }

  @SuppressWarnings("deprecation")
  public static Property<RailShape> getRailShapeProperty(Block block) {
    return asRailBlock(block).getShapeProperty();
  }

  public static BaseRailBlock asRailBlock(Block block) {
    if (block instanceof BaseRailBlock railBlock) {
      return railBlock;
    }
    throw new IllegalArgumentException(BuiltInRegistries.BLOCK.getKey(block)
        + " is not a rail block.");
  }

  public static boolean setRailShape(Level level, BlockPos pos, RailShape railShape) {
    var blockState = level.getBlockState(pos);
    var prop = getRailShapeProperty(blockState.getBlock());
    if (!prop.getPossibleValues().contains(railShape)) {
      return false;
    }
    blockState = blockState.setValue(prop, railShape);
    return level.setBlockAndUpdate(pos, blockState);
  }

  public static void traverseConnectedTracks(Level level, BlockPos pos,
      BiFunction<Level, BlockPos, Boolean> action) {
    traverseConnectedTracks(level, pos, action, new HashSet<>());
  }

  private static void traverseConnectedTracks(Level level, BlockPos pos,
      BiFunction<Level, BlockPos, Boolean> action, Set<BlockPos> visited) {
    visited.add(pos);
    if (!action.apply(level, pos)) {
      return;
    }
    getConnectedTracks(level, pos).stream()
        .filter(p -> !visited.contains(p))
        .forEach(p -> traverseConnectedTracks(level, p, action, visited));
  }

  public static Set<BlockPos> getConnectedTracks(LevelReader level, BlockPos pos) {
    final RailShape shape = BaseRailBlock.isRail(level.getBlockState(pos))
        ? getRailShapeRaw(level, pos)
        : RailShape.NORTH_SOUTH;
    return Direction.Plane.HORIZONTAL.stream()
        .flatMap(side -> getTrackConnectedTrackAt(level, pos.relative(side), shape).stream())
        .collect(Collectors.toSet());
  }

  public static Optional<BlockPos> getTrackConnectedTrackAt(LevelReader level, BlockPos pos,
      RailShape shape) {
    if (BaseRailBlock.isRail(level.getBlockState(pos))) {
      return Optional.of(pos);
    }

    var up = pos.above();
    if (shape.isAscending() && BaseRailBlock.isRail(level.getBlockState(up))) {
      return Optional.of(up);
    }

    var down = pos.below();
    if (BaseRailBlock.isRail(level.getBlockState(down))
        && getRailShapeRaw(level, down).isAscending()) {
      return Optional.of(down);
    }

    return Optional.empty();
  }

  public static RailShape getAxisAlignedDirection(Direction.Axis axis) {
    return switch (axis) {
      case X -> RailShape.EAST_WEST;
      case Z -> RailShape.NORTH_SOUTH;
      default -> throw new IllegalArgumentException("No corresponding direction for other axes.");
    };
  }

  public static RailShape getAxisAlignedDirection(Direction facing) {
    return getAxisAlignedDirection(facing.getAxis());
  }

  public static Optional<Direction> getSideFacingTrack(Level level, BlockPos pos) {
    return Arrays.stream(Direction.values())
        .filter(dir -> BaseRailBlock.isRail(level, pos.relative(dir)))
        .findFirst();
  }
}

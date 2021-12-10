package mods.railcraft.util;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import mods.railcraft.api.item.TrackPlacer;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.api.track.TypedTrack;
import mods.railcraft.world.level.block.track.TrackTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
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
 * @author CovertJaguar <https://www.railcraft.info>
 */
public final class TrackTools {

  public static final int TRAIN_LOCKDOWN_DELAY = 200;

  public static boolean isStraightTrackAt(BlockGetter world, BlockPos pos) {
    return BaseRailBlock.isRail(world.getBlockState(pos))
        && TrackShapeHelper.isStraight(getTrackDirection(world, pos));
  }

  public static boolean isRail(ItemStack stack) {
    return isRail(stack.getItem());
  }

  public static boolean isRail(Item item) {
    return item instanceof TrackPlacer
        || (item instanceof BlockItem blockItem
            && blockItem.getBlock() instanceof BaseRailBlock
            && BlockTags.RAILS.contains(blockItem.getBlock()));
  }

  public static RailShape getTrackDirection(BlockGetter world, BlockPos pos,
      BlockState state) {
    return getTrackDirection(world, pos, state, null);
  }

  public static RailShape getTrackDirection(BlockGetter world, BlockPos pos) {
    return getTrackDirection(world, pos, (AbstractMinecart) null);
  }

  public static RailShape getTrackDirection(BlockGetter world, BlockPos pos,
      @Nullable AbstractMinecart cart) {
    return getTrackDirection(world, pos, world.getBlockState(pos), cart);
  }

  public static RailShape getTrackDirection(BlockGetter world, BlockPos pos,
      BlockState state, @Nullable AbstractMinecart cart) {
    if (state.getBlock() instanceof BaseRailBlock)
      return ((BaseRailBlock) state.getBlock()).getRailDirection(state, world, pos, cart);
    throw new IllegalArgumentException("Block was not a track");
  }

  public static RailShape getRailShapeRaw(BlockGetter level, BlockPos pos) {
    return getRailShapeRaw(level.getBlockState(pos));
  }

  public static RailShape getRailShapeRaw(BlockState blockState) {
    return blockState.getValue(getRailShapeProperty(blockState.getBlock()));
  }

  @SuppressWarnings("deprecation")
  public static Property<RailShape> getRailShapeProperty(Block block) {
    if (block instanceof BaseRailBlock) {
      return ((BaseRailBlock) block).getShapeProperty();
    }
    throw new IllegalArgumentException("Block was not a track");
  }

  public static boolean setRailShape(Level level, BlockPos pos, RailShape railShape) {
    BlockState blockState = level.getBlockState(pos);
    Property<RailShape> prop = getRailShapeProperty(blockState.getBlock());
    if (prop.getPossibleValues().contains(railShape)) {
      blockState = blockState.setValue(prop, railShape);
      return level.setBlockAndUpdate(pos, blockState);
    }
    return false;
  }

  public static TrackType getTrackTypeAt(BlockGetter world, BlockPos pos) {
    return getTrackTypeAt(world, pos, world.getBlockState(pos));
  }

  public static TrackType getTrackTypeAt(BlockGetter world, BlockPos pos, BlockState state) {
    if (state.getBlock() instanceof TypedTrack) {
      return ((TypedTrack) state.getBlock()).getTrackType();
    }
    return TrackTypes.IRON.get();
  }

  // public static boolean isTrackAt(IBlockReader world, BlockPos pos, TrackKits track, Block block)
  // {
  // return isTrackSpecAt(world, pos, track.getTrackKit(), block);
  // }
  //
  // public static boolean isTrackAt(IBlockReader world, BlockPos pos, TrackKits track) {
  // return isTrackSpecAt(world, pos, track.getTrackKit());
  // }

  // public static boolean isTrackSpecAt(IBlockReader world, BlockPos pos, TrackKit trackKit, Block
  // block) {
  // if (!RailcraftBlocks.TRACK.isEqual(block))
  // return false;
  // TileEntity tile = WorldPlugin.getBlockTile(world, pos);
  // return isTrackSpec(tile, trackKit);
  // }

  // public static boolean isTrackSpecAt(IBlockReader world, BlockPos pos, TrackKit trackKit) {
  // return isTrackSpecAt(world, pos, trackKit, WorldPlugin.getBlock(world, pos));
  // }

  // public static boolean isTrackSpec(TileEntity tile, TrackKit trackKit) {
  // return (tile instanceof TileTrackOutfitted) && ((TileTrackOutfitted)
  // tile).getTrackKitInstance().getTrackKit() == trackKit;
  // }

  // public static boolean isTrackClassAt(IBlockReader world, BlockPos pos, Class<? extends
  // ITrackKitInstance> trackClass, Block block) {
  // if (!RailcraftBlocks.TRACK.isEqual(block))
  // return false;
  // TileEntity tile = WorldPlugin.getBlockTile(world, pos);
  // return isTrackClass(tile, trackClass);
  // }

  // public static boolean isTrackClassAt(IBlockReader world, BlockPos pos, Class<? extends
  // ITrackKitInstance> trackClass) {
  // return isTrackClassAt(world, pos, trackClass, WorldPlugin.getBlock(world, pos));
  // }

  // public static boolean isTrackClass(TileEntity tile, Class<? extends ITrackKitInstance>
  // trackClass) {
  // return (tile instanceof TileTrackOutfitted) &&
  // trackClass.isAssignableFrom(((TileTrackOutfitted) tile).getTrackKitInstance().getClass());
  // }

  public static void traverseConnectedTracks(Level world, BlockPos pos,
      BiFunction<Level, BlockPos, Boolean> action) {
    _traverseConnectedTracks(world, pos, action, new HashSet<>());
  }

  private static void _traverseConnectedTracks(Level world, BlockPos pos,
      BiFunction<Level, BlockPos, Boolean> action, Set<BlockPos> visited) {
    visited.add(pos);
    if (!action.apply(world, pos))
      return;
    getConnectedTracks(world, pos).stream().filter(p -> !visited.contains(p))
        .forEach(p -> _traverseConnectedTracks(world, p, action, visited));
  }

  public static Set<BlockPos> getConnectedTracks(LevelReader level, BlockPos pos) {
    final RailShape shape = BaseRailBlock.isRail(level.getBlockState(pos))
        ? getRailShapeRaw(level, pos)
        : RailShape.NORTH_SOUTH;
    return Direction.Plane.HORIZONTAL.stream()
        .map(side -> getTrackConnectedTrackAt(level, pos.relative(side), shape))
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
  }

  @Nullable
  public static BlockPos getTrackConnectedTrackAt(LevelReader level, BlockPos pos,
      RailShape shape) {
    if (BaseRailBlock.isRail(level.getBlockState(pos)))
      return pos;
    BlockPos up = pos.above();
    if (shape.isAscending() && BaseRailBlock.isRail(level.getBlockState(up)))
      return up;
    BlockPos down = pos.below();
    if (BaseRailBlock.isRail(level.getBlockState(down))
        && getRailShapeRaw(level, down).isAscending())
      return down;
    return null;
  }

  // public static Optional<TileTrackOutfitted> placeTrack(TrackKit track, World world, BlockPos
  // pos, AbstractRailBlock.RailShape direction) {
  // BlockTrackOutfitted block = (BlockTrackOutfitted) RailcraftBlocks.TRACK.block();
  // TileTrackOutfitted tile = null;
  // if (block != null) {
  // WorldPlugin.setBlockState(world, pos, TrackToolsAPI.makeTrackState(block, direction));
  // tile = TrackTileFactory.makeTrackTile(track);
  // world.setTileEntity(pos, tile);
  // }
  // //noinspection ConstantConditions
  // return Optional.ofNullable(tile);
  // }

  public static RailShape getAxisAlignedDirection(Axis axis) {
    switch (axis) {
      case X:
        return RailShape.EAST_WEST;
      case Z:
        return RailShape.NORTH_SOUTH;
      default:
        throw new IllegalArgumentException("No corresponding direction for other axes");
    }
  }

  public static RailShape getAxisAlignedDirection(Direction facing) {
    return getAxisAlignedDirection(facing.getAxis());
  }

  private TrackTools() {}

}

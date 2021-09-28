package mods.railcraft.util;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import mods.railcraft.api.items.ITrackItem;
import mods.railcraft.api.tracks.TrackType;
import mods.railcraft.api.tracks.TrackTypeProvider;
import mods.railcraft.world.level.block.track.TrackTypes;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.Property;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public final class TrackTools {

  public static final int TRAIN_LOCKDOWN_DELAY = 200;

  public static boolean isStraightTrackAt(IBlockReader world, BlockPos pos) {
    return AbstractRailBlock.isRail(world.getBlockState(pos))
        && TrackShapeHelper.isStraight(getTrackDirection(world, pos));
  }

  public static boolean isRail(ItemStack stack) {
    return isRail(stack.getItem());
  }

  public static boolean isRail(Block block) {
    return block.is(BlockTags.RAILS) && block instanceof AbstractRailBlock;
  }

  public static boolean isRail(Item item) {
    return item instanceof ITrackItem
        || (item instanceof BlockItem && isRail(((BlockItem) item).getBlock()));
  }

  public static RailShape getTrackDirection(IBlockReader world, BlockPos pos,
      BlockState state) {
    return getTrackDirection(world, pos, state, null);
  }

  public static RailShape getTrackDirection(IBlockReader world, BlockPos pos) {
    return getTrackDirection(world, pos, (AbstractMinecartEntity) null);
  }

  public static RailShape getTrackDirection(IBlockReader world, BlockPos pos,
      @Nullable AbstractMinecartEntity cart) {
    return getTrackDirection(world, pos, world.getBlockState(pos), cart);
  }

  public static RailShape getTrackDirection(IBlockReader world, BlockPos pos,
      BlockState state, @Nullable AbstractMinecartEntity cart) {
    if (state.getBlock() instanceof AbstractRailBlock)
      return ((AbstractRailBlock) state.getBlock()).getRailDirection(state, world, pos, cart);
    throw new IllegalArgumentException("Block was not a track");
  }

  public static RailShape getTrackDirectionRaw(IBlockReader world, BlockPos pos) {
    return getTrackDirectionRaw(world.getBlockState(pos));
  }

  public static RailShape getTrackDirectionRaw(BlockState state) {
    Property<RailShape> prop = getRailDirectionProperty(state.getBlock());
    return state.getValue(prop);
  }

  @SuppressWarnings("deprecation")
  public static Property<RailShape> getRailDirectionProperty(Block block) {
    if (block instanceof AbstractRailBlock)
      return ((AbstractRailBlock) block).getShapeProperty();
    throw new IllegalArgumentException("Block was not a track");
  }

  public static boolean setTrackDirection(World world, BlockPos pos, RailShape wanted) {
    BlockState state = world.getBlockState(pos);
    Property<RailShape> prop = getRailDirectionProperty(state.getBlock());
    if (prop.getPossibleValues().contains(wanted)) {
      state = state.setValue(prop, wanted);
      return world.setBlockAndUpdate(pos, state);
    }
    return false;
  }

  public static TrackType getTrackTypeAt(IBlockReader world, BlockPos pos) {
    return getTrackTypeAt(world, pos, world.getBlockState(pos));
  }

  public static TrackType getTrackTypeAt(IBlockReader world, BlockPos pos, BlockState state) {
    if (state.getBlock() instanceof TrackTypeProvider) {
      return ((TrackTypeProvider) state.getBlock()).getTrackType();
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

  public static void traverseConnectedTracks(World world, BlockPos pos,
      BiFunction<World, BlockPos, Boolean> action) {
    _traverseConnectedTracks(world, pos, action, new HashSet<>());
  }

  private static void _traverseConnectedTracks(World world, BlockPos pos,
      BiFunction<World, BlockPos, Boolean> action, Set<BlockPos> visited) {
    visited.add(pos);
    if (!action.apply(world, pos))
      return;
    getConnectedTracks(world, pos).stream().filter(p -> !visited.contains(p))
        .forEach(p -> _traverseConnectedTracks(world, p, action, visited));
  }

  public static Set<BlockPos> getConnectedTracks(IWorldReader level, BlockPos pos) {
    final RailShape shape = AbstractRailBlock.isRail(level.getBlockState(pos))
        ? getTrackDirectionRaw(level, pos)
        : RailShape.NORTH_SOUTH;
    return Direction.Plane.HORIZONTAL.stream()
        .map(side -> getTrackConnectedTrackAt(level, pos.relative(side), shape))
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
  }

  @Nullable
  public static BlockPos getTrackConnectedTrackAt(IWorldReader level, BlockPos pos,
      RailShape shape) {
    if (AbstractRailBlock.isRail(level.getBlockState(pos)))
      return pos;
    BlockPos up = pos.above();
    if (shape.isAscending() && AbstractRailBlock.isRail(level.getBlockState(up)))
      return up;
    BlockPos down = pos.below();
    if (AbstractRailBlock.isRail(level.getBlockState(down))
        && getTrackDirectionRaw(level, down).isAscending())
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

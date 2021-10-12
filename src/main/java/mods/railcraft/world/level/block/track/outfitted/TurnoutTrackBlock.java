package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import javax.annotation.Nullable;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.world.level.block.entity.track.SwitchTrackBlockEntity;
import mods.railcraft.world.level.block.entity.track.TurnoutTrackBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class TurnoutTrackBlock extends SwitchTrackBlock {

  public static final BooleanProperty MIRRORED = BooleanProperty.create("mirrored");

  public TurnoutTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(this.getShapeProperty(), RailShape.NORTH_SOUTH)
        .setValue(REVERSED, false)
        .setValue(SWITCHED, false)
        .setValue(MIRRORED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(MIRRORED);
  }

  @Override
  public SwitchTrackBlockEntity createTileEntity(BlockState state, IBlockReader reader) {
    return new TurnoutTrackBlockEntity();
  }

  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    BlockState blockState = super.getStateForPlacement(context);
    BlockPos blockPos = context.getClickedPos();
    return blockState.setValue(MIRRORED,
        this.determineMirrored(context.getLevel(), blockPos, getFacing(blockState)));
  }

  private boolean determineMirrored(World level, BlockPos blockPos, Direction facing) {
    return level
        .getBlockState(blockPos.relative(facing.getCounterClockWise()))
        .is(BlockTags.RAILS);
  }

  @Override
  public void neighborChanged(BlockState blockState, World level, BlockPos pos, Block neighborBlock,
      BlockPos neighborPos, boolean moved) {
    if (!level.isClientSide()) {
      level.setBlockAndUpdate(pos,
          blockState.setValue(MIRRORED, this.determineMirrored(level, pos, getFacing(blockState))));
    }
    super.neighborChanged(blockState, level, pos, neighborBlock, neighborPos, moved);
  }

  @Override
  public RailShape getRailDirection(BlockState blockState, IBlockReader world, BlockPos pos,
      @Nullable AbstractMinecartEntity cart) {
    final boolean mirrored = isMirrored(blockState);
    if (isSwitched(blockState)) {
      switch (getFacing(blockState)) {
        case NORTH:
          return mirrored ? RailShape.SOUTH_WEST : RailShape.SOUTH_EAST;
        case SOUTH:
          return mirrored ? RailShape.NORTH_EAST : RailShape.NORTH_WEST;
        case EAST:
          return mirrored ? RailShape.NORTH_WEST : RailShape.SOUTH_WEST;
        case WEST:
          return mirrored ? RailShape.SOUTH_EAST : RailShape.NORTH_EAST;
        default:
          throw new IllegalStateException("Invalid facing direction.");
      }
    }
    return getRailShapeRaw(blockState);
  }

  public static boolean isMirrored(BlockState blockState) {
    return blockState.getValue(MIRRORED);
  }
}

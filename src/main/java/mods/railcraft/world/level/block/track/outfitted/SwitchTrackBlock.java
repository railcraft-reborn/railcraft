package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.api.track.TrackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;

public abstract class SwitchTrackBlock extends ReversibleOutfittedTrackBlock {

  public static final BooleanProperty SWITCHED = BooleanProperty.create("switched");
  public static final EnumProperty<RailShape> SHAPE =
      EnumProperty.create("shape", RailShape.class, RailShape.NORTH_SOUTH, RailShape.EAST_WEST);

  public SwitchTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }

  @Override
  protected BlockState buildDefaultState(BlockState blockState) {
    return super.buildDefaultState(blockState)
        .setValue(SWITCHED, false);
  }

  @Override
  public Property<RailShape> getShapeProperty() {
    return SHAPE;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(SWITCHED);
  }

  @Override
  public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldBlockState,
      boolean moved) {
    super.onPlace(state, level, pos, oldBlockState, moved);
    this.updateAdjacentBlocks(state, level, pos);
  }

  protected void updateAdjacentBlocks(BlockState blockState, Level level, BlockPos pos) {
    var railShape = TrackUtil.getRailShapeRaw(blockState);
    boolean reversed = blockState.getValue(REVERSED);

    if (railShape == RailShape.NORTH_SOUTH || railShape == RailShape.EAST_WEST) {
      BlockPos offset;
      if (railShape == RailShape.NORTH_SOUTH) {
        offset = reversed ? pos.west() : pos.east();
      } else {
        offset = reversed ? pos.north() : pos.south();
      }
      if (!BaseRailBlock.isRail(level, offset)) {
        return;
      }
      TrackUtil.updateDir(level, offset);
    }
  }

  public static boolean isSwitched(BlockState blockState) {
    return blockState.getValue(SWITCHED);
  }
}

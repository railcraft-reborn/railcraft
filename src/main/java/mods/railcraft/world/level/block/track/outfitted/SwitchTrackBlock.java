package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import mods.railcraft.api.track.TrackType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
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
  protected boolean revertsToFlexTrack() {
    return false;
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
  public boolean isValidRailShape(RailShape shape) {
    // The shape is settled on placement, connecting vanilla rails must not reshape the switch
    return false;
  }

  @Override
  protected void updateState(BlockState blockState, Level level, BlockPos pos,
      Block neighborBlock) {
    // Vanilla rails only see the straight route of a switch, so connect them to every route here.
    // The state is read again as subclasses may have just changed it.
    this.updateDir(level, pos, level.getBlockState(pos), true);
  }

  public static boolean isSwitched(BlockState blockState) {
    return blockState.getValue(SWITCHED);
  }
}

package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import mods.railcraft.api.tracks.TrackType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.RailShape;

public abstract class ReversableSwitchTrackBlock extends SwitchTrackBlock {

  public static final BooleanProperty REVERSED = BooleanProperty.create("reversed");

  public ReversableSwitchTrackBlock(Supplier<? extends TrackType> trackType,
      Properties properties) {
    super(trackType, properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(this.getShapeProperty(), RailShape.NORTH_SOUTH)
        .setValue(MIRRORED, false)
        .setValue(SWITCHED, false)
        .setValue(REVERSED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(REVERSED);
  }

  public static boolean isReversed(BlockState blockState) {
    return blockState.getValue(REVERSED);
  }
}

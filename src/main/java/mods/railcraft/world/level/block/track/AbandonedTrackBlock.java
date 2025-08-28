package mods.railcraft.world.level.block.track;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class AbandonedTrackBlock extends TrackBlock {

  public static final BooleanProperty GRASS = BooleanProperty.create("grass");

  public AbandonedTrackBlock(Properties properties) {
    super(TrackTypes.ABANDONED, properties);
  }

  @Override
  protected BlockState buildDefaultState(BlockState blockState) {
    return super.buildDefaultState(blockState)
        .setValue(GRASS, false);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(GRASS);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    boolean grass = Direction.Plane.HORIZONTAL.stream()
        .anyMatch(s -> context.getLevel().getBlockState(context.getClickedPos().relative(s))
            .is(Blocks.TALL_GRASS));
    return super.getStateForPlacement(context).setValue(GRASS, grass);
  }
}

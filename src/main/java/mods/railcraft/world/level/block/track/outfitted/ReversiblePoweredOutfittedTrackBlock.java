package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import mods.railcraft.api.track.TrackType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public abstract class ReversiblePoweredOutfittedTrackBlock extends PoweredOutfittedTrackBlock {

  public static final BooleanProperty REVERSED = ReversibleOutfittedTrackBlock.REVERSED;

  public ReversiblePoweredOutfittedTrackBlock(Supplier<? extends TrackType> trackType,
      Properties properties) {
    super(trackType, properties);
  }

  @Override
  protected BlockState buildDefaultState(BlockState blockState) {
    return super.buildDefaultState(blockState)
        .setValue(REVERSED, false);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(REVERSED);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return super.getStateForPlacement(context)
        .setValue(REVERSED, context.getHorizontalDirection() == Direction.SOUTH
            || context.getHorizontalDirection() == Direction.WEST);
  }

  @Override
  public BlockState rotate(BlockState blockState, Rotation rotation) {
    return rotation == Rotation.CLOCKWISE_180 ? blockState.cycle(REVERSED) : blockState;
  }

  @Override
  protected boolean crowbarWhack(BlockState blockState, Level level, BlockPos pos,
      Player player, InteractionHand hand, ItemStack itemStack) {
    return level.setBlockAndUpdate(pos, blockState.cycle(REVERSED));
  }

  public static boolean isReversed(BlockState blockState) {
    return blockState.getValue(REVERSED);
  }
}

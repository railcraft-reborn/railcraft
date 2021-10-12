package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import mods.railcraft.api.track.TrackType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class ReversiblePoweredOutfittedTrackBlock extends PoweredOutfittedTrackBlock {

  public static final BooleanProperty REVERSED = ReversibleOutfittedTrackBlock.REVERSED;

  public ReversiblePoweredOutfittedTrackBlock(Supplier<? extends TrackType> trackType,
      Properties properties) {
    super(trackType, properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(this.getShapeProperty(), RailShape.NORTH_SOUTH)
        .setValue(POWERED, false)
        .setValue(REVERSED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(REVERSED);
  }

  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    return super.getStateForPlacement(context)
        .setValue(REVERSED, context.getHorizontalDirection() == Direction.SOUTH
            || context.getHorizontalDirection() == Direction.WEST);
  }

  @Override
  public BlockState rotate(BlockState blockState, Rotation rotation) {
    return rotation == Rotation.CLOCKWISE_180
        ? blockState.setValue(REVERSED, !blockState.getValue(REVERSED))
        : blockState;
  }

  @Override
  protected boolean crowbarWhack(BlockState blockState, World level, BlockPos pos,
      PlayerEntity player, Hand hand, ItemStack itemStack) {
    level.setBlockAndUpdate(pos, blockState.setValue(REVERSED, !blockState.getValue(REVERSED)));
    return true;
  }

  public static boolean isReversed(BlockState blockState) {
    return blockState.getValue(REVERSED);
  }
}

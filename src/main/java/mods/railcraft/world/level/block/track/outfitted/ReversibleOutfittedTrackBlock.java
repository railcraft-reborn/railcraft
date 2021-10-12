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

public class ReversibleOutfittedTrackBlock extends OutfittedTrackBlock {

  public static final BooleanProperty REVERSED = BooleanProperty.create("reversed");

  public ReversibleOutfittedTrackBlock(Supplier<? extends TrackType> trackType,
      Properties properties) {
    super(trackType, properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(this.getShapeProperty(), RailShape.NORTH_SOUTH)
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

  public static Direction getFacing(BlockState blockState) {
    return getDirection(getRailShapeRaw(blockState), isReversed(blockState));
  }

  public static Direction getDirection(RailShape railShape, boolean reversed) {
    return railShape == RailShape.NORTH_SOUTH
        ? reversed ? Direction.SOUTH : Direction.NORTH
        : reversed ? Direction.WEST : Direction.EAST;
  }
}

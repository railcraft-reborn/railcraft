package mods.railcraft.world.level.block.manipulator;

import org.jspecify.annotations.Nullable;
import com.mojang.serialization.MapCodec;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.manipulator.TrainDispenserBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.redstone.Orientation;

public class TrainDispenserBlock extends ManipulatorBlock<TrainDispenserBlockEntity> {

  public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
  private static final MapCodec<TrainDispenserBlock> CODEC = simpleCodec(TrainDispenserBlock::new);

  public TrainDispenserBlock(Properties properties) {
    super(TrainDispenserBlockEntity.class, properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(POWERED, false)
        .setValue(FACING, Direction.DOWN));
  }

  @Override
  protected MapCodec<? extends ManipulatorBlock<TrainDispenserBlockEntity>> codec() {
    return CODEC;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(FACING);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return this.defaultBlockState()
        .setValue(FACING, context.getNearestLookingDirection().getOpposite());
  }

  @Override
  public Direction getFacing(BlockState blockState) {
    return blockState.getValue(FACING);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
      BlockEntityType<T> type) {
    return level.isClientSide() ? null
        : createTickerHelper(type, RailcraftBlockEntityTypes.TRAIN_DISPENSER.get(),
            TrainDispenserBlockEntity::serverTick);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TrainDispenserBlockEntity(pos, state);
  }

  @Override
  public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block,
      @Nullable Orientation orientation, boolean isMoving) {

    boolean flag = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above());
    level.setBlock(pos, state.setValue(POWERED, flag), 4);

    if (level.isClientSide()) {
      return;
    }
    if (level.getBlockEntity(pos) instanceof TrainDispenserBlockEntity trainDispenserBlockEntity) {
      trainDispenserBlockEntity.onNeighborChange();
    }
  }
}

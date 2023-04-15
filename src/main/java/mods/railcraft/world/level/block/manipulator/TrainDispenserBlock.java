package mods.railcraft.world.level.block.manipulator;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.manipulator.TrainDispenserBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class TrainDispenserBlock extends ManipulatorBlock<TrainDispenserBlockEntity>{

  public static final DirectionProperty FACING = BlockStateProperties.FACING;

  public TrainDispenserBlock(Properties properties) {
    super(TrainDispenserBlockEntity.class, properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(POWERED, false)
        .setValue(FACING, Direction.DOWN));
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
      BlockPos fromPos, boolean isMoving) {

    boolean flag = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above());
    level.setBlock(pos, state.setValue(POWERED, flag), 4);

    if (level.isClientSide()) {
      return;
    }
    if (level.getBlockEntity(pos) instanceof TrainDispenserBlockEntity trainDispenserBlockEntity) {
      trainDispenserBlockEntity.onNeighborChange();
    }
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip,
      TooltipFlag flag) {
    tooltip.add(Component.translatable(Translations.Tips.TRAIN_DISPENSER)
        .withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable(Translations.Tips.HIT_CROWBAR_TO_ROTATE)
        .withStyle(ChatFormatting.BLUE));
    tooltip.add(Component.translatable(Translations.Tips.PAIR_WITH_CONTROL_TRACK)
        .withStyle(ChatFormatting.BLUE));
    tooltip.add(Component.translatable(Translations.Tips.APPLY_REDSTONE_TO_DISPENSE_TRAINS)
        .withStyle(ChatFormatting.RED));
  }
}

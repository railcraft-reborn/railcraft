package mods.railcraft.world.level.block.manipulator;

import mods.railcraft.util.PowerUtil;
import mods.railcraft.world.level.block.entity.manipulator.ManipulatorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public abstract class ManipulatorBlock<T extends ManipulatorBlockEntity> extends BaseEntityBlock {

  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

  private final Class<T> blockEntityType;

  protected ManipulatorBlock(Class<T> blockEntityType, Properties properties) {
    super(properties);
    this.blockEntityType = blockEntityType;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(POWERED);
  }

  @Override
  public RenderShape getRenderShape(BlockState blockState) {
    return RenderShape.MODEL;
  }

  @Override
  public InteractionResult use(BlockState blockState, Level level,
      BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
    if (!level.isClientSide()) {
      BlockEntity blockEntity = level.getBlockEntity(blockPos);
      if (!this.blockEntityType.isInstance(blockEntity)) {
        return InteractionResult.PASS;
      }

      NetworkHooks.openGui(
          (ServerPlayer) player, this.blockEntityType.cast(blockEntity), blockPos);
    }
    return InteractionResult.sidedSuccess(level.isClientSide());
  }

  public abstract Direction getFacing(BlockState blockState);

  @Override
  public int getSignal(BlockState blockState, BlockGetter level, BlockPos blockPos,
      Direction direction) {
    boolean emit = false;
    if (isPowered(blockState)) {
      var neighborBlockState = level.getBlockState(blockPos.relative(direction.getOpposite()));
      emit = BaseRailBlock.isRail(neighborBlockState)
          || neighborBlockState.is(Blocks.REDSTONE_WIRE)
          || neighborBlockState.is(Blocks.REPEATER);
    }
    return emit ? PowerUtil.FULL_POWER : PowerUtil.NO_POWER;
  }

  public static boolean isPowered(BlockState blockState) {
    return blockState.getValue(POWERED);
  }
}

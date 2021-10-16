package mods.railcraft.world.level.block;

import mods.railcraft.util.PowerUtil;
import mods.railcraft.world.level.block.entity.ManipulatorBlockEntity;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public abstract class ManipulatorBlock<T extends ManipulatorBlockEntity> extends Block {

  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

  private final Class<T> blockEntityType;

  protected ManipulatorBlock(Class<T> blockEntityType, Properties properties) {
    super(properties);
    this.blockEntityType = blockEntityType;
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(POWERED);
  }

  @Override
  public boolean hasTileEntity(BlockState blockState) {
    return true;
  }

  @Override
  public abstract T createTileEntity(BlockState blockState, IBlockReader level);

  @Override
  public ActionResultType use(BlockState blockState, World level,
      BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
    if (!level.isClientSide()) {
      TileEntity blockEntity = level.getBlockEntity(blockPos);
      if (!this.blockEntityType.isInstance(blockEntity)) {
        return ActionResultType.PASS;
      }

      NetworkHooks.openGui(
          (ServerPlayerEntity) player, this.blockEntityType.cast(blockEntity), blockPos);
    }
    return ActionResultType.sidedSuccess(level.isClientSide());
  }

  public abstract Direction getFacing(BlockState blockState);

  @Override
  public int getSignal(BlockState blockState, IBlockReader level, BlockPos blockPos,
      Direction direction) {
    boolean emit = false;
    if (isPowered(blockState)) {
      BlockState neighborBlockState =
          level.getBlockState(blockPos.relative(direction.getOpposite()));
      emit = AbstractRailBlock.isRail(neighborBlockState)
          || neighborBlockState.is(Blocks.REDSTONE_WIRE)
          || neighborBlockState.is(Blocks.REPEATER);
    }
    return emit ? PowerUtil.FULL_POWER : PowerUtil.NO_POWER;
  }

  public static boolean isPowered(BlockState blockState) {
    return blockState.getValue(POWERED);
  }
}

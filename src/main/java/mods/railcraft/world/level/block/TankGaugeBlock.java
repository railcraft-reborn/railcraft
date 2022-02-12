package mods.railcraft.world.level.block;

import javax.annotation.Nullable;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.module.TankModule;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockBlockEntity;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockListener;
import mods.railcraft.world.level.block.entity.multiblock.TankBlockEntity;
import mods.railcraft.world.level.material.fluid.tank.FilteredTank;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidAttributes;

public abstract class TankGaugeBlock extends AbstractStrengthenedGlassBlock implements EntityBlock {

  public TankGaugeBlock(Properties properties) {
    super(properties);
  }

  @Override
  public int getLightEmission(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
    return LevelUtil.getBlockEntity(blockGetter, blockPos, TankBlockEntity.class)
        .flatMap(TankBlockEntity::getMembership)
        .map(MultiblockBlockEntity.Membership::master)
        .map(TankBlockEntity::getModule)
        .map(TankModule::getTank)
        .map(FilteredTank::getFluidType)
        .map(Fluid::getAttributes)
        .map(FluidAttributes::getLuminosity)
        .orElse(super.getLightEmission(blockState, blockGetter, blockPos));
  }

  @Nullable
  @Override
  public <T extends BlockEntity> GameEventListener getListener(Level level, T blockEntity) {
    return blockEntity instanceof MultiblockBlockEntity<?> multiblock
        ? new MultiblockListener(multiblock)
        : null;
  }

  @Override
  public InteractionResult use(BlockState blockState, Level level,
      BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {

    if (level.isClientSide()) {
      return InteractionResult.SUCCESS;
    }

    return LevelUtil.getBlockEntity(level, pos, TankBlockEntity.class)
        .flatMap(MultiblockBlockEntity::getMembership)
        .map(MultiblockBlockEntity.Membership::master)
        .map(master -> {
          master.use((ServerPlayer) player, hand);
          return InteractionResult.CONSUME;
        })
        .orElse(InteractionResult.PASS);
  }

  @SuppressWarnings("unchecked")
  @Nullable
  protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
      BlockEntityType<A> type, BlockEntityType<E> expectedType,
      BlockEntityTicker<? super E> ticker) {
    return expectedType == type ? (BlockEntityTicker<A>) ticker : null;
  }
}

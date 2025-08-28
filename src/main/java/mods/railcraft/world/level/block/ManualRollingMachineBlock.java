package mods.railcraft.world.level.block;

import org.jetbrains.annotations.Nullable;
import com.mojang.serialization.MapCodec;
import mods.railcraft.Translations;
import mods.railcraft.integrations.jei.JeiSearchable;
import mods.railcraft.world.level.block.entity.ManualRollingMachineBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ManualRollingMachineBlock extends BaseEntityBlock implements JeiSearchable {

  private static final MapCodec<ManualRollingMachineBlock> CODEC =
      simpleCodec(ManualRollingMachineBlock::new);

  public ManualRollingMachineBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected MapCodec<? extends BaseEntityBlock> codec() {
    return CODEC;
  }

  @Override
  public RenderShape getRenderShape(BlockState blockState) {
    return RenderShape.MODEL;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new ManualRollingMachineBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide() ? null
        : createTickerHelper(type, RailcraftBlockEntityTypes.MANUAL_ROLLING_MACHINE.get(),
            ManualRollingMachineBlockEntity::serverTick);
  }

  @Override
  protected InteractionResult useWithoutItem(BlockState blockState, Level level,
      BlockPos pos, Player player, BlockHitResult rayTraceResult) {
    if (player instanceof ServerPlayer serverPlayer) {
      level.getBlockEntity(pos, RailcraftBlockEntityTypes.MANUAL_ROLLING_MACHINE.get())
          .ifPresent(blockEntity -> serverPlayer.openMenu(blockEntity, pos));
    }
    return InteractionResult.SUCCESS;
  }

  @Override
  public Component jeiDescription() {
    return Component.translatable(Translations.Jei.MANUAL_ROLLING_MACHINE);
  }
}

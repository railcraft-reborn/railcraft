package mods.railcraft.world.level.block;

import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.ChargeBlock;
import mods.railcraft.api.charge.ChargeStorage;
import mods.railcraft.world.level.block.entity.PoweredRollingMachineBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class PoweredRollingMachineBlock extends BaseEntityBlock implements ChargeBlock {

  private static final Map<Charge, Spec> CHARGE_SPECS =
      Spec.make(Charge.distribution, ConnectType.BLOCK, 0,
          new ChargeStorage.Spec(ChargeStorage.State.RECHARGEABLE, 1000, 1000, 1));

  public PoweredRollingMachineBlock(Properties properties) {
    super(properties);
  }

  @Override
  public RenderShape getRenderShape(BlockState blockState) {
    return RenderShape.MODEL;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new PoweredRollingMachineBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide() ? null
        : createTickerHelper(type, RailcraftBlockEntityTypes.POWERED_ROLLING_MACHINE.get(),
            PoweredRollingMachineBlockEntity::serverTick);
  }

  @Override
  public InteractionResult use(BlockState blockState, Level level,
      BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
    if (level.isClientSide()) {
      return InteractionResult.SUCCESS;
    }
    level.getBlockEntity(pos, RailcraftBlockEntityTypes.POWERED_ROLLING_MACHINE.get())
        .ifPresent(blockEntity -> NetworkHooks.openScreen((ServerPlayer) player, blockEntity, pos));
    return InteractionResult.CONSUME;
  }

  @Override
  public Map<Charge, Spec> getChargeSpecs(BlockState state, ServerLevel level, BlockPos pos) {
    return CHARGE_SPECS;
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip,
      TooltipFlag flag) {
    super.appendHoverText(stack, level, tooltip, flag);
    tooltip.add(Component.translatable(Translations.Tips.ROLLING_MACHINE).withStyle(ChatFormatting.GRAY));
  }
}

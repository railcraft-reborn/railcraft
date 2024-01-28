package mods.railcraft.world.level.block;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.integrations.jei.JeiSearchable;
import mods.railcraft.world.level.block.entity.ManualRollingMachineBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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

public class ManualRollingMachineBlock extends BaseEntityBlock implements JeiSearchable {

  public ManualRollingMachineBlock(Properties properties) {
    super(properties);
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
  public InteractionResult use(BlockState blockState, Level level,
      BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
    if (player instanceof ServerPlayer serverPlayer) {
      level.getBlockEntity(pos, RailcraftBlockEntityTypes.MANUAL_ROLLING_MACHINE.get())
          .ifPresent(blockEntity -> NetworkHooks.openScreen(serverPlayer, blockEntity, pos));
    }
    return InteractionResult.sidedSuccess(level.isClientSide());
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip,
      TooltipFlag flag) {
    super.appendHoverText(stack, level, tooltip, flag);
    tooltip.add(Component.translatable(Translations.Tips.ROLLING_MACHINE).withStyle(ChatFormatting.GRAY));
  }

  @Override
  public Component jeiDescription() {
    return Component.translatable(Translations.Jei.MANUAL_ROLLING_MACHINE);
  }
}

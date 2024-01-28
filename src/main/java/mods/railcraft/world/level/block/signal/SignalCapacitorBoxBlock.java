package mods.railcraft.world.level.block.signal;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.client.ScreenFactories;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.signal.SignalCapacitorBoxBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class SignalCapacitorBoxBlock extends SignalBoxBlock implements EntityBlock {

  public SignalCapacitorBoxBlock(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult use(BlockState blockState, Level level, BlockPos pos,
      Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
    if (level.isClientSide()) {
      level.getBlockEntity(pos, RailcraftBlockEntityTypes.SIGNAL_CAPACITOR_BOX.get())
          .ifPresent(ScreenFactories::openSignalCapacitorBoxScreen);
    }
    return InteractionResult.sidedSuccess(level.isClientSide());
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new SignalCapacitorBoxBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide() ? null
        : BaseEntityBlock.createTickerHelper(type,
            RailcraftBlockEntityTypes.SIGNAL_CAPACITOR_BOX.get(),
            SignalCapacitorBoxBlockEntity::serverTick);
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level,
      List<Component> tooltip, TooltipFlag flag) {
    tooltip.add(Component.translatable(Translations.Tips.SENDS_SIGNALS_TO_RECEIVERS)
        .withStyle(ChatFormatting.GRAY));
  }
}

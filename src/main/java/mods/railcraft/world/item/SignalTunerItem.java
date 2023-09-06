package mods.railcraft.world.item;

import java.util.List;
import mods.railcraft.Translations.Signal;
import mods.railcraft.Translations.Tips;
import mods.railcraft.api.signal.entity.SignalControllerEntity;
import mods.railcraft.api.signal.entity.SignalReceiverEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class SignalTunerItem extends PairingToolItem {

  public SignalTunerItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult onItemUseFirst(ItemStack itemStack, UseOnContext context) {
    var level = context.getLevel();
    if (!(level instanceof ServerLevel serverLevel)) {
      return InteractionResult.SUCCESS;
    }

    var player = context.getPlayer();
    var pos = context.getClickedPos();
    var blockState = level.getBlockState(pos);
    var previousTarget = getPeerPos(itemStack).orElse(null);

    if (previousTarget != null && checkAbandonPairing(previousTarget, player, serverLevel, () -> {
      if (level.getBlockEntity(pos) instanceof SignalControllerEntity provider) {
        provider.getSignalController().stopLinking();
      }
    })) {
      clearPeerPos(itemStack);
      player.displayClientMessage(Component.translatable(Signal.SIGNAL_TUNER_ABANDONED)
          .withStyle(ChatFormatting.LIGHT_PURPLE), true);
      return InteractionResult.SUCCESS;
    }

    var blockEntity = level.getBlockEntity(pos);
    if (blockEntity == null) {
      return InteractionResult.CONSUME;
    }

    if (blockEntity instanceof SignalReceiverEntity signalReceiver
        && previousTarget != null) {
      var previousTargetPos = previousTarget.pos();
      if (!pos.equals(previousTargetPos)) {
        var previousBlockEntity = level.getBlockEntity(previousTargetPos);
        if (previousBlockEntity instanceof SignalControllerEntity signalController) {
          if (blockEntity != previousBlockEntity) {
            signalController.getSignalController().addPeer(signalReceiver);
            signalController.getSignalController().stopLinking();
            player.displayClientMessage(Component.translatable(Signal.SIGNAL_TUNER_SUCCESS,
                previousBlockEntity.getBlockState().getBlock().getName(),
                blockState.getBlock().getName()).withStyle(ChatFormatting.GREEN),
                true);
            clearPeerPos(itemStack);
            return InteractionResult.SUCCESS;
          }
        } else if (level.isLoaded(previousTarget.pos())) {
          player.displayClientMessage(Component.translatable(Signal.SIGNAL_TUNER_LOST)
              .withStyle(ChatFormatting.RED), true);
          clearPeerPos(itemStack);
        } else {
          player.displayClientMessage(
              Component.translatable(Signal.SIGNAL_TUNER_UNLOADED)
                  .withStyle(ChatFormatting.RED),
              true);
          clearPeerPos(itemStack);
        }
      }
    } else if (blockEntity instanceof SignalControllerEntity provider) {
      var controller = provider.getSignalController();
      if (previousTarget == null || !pos.equals(previousTarget.pos())) {
        player.displayClientMessage(
            Component.translatable(Signal.SIGNAL_TUNER_BEGIN, blockState.getBlock().getName())
                .withStyle(ChatFormatting.LIGHT_PURPLE),
            true);
        setPeerPos(itemStack,
            GlobalPos.of(blockEntity.getLevel().dimension(), blockEntity.getBlockPos()));
        controller.startLinking();
      } else {
        player.displayClientMessage(Component.translatable(Signal.SIGNAL_TUNER_ABANDONED,
            blockState.getBlock().getName()), true);
        controller.stopLinking();
        clearPeerPos(itemStack);
      }
    } else {
      return InteractionResult.PASS;
    }

    return InteractionResult.CONSUME;
  }

  @Override
  public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip,
      TooltipFlag flag) {
    tooltip.add(Component.translatable(Tips.LINKS_CONTROLLERS_TO_RECEIVERS)
        .withStyle(ChatFormatting.GRAY));
  }
}

package mods.railcraft.world.item;

import java.util.Objects;
import mods.railcraft.Translations;
import mods.railcraft.api.core.DimensionPos;
import mods.railcraft.api.signal.SurveyableSignal;
import mods.railcraft.api.signal.TrackLocator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public class SignalBlockSurveyorItem extends PairingToolItem {

  public SignalBlockSurveyorItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult onItemUseFirst(ItemStack itemStack, UseOnContext context) {
    var player = context.getPlayer();
    var stack = context.getItemInHand();
    var level = context.getLevel();
    var pos = context.getClickedPos();
    var blockEntity = level.getBlockEntity(pos);
    if (blockEntity instanceof SurveyableSignal<?> signal) {
      if (level.isClientSide()) {
        return InteractionResult.SUCCESS;
      }

      var signalNetwork = signal.getSignalNetwork();

      if (this.checkAbandonPairing(stack, player, (ServerLevel) level,
          signalNetwork::stopLinking)) {
        player.displayClientMessage(
            Component.translatable(Translations.Misc.SIGNAL_SURVEYOR_ABANDONED), true);
        return InteractionResult.SUCCESS;
      }

      var signalPos = this.getPeerPos(stack);
      var trackStatus = signal.getTrackLocator().getTrackStatus();
      if (trackStatus == TrackLocator.Status.INVALID) {
        player.displayClientMessage(
            Component.translatable(Translations.Misc.SIGNAL_SURVEYOR_INVALID_TRACK,
                signal.getDisplayName().getString()),
            true);
      } else if (signalPos == null) {
        player.displayClientMessage(
            Component.translatable(Translations.Misc.SIGNAL_SURVEYOR_BEGIN),
            true);
        this.setPeerPos(stack, DimensionPos.from(blockEntity));
        signalNetwork.startLinking();
      } else if (!Objects.equals(pos, signalPos.getPos())) {
        blockEntity = level.getBlockEntity(signalPos.getPos());
        if (blockEntity instanceof SurveyableSignal<?> otherSignal) {
          if (this.tryLinking(signal, otherSignal)) {
            signal.getSignalNetwork().stopLinking();
            otherSignal.getSignalNetwork().stopLinking();
            player.displayClientMessage(
                Component.translatable(Translations.Misc.SIGNAL_SURVEYOR_SUCCESS),
                true);
            this.clearPeerPos(stack);
          } else {
            player.displayClientMessage(
                Component.translatable(Translations.Misc.SIGNAL_SURVEYOR_INVALID_PAIR),
                true);
          }
        } else if (level.isLoaded(signalPos.getPos())) {
          player.displayClientMessage(
              Component.translatable(Translations.Misc.SIGNAL_SURVEYOR_LOST), true);
          signalNetwork.stopLinking();
          this.clearPeerPos(stack);
        } else {
          player.displayClientMessage(
              Component.translatable(Translations.Misc.SIGNAL_SURVEYOR_UNLOADED),
              true);
        }
      } else {
        player.displayClientMessage(
            Component.translatable(Translations.Misc.SIGNAL_SURVEYOR_ABANDONED),
            true);
        signalNetwork.stopLinking();
        this.clearPeerPos(stack);
      }
    } else if (!level.isClientSide()) {
      player.displayClientMessage(
          Component.translatable(Translations.Misc.SIGNAL_SURVEYOR_INVALID_BLOCK),
          true);
    }

    return InteractionResult.PASS;
  }

  private <T, T2> boolean tryLinking(SurveyableSignal<T> signal1, SurveyableSignal<T2> signal2) {
    return signal1.getSignalType().isInstance(signal2)
        && signal2.getSignalType().isInstance(signal1)
        && signal1.getSignalNetwork().addPeer(signal1.getSignalType().cast(signal2))
        && signal2.getSignalNetwork().addPeer(signal2.getSignalType().cast(signal1));
  }
}

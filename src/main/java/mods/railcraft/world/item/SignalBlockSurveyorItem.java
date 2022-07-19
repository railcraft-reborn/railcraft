package mods.railcraft.world.item;

import mods.railcraft.api.core.DimensionPos;
import mods.railcraft.api.signal.SurveyableSignal;
import mods.railcraft.api.signal.TrackLocator;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Objects;

public class SignalBlockSurveyorItem extends PairingToolItem {

  public SignalBlockSurveyorItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult onItemUseFirst(ItemStack itemStack, UseOnContext context) {
    Player player = context.getPlayer();
    ItemStack stack = context.getItemInHand();
    Level level = context.getLevel();
    BlockPos pos = context.getClickedPos();
    BlockEntity blockEntity = level.getBlockEntity(pos);
    if (blockEntity instanceof SurveyableSignal<?> signal) {
      if (level.isClientSide()) {
        return InteractionResult.SUCCESS;
      }

      var signalNetwork = signal.getSignalNetwork();

      if (this.checkAbandonPairing(stack, player, (ServerLevel) level,
          signalNetwork::stopLinking)) {
        player.displayClientMessage(
            Component.translatable("signal_surveyor.abandoned"), true);
        return InteractionResult.SUCCESS;
      }

      var signalPos = this.getPeerPos(stack);
      var trackStatus = signal.getTrackLocator().getTrackStatus();
      if (trackStatus == TrackLocator.Status.INVALID) {
        player.displayClientMessage(Component.translatable("signal_surveyor.invalid_track",
            signal.getDisplayName().getString()), true);
      } else if (signalPos == null) {
        player.displayClientMessage(Component.translatable("signal_surveyor.begin"), true);
        this.setPeerPos(stack, DimensionPos.from(blockEntity));
        signalNetwork.startLinking();
      } else if (!Objects.equals(pos, signalPos.getPos())) {
        blockEntity = level.getBlockEntity(signalPos.getPos());
        if (blockEntity instanceof SurveyableSignal) {
          SurveyableSignal<?> otherSignal = (SurveyableSignal<?>) blockEntity;
          if (this.tryLinking(signal, otherSignal)) {
            signal.getSignalNetwork().stopLinking();
            otherSignal.getSignalNetwork().stopLinking();
            player.displayClientMessage(Component.translatable("signal_surveyor.success"),
                true);
            this.clearPeerPos(stack);
          } else {
            player.displayClientMessage(
                Component.translatable("signal_surveyor.invalid_pair"),
                true);
          }
        } else if (level.isLoaded(signalPos.getPos())) {
          player.displayClientMessage(Component.translatable("signal_surveyor.lost"), true);
          signalNetwork.stopLinking();
          this.clearPeerPos(stack);
        } else {
          player.displayClientMessage(Component.translatable("signal_surveyor.unloaded"),
              true);
        }
      } else {
        player.displayClientMessage(Component.translatable("signal_surveyor.abandoned"),
            true);
        signalNetwork.stopLinking();
        this.clearPeerPos(stack);
      }
    } else if (!level.isClientSide()) {
      player.displayClientMessage(Component.translatable("signal_surveyor.invalid_block"),
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

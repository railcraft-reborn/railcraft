package mods.railcraft.world.item;

import java.util.Objects;
import mods.railcraft.api.core.DimensionPos;
import mods.railcraft.api.signal.Signal;
import mods.railcraft.api.signal.SignalNetwork;
import mods.railcraft.api.signal.TrackLocator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class SignalBlockSurveyorItem extends PairingToolItem {

  public SignalBlockSurveyorItem(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResultType useOn(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    ItemStack stack = context.getItemInHand();
    World level = context.getLevel();
    BlockPos pos = context.getClickedPos();
    TileEntity blockEntity = level.getBlockEntity(pos);
    if (blockEntity instanceof Signal) {
      if (level.isClientSide()) {
        return ActionResultType.SUCCESS;
      }

      Signal<?> signal = (Signal<?>) blockEntity;
      SignalNetwork<?> signalNetwork = signal.getSignalNetwork();

      if (this.checkAbandonPairing(stack, player, (ServerWorld) level,
          signalNetwork::stopLinking)) {
        player.sendMessage(
            new TranslationTextComponent("signal_surveyor.abandoned"), Util.NIL_UUID);
        return ActionResultType.SUCCESS;
      }

      DimensionPos signalPos = this.getPeerPos(stack);
      TrackLocator.Status trackStatus = signal.getTrackLocator().getTrackStatus();
      if (trackStatus == TrackLocator.Status.INVALID) {
        player.sendMessage(new TranslationTextComponent("signal_surveyor.invalid_track",
            signal.getDisplayName().getString()), Util.NIL_UUID);
      } else if (signalPos == null) {
        player.sendMessage(new TranslationTextComponent("signal_surveyor.begin"), Util.NIL_UUID);
        this.setPeerPos(stack, DimensionPos.from(blockEntity));
        signalNetwork.startLinking();
      } else if (!Objects.equals(pos, signalPos.getPos())) {
        blockEntity = level.getBlockEntity(signalPos.getPos());
        if (blockEntity instanceof Signal) {
          Signal<?> otherSignal = (Signal<?>) blockEntity;
          if (this.tryLinking(signal, otherSignal)) {
            signal.getSignalNetwork().stopLinking();
            otherSignal.getSignalNetwork().stopLinking();
            player.sendMessage(new TranslationTextComponent("signal_surveyor.success"),
                Util.NIL_UUID);
            this.clearPeerPos(stack);
          } else {
            player.sendMessage(new TranslationTextComponent("signal_surveyor.invalid_pair"),
                Util.NIL_UUID);
          }
        } else if (level.isLoaded(signalPos.getPos())) {
          player.sendMessage(new TranslationTextComponent("signal_surveyor.lost"), Util.NIL_UUID);
          signalNetwork.stopLinking();
          this.clearPeerPos(stack);
        } else {
          player.sendMessage(new TranslationTextComponent("signal_surveyor.unloaded"),
              Util.NIL_UUID);
        }
      } else {
        player.sendMessage(new TranslationTextComponent("signal_surveyor.abandoned"),
            Util.NIL_UUID);
        signalNetwork.stopLinking();
        this.clearPeerPos(stack);
      }
    } else if (!level.isClientSide()) {
      player.sendMessage(new TranslationTextComponent("signal_surveyor.invalid_block"),
          Util.NIL_UUID);
    }

    return ActionResultType.PASS;
  }

  private <T, T2> boolean tryLinking(Signal<T> signal1, Signal<T2> signal2) {
    if (signal1.getSignalType().isInstance(signal2)
        && signal2.getSignalType().isInstance(signal1)) {
      return signal1.getSignalNetwork().addPeer(signal1.getSignalType().cast(signal2))
          && signal2.getSignalNetwork().addPeer(signal2.getSignalType().cast(signal1));
    }
    return false;
  }
}

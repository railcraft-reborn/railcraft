package mods.railcraft.world.item;

import mods.railcraft.api.core.DimensionPos;
import mods.railcraft.api.signal.SignalController;
import mods.railcraft.api.signal.SignalControllerProvider;
import mods.railcraft.api.signal.SignalReceiverProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

import net.minecraft.world.item.Item.Properties;

public class SignalTunerItem extends PairingToolItem {

  public SignalTunerItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult onItemUseFirst(ItemStack itemStack, UseOnContext context) {
    Player player = context.getPlayer();
    Level level = context.getLevel();
    BlockPos pos = context.getClickedPos();
    BlockState blockState = level.getBlockState(pos);

    if (!level.isClientSide()) {
      if (this.checkAbandonPairing(itemStack, player, (ServerLevel) level,
          () -> {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof SignalControllerProvider) {
              ((SignalControllerProvider) blockEntity).getSignalController().stopLinking();
            }
          })) {
        player.displayClientMessage(Component.translatable("signal_tuner.abandoned"), true);
        return InteractionResult.SUCCESS;
      }

      BlockEntity blockEntity = level.getBlockEntity(pos);
      if (blockEntity != null) {
        DimensionPos previousTarget = this.getPeerPos(itemStack);
        if (blockEntity instanceof SignalReceiverProvider && previousTarget != null) {
          if (!Objects.equals(pos, previousTarget.getPos())) {
            SignalReceiverProvider signalReceiver = (SignalReceiverProvider) blockEntity;
            BlockEntity previousBlockEntity = level.getBlockEntity(previousTarget.getPos());
            if (previousBlockEntity instanceof SignalControllerProvider) {
              SignalControllerProvider signalController =
                  (SignalControllerProvider) previousBlockEntity;
              if (blockEntity != previousBlockEntity) {
                signalController.getSignalController().addPeer(signalReceiver);
                signalController.getSignalController().stopLinking();
                player.displayClientMessage(
                    Component.translatable("signal_tuner.success",
                        previousBlockEntity.getBlockState().getBlock().getName(),
                        blockState.getBlock().getName()),
                    true);
                this.clearPeerPos(itemStack);
                return InteractionResult.SUCCESS;
              }
            } else if (level.isLoaded(previousTarget.getPos())) {
              player.displayClientMessage(
                  Component.translatable("signal_tuner.lost"),
                  true);
              this.clearPeerPos(itemStack);
            } else {
              player.displayClientMessage(
                  Component.translatable("signal_tuner.unloaded"),
                  true);
              this.clearPeerPos(itemStack);
            }
          }
        } else if (blockEntity instanceof SignalControllerProvider) {
          SignalController controller =
              ((SignalControllerProvider) blockEntity).getSignalController();
          if (previousTarget == null || !Objects.equals(pos, previousTarget.getPos())) {
            player.displayClientMessage(
                Component.translatable("signal_tuner.begin",
                    blockState.getBlock().getName()),
                true);
            this.setPeerPos(itemStack, DimensionPos.from(blockEntity));
            controller.startLinking();
          } else {
            player.displayClientMessage(
                Component.translatable("signal_tuner.abandoned",
                    blockState.getBlock().getName()),
                true);
            controller.stopLinking();
            this.clearPeerPos(itemStack);
          }
        } else
          return InteractionResult.PASS;
      }
    }

    return InteractionResult.sidedSuccess(level.isClientSide());
  }
}

package mods.railcraft.world.item;

import java.util.Objects;
import mods.railcraft.api.core.DimensionPos;
import mods.railcraft.api.signal.SignalController;
import mods.railcraft.api.signal.SignalControllerProvider;
import mods.railcraft.api.signal.SignalReceiverProvider;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class SignalTunerItem extends PairingToolItem {

  public SignalTunerItem(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResultType onItemUseFirst(ItemStack itemStack, ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    World level = context.getLevel();
    BlockPos pos = context.getClickedPos();
    BlockState blockState = level.getBlockState(pos);

    if (!level.isClientSide()) {
      if (this.checkAbandonPairing(itemStack, player, (ServerWorld) level,
          () -> {
            TileEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof SignalControllerProvider) {
              ((SignalControllerProvider) blockEntity).getSignalController().stopLinking();
            }
          })) {
        player.displayClientMessage(new TranslationTextComponent("signal_tuner.abandoned"), true);
        return ActionResultType.SUCCESS;
      }

      TileEntity blockEntity = level.getBlockEntity(pos);
      if (blockEntity != null) {
        DimensionPos previousTarget = this.getPeerPos(itemStack);
        if (blockEntity instanceof SignalReceiverProvider && previousTarget != null) {
          if (!Objects.equals(pos, previousTarget.getPos())) {
            SignalReceiverProvider signalReceiver = (SignalReceiverProvider) blockEntity;
            TileEntity previousBlockEntity = level.getBlockEntity(previousTarget.getPos());
            if (previousBlockEntity instanceof SignalControllerProvider) {
              SignalControllerProvider signalController =
                  (SignalControllerProvider) previousBlockEntity;
              if (blockEntity != previousBlockEntity) {
                signalController.getSignalController().addPeer(signalReceiver);
                signalController.getSignalController().stopLinking();
                player.displayClientMessage(
                    new TranslationTextComponent("signal_tuner.success",
                        previousBlockEntity.getBlockState().getBlock().getName(),
                        blockState.getBlock().getName()),
                    true);
                this.clearPeerPos(itemStack);
                return ActionResultType.SUCCESS;
              }
            } else if (level.isLoaded(previousTarget.getPos())) {
              player.displayClientMessage(
                  new TranslationTextComponent("signal_tuner.lost"),
                  true);
              this.clearPeerPos(itemStack);
            } else {
              player.displayClientMessage(
                  new TranslationTextComponent("signal_tuner.unloaded"),
                  true);
              this.clearPeerPos(itemStack);
            }
          }
        } else if (blockEntity instanceof SignalControllerProvider) {
          SignalController controller =
              ((SignalControllerProvider) blockEntity).getSignalController();
          if (previousTarget == null || !Objects.equals(pos, previousTarget.getPos())) {
            player.displayClientMessage(
                new TranslationTextComponent("signal_tuner.begin",
                    blockState.getBlock().getName()),
                true);
            this.setPeerPos(itemStack, DimensionPos.from(blockEntity));
            controller.startLinking();
          } else {
            player.displayClientMessage(
                new TranslationTextComponent("signal_tuner.abandoned",
                    blockState.getBlock().getName()),
                true);
            controller.stopLinking();
            this.clearPeerPos(itemStack);
          }
        } else
          return ActionResultType.PASS;
      }
    }

    return ActionResultType.sidedSuccess(level.isClientSide());
  }
}

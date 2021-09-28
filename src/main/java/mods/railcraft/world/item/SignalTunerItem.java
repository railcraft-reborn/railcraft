package mods.railcraft.world.item;

import java.util.Objects;
import mods.railcraft.api.core.WorldCoordinate;
import mods.railcraft.api.signals.SignalController;
import mods.railcraft.api.signals.SignalControllerNetwork;
import mods.railcraft.api.signals.SignalReceiver;
import mods.railcraft.world.signal.NetworkType;
import net.minecraft.block.BlockState;
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

public class SignalTunerItem extends PairingToolItem {

  public SignalTunerItem(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResultType useOn(ItemUseContext context) {
    ItemStack itemStack = context.getItemInHand();
    PlayerEntity player = context.getPlayer();
    World level = context.getLevel();
    BlockPos pos = context.getClickedPos();
    BlockState blockState = level.getBlockState(pos);

    if (!level.isClientSide()) {
      if (this.actionCleanPairing(itemStack, player, (ServerWorld) level,
          NetworkType.SIGNAL_CONTROLLER)) {
        return ActionResultType.SUCCESS;
      }

      TileEntity blockEntity = level.getBlockEntity(pos);
      if (blockEntity != null) {
        WorldCoordinate previousTarget = this.getPairData(itemStack);
        if (blockEntity instanceof SignalReceiver && previousTarget != null) {
          if (!Objects.equals(pos, previousTarget.getPos())) {
            BlockState controllerBlockState = level.getBlockState(previousTarget.getPos());
            TileEntity controllerBlockEntity = level.getBlockEntity(previousTarget.getPos());
            if (controllerBlockEntity instanceof SignalController) {
              SignalControllerNetwork controller =
                  ((SignalController) controllerBlockEntity).getSignalControllerNetwork();
              if (blockEntity != controllerBlockEntity) {
                controller.addPeer((SignalReceiver) blockEntity);
                controller.endLinking();
                player.sendMessage(
                    new TranslationTextComponent(this.getDescriptionId() + ".success",
                        controllerBlockState.getBlock().getName(), blockState.getBlock().getName()),
                    Util.NIL_UUID);
                clearPairData(itemStack);
                return ActionResultType.SUCCESS;
              }
            } else if (level.isLoaded(previousTarget.getPos())) {
              player.sendMessage(
                  new TranslationTextComponent(this.getDescriptionId() + ".abandon.gone"),
                  Util.NIL_UUID);
              clearPairData(itemStack);
            } else {
              player.sendMessage(
                  new TranslationTextComponent(this.getDescriptionId() + ".abandon.chunk"),
                  Util.NIL_UUID);
              clearPairData(itemStack);
            }
          }
        } else if (blockEntity instanceof SignalController) {
          SignalControllerNetwork controller =
              ((SignalController) blockEntity).getSignalControllerNetwork();
          if (previousTarget == null || !Objects.equals(pos, previousTarget.getPos())) {
            player.sendMessage(
                new TranslationTextComponent(this.getDescriptionId() + ".start",
                    blockState.getBlock().getName()),
                Util.NIL_UUID);
            setPairData(itemStack, blockEntity);
            controller.startLinking();
          } else {
            player.sendMessage(
                new TranslationTextComponent(this.getDescriptionId() + ".stop",
                    blockState.getBlock().getName()),
                Util.NIL_UUID);
            controller.endLinking();
            clearPairData(itemStack);
          }
        } else
          return ActionResultType.PASS;
      }
    }

    return ActionResultType.sidedSuccess(level.isClientSide());
  }
}

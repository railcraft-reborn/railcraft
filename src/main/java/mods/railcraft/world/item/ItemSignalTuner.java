package mods.railcraft.world.item;

import java.util.Objects;
import mods.railcraft.api.core.WorldCoordinate;
import mods.railcraft.api.signals.SignalControllerProvider;
import mods.railcraft.api.signals.IReceiverProvider;
import mods.railcraft.api.signals.SignalController;
import mods.railcraft.api.signals.SignalReceiver;
import mods.railcraft.world.signal.NetworkType;
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

public class ItemSignalTuner extends ItemPairingTool {

  public ItemSignalTuner(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResultType useOn(ItemUseContext context) {
    ItemStack stack = context.getItemInHand();
    PlayerEntity playerIn = context.getPlayer();
    World worldIn = context.getLevel();
    BlockPos pos = context.getClickedPos();
    if (worldIn instanceof ServerWorld
        && actionCleanPairing(stack, playerIn, (ServerWorld) worldIn, NetworkType.CONTROLLER)) {
      return ActionResultType.SUCCESS;
    }
    TileEntity recTile = worldIn.getBlockEntity(pos);
    if (recTile != null) {
      WorldCoordinate previousTarget = getPairData(stack);
      if (recTile instanceof IReceiverProvider && previousTarget != null) {
        if (!worldIn.isClientSide()) {
          SignalReceiver receiver = ((IReceiverProvider) recTile).getReceiver();
          if (!Objects.equals(pos, previousTarget.getPos())) {
            TileEntity conTile = worldIn.getBlockEntity(previousTarget.getPos());
            if (conTile instanceof SignalControllerProvider) {
              SignalController controller = ((SignalControllerProvider) conTile).getController();
              if (recTile != conTile) {
                controller.add(recTile);
                controller.endLinking();
                playerIn.sendMessage(
                    new TranslationTextComponent(this.getDescriptionId() + ".success",
                        controller.getLocalizationTag(), receiver.getLocalizationTag()),
                    Util.NIL_UUID);
                clearPairData(stack);
                return ActionResultType.SUCCESS;
              }
            } else if (worldIn.isLoaded(previousTarget.getPos())) {
              playerIn.sendMessage(
                  new TranslationTextComponent(this.getDescriptionId() + ".abandon.gone"),
                  Util.NIL_UUID);
              clearPairData(stack);
            } else {
              playerIn.sendMessage(
                  new TranslationTextComponent(this.getDescriptionId() + ".abandon.chunk"),
                  Util.NIL_UUID);
              clearPairData(stack);
            }
          }
        }
      } else if (recTile instanceof SignalControllerProvider) {
        if (!worldIn.isClientSide()) {
          SignalController controller = ((SignalControllerProvider) recTile).getController();
          if (previousTarget == null || !Objects.equals(pos, previousTarget.getPos())) {
            playerIn.sendMessage(
                new TranslationTextComponent(this.getDescriptionId() + ".start",
                    controller.getLocalizationTag()),
                Util.NIL_UUID);
            setPairData(stack, recTile);
            controller.startLinking();
          } else {
            playerIn.sendMessage(
                new TranslationTextComponent(this.getDescriptionId() + ".stop",
                    controller.getLocalizationTag()),
                Util.NIL_UUID);
            controller.endLinking();
            clearPairData(stack);
          }
        }
      } else
        return ActionResultType.PASS;
      return ActionResultType.SUCCESS;
    }
    return ActionResultType.PASS;
  }
}

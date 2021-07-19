package mods.railcraft.world.item;

import java.util.Objects;
import mods.railcraft.api.core.WorldCoordinate;
import mods.railcraft.api.signals.INetwork;
import mods.railcraft.api.signals.ISignal;
import mods.railcraft.api.signals.TrackLocator;
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

public class ItemSignalBlockSurveyor extends ItemPairingTool {

  public ItemSignalBlockSurveyor(Properties properties) {
    super(properties);
  }

  // TODO: This function could probably be picked apart and pulled into the super class, but meh...
  @Override
  public ActionResultType useOn(ItemUseContext context) {
    PlayerEntity playerIn = context.getPlayer();
    ItemStack stack = context.getItemInHand();
    World level = context.getLevel();
    BlockPos pos = context.getClickedPos();
    if (level instanceof ServerWorld
        && this.actionCleanPairing(stack, playerIn, (ServerWorld) level,
            NetworkType.BLOCK_SIGNAL)) {
      return ActionResultType.SUCCESS;
    }
    TileEntity tile = level.getBlockEntity(pos);
    if (tile != null)
      if (tile instanceof ISignal) {
        if (!level.isClientSide()) {
          ISignal signalTile = (ISignal) tile;
          INetwork signalBlock = signalTile.getNetwork();
          WorldCoordinate signalPos = getPairData(stack);
          TrackLocator.Status trackStatus = signalTile.getTrackLocator().getTrackStatus();
          if (trackStatus == TrackLocator.Status.INVALID)
            playerIn.sendMessage(new TranslationTextComponent("gui.railcraft.surveyor.track",
                signalTile.getDisplayName().getString()), Util.NIL_UUID);
          else if (signalPos == null) {
            playerIn.sendMessage(new TranslationTextComponent("gui.railcraft.surveyor.begin"),
                Util.NIL_UUID);
            setPairData(stack, tile);
            signalBlock.startLinking();
          } else if (!Objects.equals(pos, signalPos.getPos())) {
            tile = level.getBlockEntity(signalPos.getPos());
            if (tile instanceof ISignal) {
              if (signalBlock.add(tile)) {
                playerIn.sendMessage(new TranslationTextComponent("gui.railcraft.surveyor.success"),
                    Util.NIL_UUID);
                clearPairData(stack);
              } else
                playerIn.sendMessage(new TranslationTextComponent("gui.railcraft.surveyor.invalid"),
                    Util.NIL_UUID);
            } else if (level.isLoaded(signalPos.getPos())) {
              playerIn.sendMessage(new TranslationTextComponent("gui.railcraft.surveyor.lost"),
                  Util.NIL_UUID);
              signalBlock.endLinking();
              clearPairData(stack);
            } else
              playerIn.sendMessage(new TranslationTextComponent("gui.railcraft.surveyor.unloaded"),
                  Util.NIL_UUID);
          } else {
            playerIn.sendMessage(new TranslationTextComponent("gui.railcraft.surveyor.abandon"),
                Util.NIL_UUID);
            signalBlock.endLinking();
            clearPairData(stack);
          }
        }
        return ActionResultType.SUCCESS;
      } else if (!level.isClientSide())
        playerIn.sendMessage(new TranslationTextComponent("gui.railcraft.surveyor.wrong"),
            Util.NIL_UUID);
    return ActionResultType.PASS;
  }
}

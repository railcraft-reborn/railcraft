package mods.railcraft.world.item;

import javax.annotation.Nullable;
import mods.railcraft.api.core.WorldCoordinate;
import mods.railcraft.api.items.ActivationBlockingItem;
import mods.railcraft.api.items.InvToolsAPI;
import mods.railcraft.api.signals.IMutableNetwork;
import mods.railcraft.world.signal.NetworkType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

/**
 * Created by CovertJaguar on 6/7/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
@ActivationBlockingItem
public class ItemPairingTool extends Item {

  public static final String PAIR_DATA_TAG = "pairData";
  public static final String COORD_TAG = "coord";

  public ItemPairingTool(Properties properties) {
    super(properties);
  }

  public @Nullable WorldCoordinate getPairData(ItemStack stack) {
    return InvToolsAPI.getRailcraftDataSubtag(stack, PAIR_DATA_TAG)
        .map(nbt -> WorldCoordinate.readFromNBT(nbt, COORD_TAG))
        .orElse(null);
  }

  public void setPairData(ItemStack stack, TileEntity tile) {
    CompoundNBT pairData = new CompoundNBT();
    WorldCoordinate pos = WorldCoordinate.from(tile);
    pos.writeToNBT(pairData, COORD_TAG);
    InvToolsAPI.setRailcraftDataSubtag(stack, PAIR_DATA_TAG, pairData);
  }

  public void clearPairData(ItemStack stack) {
    InvToolsAPI.clearRailcraftDataSubtag(stack, PAIR_DATA_TAG);
  }

  public <T> boolean actionCleanPairing(ItemStack stack, PlayerEntity player, ServerWorld worldIn,
      NetworkType networkType) {
    WorldCoordinate signalPos = getPairData(stack);
    if (signalPos != null) {
      if (signalPos.getDim() != worldIn.dimension()) {
        abandonPairing(stack, player);
        return true;
      } else if (player.isShiftKeyDown()) {
        networkType.getNetwork(worldIn.getServer().getLevel(signalPos.getDim()), signalPos.getPos())
            .ifPresent(IMutableNetwork::endLinking);
        abandonPairing(stack, player);
        return true;
      }
    }
    return false;
  }

  private void abandonPairing(ItemStack stack, PlayerEntity playerIn) {
    playerIn.sendMessage(
        new TranslationTextComponent(this.getDescriptionId() + ".abandon"), Util.NIL_UUID);
    this.clearPairData(stack);
  }
}

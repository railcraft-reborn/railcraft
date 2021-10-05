package mods.railcraft.world.item;

import javax.annotation.Nullable;
import mods.railcraft.api.core.DimensionPos;
import mods.railcraft.api.item.ActivationBlockingItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

/**
 * Created by CovertJaguar on 6/7/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
@ActivationBlockingItem
public class PairingToolItem extends Item {

  public PairingToolItem(Properties properties) {
    super(properties);
  }

  @Nullable
  public DimensionPos getPeerPos(ItemStack itemStack) {
    CompoundNBT tag = itemStack.getTag();
    return tag != null && tag.contains("peerPos", Constants.NBT.TAG_COMPOUND)
        ? DimensionPos.readTag(tag.getCompound("peerPos"))
        : null;
  }

  public void setPeerPos(ItemStack itemStack, DimensionPos peerPos) {
    itemStack.getOrCreateTag().put("peerPos", peerPos.writeTag());
  }

  public void clearPeerPos(ItemStack itemStack) {
    CompoundNBT tag = itemStack.getTag();
    if (tag != null) {
      tag.remove("peerPos");
    }
  }

  public <T> boolean checkAbandonPairing(ItemStack itemStack, PlayerEntity player,
      ServerWorld level, Runnable stopLinking) {
    DimensionPos signalPos = this.getPeerPos(itemStack);
    if (signalPos != null) {
      if (signalPos.getDim().compareTo(level.dimension()) != 0) {
        this.clearPeerPos(itemStack);
        return true;
      } else if (player.isShiftKeyDown()) {
        stopLinking.run();
        this.clearPeerPos(itemStack);
        return true;
      }
    }
    return false;
  }
}

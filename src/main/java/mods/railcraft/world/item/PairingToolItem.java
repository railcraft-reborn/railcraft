package mods.railcraft.world.item;

import java.util.Optional;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import mods.railcraft.api.item.ActivationBlockingItem;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@ActivationBlockingItem
public class PairingToolItem extends Item {

  private static final Logger logger = LogUtils.getLogger();

  public PairingToolItem(Properties properties) {
    super(properties);
  }

  public static Optional<GlobalPos> getPeerPos(ItemStack itemStack) {
    var tag = itemStack.getTag();
    return tag != null && tag.contains("peerPos", Tag.TAG_COMPOUND)
        ? GlobalPos.CODEC
            .parse(NbtOps.INSTANCE, tag.getCompound("peerPos"))
            .resultOrPartial(logger::error)
        : Optional.empty();
  }

  public static void setPeerPos(ItemStack itemStack, GlobalPos peerPos) {
    GlobalPos.CODEC
        .encodeStart(NbtOps.INSTANCE, peerPos)
        .resultOrPartial(logger::error)
        .ifPresent(tag -> itemStack.getOrCreateTag().put("peerPos", tag));
  }

  public static void clearPeerPos(ItemStack itemStack) {
    var tag = itemStack.getTag();
    if (tag != null) {
      tag.remove("peerPos");
    }
  }

  public static <T> boolean checkAbandonPairing(GlobalPos signalPos, Player player,
      ServerLevel level, Runnable stopLinking) {
    if (signalPos.dimension().compareTo(level.dimension()) != 0) {
      return true;
    }
    if (player.isShiftKeyDown()) {
      stopLinking.run();
      return true;
    }
    return false;
  }
}

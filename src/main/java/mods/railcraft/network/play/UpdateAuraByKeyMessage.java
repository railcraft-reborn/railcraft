package mods.railcraft.network.play;

import mods.railcraft.world.item.GogglesItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.neoforge.network.NetworkEvent;

public record UpdateAuraByKeyMessage(CompoundTag tag) {

  public void encode(FriendlyByteBuf out) {
    out.writeNbt(tag);
  }

  public static UpdateAuraByKeyMessage decode(FriendlyByteBuf in) {
    return new UpdateAuraByKeyMessage(in.readNbt());
  }

  public boolean handle(NetworkEvent.Context context) {
    var player = context.getSender();
    var itemStack = player.getItemBySlot(EquipmentSlot.HEAD);
    if (itemStack.getItem() instanceof GogglesItem) {
      itemStack.setTag(tag);
    }
    return true;
  }
}

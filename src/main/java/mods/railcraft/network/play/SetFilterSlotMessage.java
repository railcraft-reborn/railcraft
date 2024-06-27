package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.world.inventory.slot.RailcraftSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public record SetFilterSlotMessage(int slotIndex, ItemStack stack) {

  public static SetFilterSlotMessage decode(FriendlyByteBuf in) {
    var slotIndex = in.readVarInt();
    var stack = in.readItem();
    return new SetFilterSlotMessage(slotIndex, stack);
  }

  public void encode(FriendlyByteBuf out) {
    out.writeVarInt(this.slotIndex);
    out.writeItem(this.stack);
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    var player = context.get().getSender();
    if (player == null) {
      return false;
    }
    if (!this.stack.isEmpty() && this.stack.getCount() <= this.stack.getMaxStackSize()) {
      var container = player.containerMenu;
      if (container != null && this.slotIndex >= 0 && this.slotIndex < container.slots.size()) {
        if (container.getSlot(this.slotIndex) instanceof RailcraftSlot slot && slot.isPhantom()) {
          if (slot.mayPlace(this.stack)) {
            slot.set(this.stack);
          }
        }
      }
    }
    return true;
  }
}

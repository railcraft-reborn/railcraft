package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.network.RailcraftCustomPacketPayload;
import mods.railcraft.world.inventory.slot.RailcraftSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record SetFilterSlotMessage(
    int slotIndex, ItemStack stack) implements RailcraftCustomPacketPayload {

  public static final ResourceLocation ID = RailcraftConstants.rl("set_filter_slot");

  public static SetFilterSlotMessage read(FriendlyByteBuf buf) {
    return new SetFilterSlotMessage(buf.readVarInt(), buf.readItem());
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeVarInt(this.slotIndex);
    buf.writeItem(this.stack);
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  @Override
  public void handle(PlayPayloadContext context) {
    context.player().ifPresent(player -> {
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
    });
  }
}

package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.inventory.slot.RailcraftSlot;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetFilterSlotMessage(
    int slotIndex, ItemStack stack) implements CustomPacketPayload {

  public static final Type<SetFilterSlotMessage> TYPE =
      new Type<>(RailcraftConstants.id("set_filter_slot"));

  public static final StreamCodec<RegistryFriendlyByteBuf, SetFilterSlotMessage> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.VAR_INT, SetFilterSlotMessage::slotIndex,
          ItemStack.STREAM_CODEC, SetFilterSlotMessage::stack,
          SetFilterSlotMessage::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SetFilterSlotMessage message, IPayloadContext context) {
    var player = context.player();
    if (!message.stack.isEmpty() && message.stack.getCount() <= message.stack.getMaxStackSize()) {
      var container = player.containerMenu;
      if (container != null && message.slotIndex >= 0 && message.slotIndex < container.slots.size()) {
        if (container.getSlot(message.slotIndex) instanceof RailcraftSlot slot && slot.isPhantom()) {
          if (slot.mayPlace(message.stack)) {
            slot.set(message.stack);
          }
        }
      }
    }
  }
}

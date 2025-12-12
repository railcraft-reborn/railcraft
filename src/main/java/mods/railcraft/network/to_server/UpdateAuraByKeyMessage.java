package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.GogglesItem;
import mods.railcraft.world.item.component.AuraComponent;
import mods.railcraft.world.item.component.RailcraftDataComponents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpdateAuraByKeyMessage(GogglesItem.Aura aura) implements CustomPacketPayload {

  public static final Type<UpdateAuraByKeyMessage> TYPE =
      new Type<>(RailcraftConstants.id("update_aura_by_key"));

  public static final StreamCodec<FriendlyByteBuf, UpdateAuraByKeyMessage> STREAM_CODEC =
      StreamCodec.composite(
          GogglesItem.Aura.STREAM_CODEC, UpdateAuraByKeyMessage::aura,
          UpdateAuraByKeyMessage::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(UpdateAuraByKeyMessage message, IPayloadContext context) {
    var player = context.player();
    var itemStack = player.getItemBySlot(EquipmentSlot.HEAD);
    if (itemStack.getItem() instanceof GogglesItem) {
      itemStack.set(RailcraftDataComponents.AURA, new AuraComponent(message.aura));
    }
  }
}

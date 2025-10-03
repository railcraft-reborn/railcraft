package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.GoldenTicketItem;
import mods.railcraft.world.item.TicketItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record EditTicketMessage(
    InteractionHand hand, String dest) implements CustomPacketPayload {

  public static final Type<EditTicketMessage> TYPE =
      new Type<>(RailcraftConstants.rl("edit_ticket"));

  public static final StreamCodec<FriendlyByteBuf, EditTicketMessage> STREAM_CODEC =
      StreamCodec.composite(
          NeoForgeStreamCodecs.enumCodec(InteractionHand.class), EditTicketMessage::hand,
          ByteBufCodecs.STRING_UTF8, EditTicketMessage::dest,
          EditTicketMessage::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(EditTicketMessage message, IPayloadContext context) {
    var player = context.player();
    var senderProfile = player.nameAndId();

    var itemStackToUpdate = player.getItemInHand(message.hand);
    if (itemStackToUpdate.getItem() instanceof GoldenTicketItem) {
      TicketItem.setTicketData(itemStackToUpdate, message.dest, senderProfile);
    }
  }
}

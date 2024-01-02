package mods.railcraft.network.to_server;

import mods.railcraft.Railcraft;
import mods.railcraft.world.item.GoldenTicketItem;
import mods.railcraft.world.item.TicketItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record EditTicketMessage(
    InteractionHand hand, String dest) implements CustomPacketPayload {

  public static final ResourceLocation ID = Railcraft.rl("edit_ticket");

  public static EditTicketMessage read(FriendlyByteBuf buf) {
    return new EditTicketMessage(buf.readEnum(InteractionHand.class), buf.readUtf());
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeEnum(this.hand);
    buf.writeUtf(dest);
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  public void handle(PlayPayloadContext context) {
    context.player().ifPresent(player -> {
      var senderProfile = player.getGameProfile();

      var itemStackToUpdate = player.getItemInHand(this.hand);
      if (itemStackToUpdate.getItem() instanceof GoldenTicketItem) {
        TicketItem.setTicketData(itemStackToUpdate, this.dest, senderProfile);
      }
    });
  }
}

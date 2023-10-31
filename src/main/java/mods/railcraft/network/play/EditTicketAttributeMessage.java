package mods.railcraft.network.play;

import mods.railcraft.world.item.GoldenTicketItem;
import mods.railcraft.world.item.TicketItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.NetworkEvent;

public record EditTicketAttributeMessage(InteractionHand hand, String dest) {

  public void encode(FriendlyByteBuf out) {
    out.writeEnum(this.hand);
    out.writeUtf(dest);
  }

  public static EditTicketAttributeMessage decode(FriendlyByteBuf in) {
    return new EditTicketAttributeMessage(in.readEnum(InteractionHand.class), in.readUtf());
  }

  public boolean handle(NetworkEvent.Context context) {
    var player = context.getSender();
    var senderProfile = player.getGameProfile();

    var itemStackToUpdate = player.getItemInHand(this.hand);
    if (itemStackToUpdate.getItem() instanceof GoldenTicketItem) {
      TicketItem.setTicketData(itemStackToUpdate, this.dest, senderProfile);
    }
    return true;
  }
}

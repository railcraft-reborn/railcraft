package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.client.gui.screen.GoldenTicketScreen;
import mods.railcraft.client.gui.screen.RoutingTableBookScreen;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public record OpenItemScreenMessage(InteractionHand hand, ItemStack itemStack) {

  public void encode(FriendlyByteBuf out) {
    out.writeEnum(this.hand);
    out.writeItem(this.itemStack);
  }

  public static OpenItemScreenMessage decode(FriendlyByteBuf in) {
    return new OpenItemScreenMessage(in.readEnum(InteractionHand.class), in.readItem());
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    processOnClient(this);
    return true;
  }

  private static void processOnClient(OpenItemScreenMessage message) {
    var minecraft = Minecraft.getInstance();
    var player = minecraft.player;
    var item = player.getItemInHand(message.hand);
    if (item.is(RailcraftItems.GOLDEN_TICKET.get())) {
      minecraft.setScreen(new GoldenTicketScreen(item, message.hand));
    } else if (item.is(RailcraftItems.ROUTING_TABLE_BOOK.get())) {
      minecraft.setScreen(new RoutingTableBookScreen(player, item, message.hand));
    }
  }
}

package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.client.gui.screen.inventory.GoldenTicketScreen;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public record OpenTicketMessage (InteractionHand hand, ItemStack itemStack) {

  public void encode(FriendlyByteBuf out) {
    out.writeEnum(this.hand);
    out.writeItem(this.itemStack);
  }

  public static OpenTicketMessage decode(FriendlyByteBuf in) {
    return new OpenTicketMessage(in.readEnum(InteractionHand.class), in.readItem());
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    processOnClient(this);
    return true;
  }

  private static void processOnClient(OpenTicketMessage message) {
    var minecraft = Minecraft.getInstance();
    var item = minecraft.player.getItemInHand(message.hand);
    if (item.is(RailcraftItems.GOLDEN_TICKET.get())) {
      minecraft.setScreen(new GoldenTicketScreen());
    }
  }
}

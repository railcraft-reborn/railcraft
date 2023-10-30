package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.world.inventory.RailcraftMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.NetworkEvent;

public record SyncWidgetMessage(int windowId, byte widgetId, FriendlyByteBuf data) {

  public void encode(FriendlyByteBuf out) {
    out.writeVarInt(this.windowId);
    out.writeByte(this.widgetId);
    out.writeVarInt(this.data.readableBytes());
    out.writeBytes(this.data);
  }

  public static SyncWidgetMessage decode(FriendlyByteBuf in) {
    return new SyncWidgetMessage(in.readVarInt(), in.readByte(),
        new FriendlyByteBuf(in.readBytes(in.readVarInt())));
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    processOnClient(this);
    return true;
  }

  private static void processOnClient(SyncWidgetMessage message) {
    var minecraft = Minecraft.getInstance();
    var menu = minecraft.player.containerMenu;
    if (menu instanceof RailcraftMenu railcraftMenu
        && menu.containerId == message.windowId) {
      railcraftMenu.getWidgets().get(message.widgetId).readFromBuf(message.data);
      message.data.release();
    }
  }
}

package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.world.inventory.RailcraftMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SyncWidgetMessage {

  private byte windowId;
  private byte widgetId;
  private FriendlyByteBuf payload;

  public SyncWidgetMessage() {}

  public SyncWidgetMessage(int windowId, int widgetId, FriendlyByteBuf data) {
    this.windowId = (byte) windowId;
    this.widgetId = (byte) widgetId;
    this.payload = data;
  }

  public void encode(FriendlyByteBuf data) {
    data.writeByte(this.windowId);
    data.writeByte(this.widgetId);
    data.writeVarInt(this.payload.readableBytes());
    data.writeBytes(payload);
  }

  public static SyncWidgetMessage decode(FriendlyByteBuf data) {
    return new SyncWidgetMessage(data.readByte(), data.readByte(),
        new FriendlyByteBuf(data.readBytes(data.readVarInt())));
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> processOnClient(this));
    return true;
  }

  private static void processOnClient(SyncWidgetMessage message) {
    var minecraft = Minecraft.getInstance();
    var menu = minecraft.player.containerMenu;
    if (menu instanceof RailcraftMenu railcraftMenu
        && menu.containerId == message.windowId) {
      railcraftMenu.getWidgets().get(message.widgetId).readServerSyncData(message.payload);
    }
  }
}

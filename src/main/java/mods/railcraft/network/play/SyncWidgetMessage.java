package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.world.inventory.RailcraftMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SyncWidgetMessage {

  private byte windowId;
  private byte widgetId;
  private PacketBuffer payload;

  public SyncWidgetMessage() {}

  public SyncWidgetMessage(int windowId, int widgetId, PacketBuffer data) {
    this.windowId = (byte) windowId;
    this.widgetId = (byte) widgetId;
    this.payload = data;
  }

  public void encode(PacketBuffer data) {
    data.writeByte(this.windowId);
    data.writeByte(this.widgetId);
    data.writeVarInt(this.payload.readableBytes());
    data.writeBytes(payload);
  }

  public static SyncWidgetMessage decode(PacketBuffer data) {
    return new SyncWidgetMessage(data.readByte(), data.readByte(),
        new PacketBuffer(data.readBytes(data.readVarInt())));
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> processOnClient(this));
    return true;
  }

  private static void processOnClient(SyncWidgetMessage message) {
    Minecraft mc = Minecraft.getInstance();
    Container menu = mc.player.containerMenu;
    if (menu instanceof RailcraftMenu
        && menu.containerId == message.windowId) {
      RailcraftMenu railcraftContainer = ((RailcraftMenu) menu);
      railcraftContainer.getWidgets().get(message.widgetId).readServerSyncData(message.payload);
    }
  }
}

package mods.railcraft.network.to_client;

import io.netty.buffer.Unpooled;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.network.RailcraftCustomPacketPayload;
import mods.railcraft.world.inventory.RailcraftMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record SyncWidgetMessage(
    int windowId, byte widgetId, byte[] rawUpdates) implements RailcraftCustomPacketPayload {

  public static final ResourceLocation ID = RailcraftConstants.rl("sync_widget");

  public static SyncWidgetMessage read(FriendlyByteBuf buf) {
    return new SyncWidgetMessage(buf.readVarInt(), buf.readByte(), buf.readByteArray());
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeVarInt(this.windowId);
    buf.writeByte(this.widgetId);
    buf.writeByteArray(this.rawUpdates);
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  @Override
  public void handle(PlayPayloadContext context) {
    context.player().ifPresent(player -> {
      var menu = player.containerMenu;
      if (menu instanceof RailcraftMenu railcraftMenu
          && menu.containerId == this.windowId) {
        var buff = new FriendlyByteBuf(Unpooled.wrappedBuffer(this.rawUpdates));
        railcraftMenu.getWidgets().get(this.widgetId).readFromBuf(buff);
        buff.release();
      }
    });
  }
}

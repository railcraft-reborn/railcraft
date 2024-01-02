package mods.railcraft.network.to_client;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.inventory.RailcraftMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record SyncWidgetMessage(
    int windowId, byte widgetId, FriendlyByteBuf data) implements CustomPacketPayload {

  public static final ResourceLocation ID = RailcraftConstants.rl("sync_widget");

  public static SyncWidgetMessage read(FriendlyByteBuf buf) {
    return new SyncWidgetMessage(buf.readVarInt(), buf.readByte(),
        new FriendlyByteBuf(buf.readBytes(buf.readVarInt())));
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeVarInt(this.windowId);
    buf.writeByte(this.widgetId);
    buf.writeVarInt(this.data.readableBytes());
    buf.writeBytes(this.data);
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  public void handle(PlayPayloadContext context) {
    context.player().ifPresent(player -> {
      var menu = player.containerMenu;
      if (menu instanceof RailcraftMenu railcraftMenu
          && menu.containerId == windowId) {
        railcraftMenu.getWidgets().get(widgetId).readFromBuf(data);
        data.release();
      }
    });
  }
}

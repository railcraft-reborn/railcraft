package mods.railcraft.network.to_client;

import io.netty.buffer.Unpooled;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.inventory.RailcraftMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.connection.ConnectionType;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncWidgetMessage(
    int windowId, byte widgetId, byte[] rawUpdates) implements CustomPacketPayload {

  public static final Type<SyncWidgetMessage> TYPE =
      new Type<>(RailcraftConstants.id("sync_widget"));

  public static final StreamCodec<FriendlyByteBuf, SyncWidgetMessage> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.VAR_INT, SyncWidgetMessage::windowId,
          ByteBufCodecs.BYTE, SyncWidgetMessage::widgetId,
          ByteBufCodecs.BYTE_ARRAY, SyncWidgetMessage::rawUpdates,
          SyncWidgetMessage::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SyncWidgetMessage message, IPayloadContext context) {
    var player = context.player();
    var menu = player.containerMenu;
    if (menu instanceof RailcraftMenu railcraftMenu
        && menu.containerId == message.windowId) {
      var buff = new RegistryFriendlyByteBuf(
          new FriendlyByteBuf(Unpooled.wrappedBuffer(message.rawUpdates)),
          player.registryAccess(), ConnectionType.OTHER);
      railcraftMenu.getWidgets().get(message.widgetId).readFromBuf(buff);
      buff.release();
    }
  }
}

package mods.railcraft.network;

import javax.annotation.Nullable;
import io.netty.buffer.Unpooled;
import mods.railcraft.api.core.Syncable;
import mods.railcraft.gui.widget.Widget;
import mods.railcraft.network.play.SyncEntityMessage;
import mods.railcraft.network.play.SyncWidgetMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public final class PacketBuilder {

  private static PacketBuilder instance;

  private PacketBuilder() {}

  public static PacketBuilder instance() {
    if (instance == null)
      instance = new PacketBuilder();
    return instance;
  }

  public void sendTileEntityPacket(BlockEntity tile) {
    if (tile.getLevel() instanceof ServerLevel) {
      ServerLevel world = (ServerLevel) tile.getLevel();
      Packet<ClientGamePacketListener> packet = tile.getUpdatePacket();
      if (packet != null) {
        PacketDispatcher.sendVanillaPacketToWatchers(packet, world, tile.getBlockPos());
      }
    }
  }

  public void sendTileEntityPacket(@Nullable BlockEntity tile, ServerPlayer player) {
    if (tile != null) {
      Packet<ClientGamePacketListener> packet = tile.getUpdatePacket();
      if (packet != null) {
        PacketDispatcher.sendToPlayer(packet, player);
      }
    }
  }

  // public void sendGuiReturnPacket(IGuiReturnHandler handler, byte[] extraData) {
  // PacketGuiReturn pkt = new PacketGuiReturn(handler, extraData);
  // PacketDispatcher.sendToServer(pkt);
  // }
  //
  // public void sendKeyPressPacket(EnumKeyBinding keyPress) {
  // PacketKeyPress pkt = new PacketKeyPress(keyPress);
  // PacketDispatcher.sendToServer(pkt);
  // }
  //
  // public void sendGuiIntegerPacket(IContainerListener listener, int windowId, int key, int value)
  // {
  // if (listener instanceof ServerPlayerEntity) {
  // PacketGuiInteger pkt = new PacketGuiInteger(windowId, key, value);
  // PacketDispatcher.sendToPlayer(pkt, (ServerPlayerEntity) listener);
  // }
  // }
  //

  //
  // public void sendGuiDataPacket(IContainerListener listener, int windowId, int key, byte[] value)
  // {
  // if (listener instanceof ServerPlayerEntity) {
  // PacketGuiData pkt = new PacketGuiData(windowId, key, value);
  // PacketDispatcher.sendToPlayer(pkt, (ServerPlayerEntity) listener);
  // }
  // }
  //
  public void sendGuiWidgetPacket(ServerPlayer player, int windowId, Widget widget) {
    if (widget.hasServerSyncData(player)) {
      FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
      widget.writeServerSyncData(player, byteBuf);
      SyncWidgetMessage pkt = new SyncWidgetMessage(windowId, widget.getId(), byteBuf);
      PacketDispatcher.sendToPlayer(pkt, player);
    }
  }
  //
  // public void sendGoldenTicketGuiPacket(ServerPlayerEntity player, EnumHand hand) {
  // PacketTicketGui pkt = new PacketTicketGui(hand);
  // PacketDispatcher.sendToPlayer(pkt, player);
  // }
  //
  // public void sendLogbookGuiPacket(ServerPlayerEntity player,
  // Multimap<LocalDate, GameProfile> log) {
  // PacketLogbook pkt = new PacketLogbook(log);
  // PacketDispatcher.sendToPlayer(pkt, player);
  // }
  //
  // public void stopRecord(EntityCartJukebox cart) {
  // if (!RailcraftConfig.playSounds())
  // return;
  // PacketStopRecord pkt = new PacketStopRecord(cart);
  // PacketDispatcher.sendToDimension(pkt, cart.world.provider.getDimension());
  // }

  public <T extends Entity & Syncable> void sendEntitySync(T entity) {
    SyncEntityMessage pkt = new SyncEntityMessage(entity);
    PacketDispatcher.sendToDimension(pkt, entity.level.dimension());
  }
}

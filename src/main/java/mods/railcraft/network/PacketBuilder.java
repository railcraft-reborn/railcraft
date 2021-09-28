package mods.railcraft.network;

import java.util.Collection;
import javax.annotation.Nullable;
import io.netty.buffer.Unpooled;
import mods.railcraft.api.core.Syncable;
import mods.railcraft.api.signals.SignalPacketBuilder;
import mods.railcraft.gui.widget.Widget;
import mods.railcraft.network.play.RequestNetworkPeersSyncMessage;
import mods.railcraft.network.play.SetMenuStringMessage;
import mods.railcraft.network.play.SyncEntityMessage;
import mods.railcraft.network.play.SyncWidgetMessage;
import mods.railcraft.network.play.SyncNetworkPeersMessage;
import mods.railcraft.world.signal.NetworkType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public final class PacketBuilder implements SignalPacketBuilder {

  private static PacketBuilder instance;

  private PacketBuilder() {}

  public static PacketBuilder instance() {
    if (instance == null)
      instance = new PacketBuilder();
    return instance;
  }

  public void sendTileEntityPacket(TileEntity tile) {
    if (tile.getLevel() instanceof ServerWorld) {
      ServerWorld world = (ServerWorld) tile.getLevel();
      SUpdateTileEntityPacket packet = tile.getUpdatePacket();
      if (packet != null)
        PacketDispatcher.sendVanillaPacketToWatchers(packet, world, tile.getBlockPos());
    }
  }

  public void sendTileEntityPacket(@Nullable TileEntity tile, ServerPlayerEntity player) {
    if (tile != null) {
      SUpdateTileEntityPacket packet = tile.getUpdatePacket();
      if (packet != null)
        PacketDispatcher.sendToPlayer(packet, player);
    }
  }

  @Override
  public void syncNetworkPeers(NetworkType type, BlockPos pos, Collection<BlockPos> peers,
      RegistryKey<World> dimension) {
    PacketDispatcher.sendToDimension(new SyncNetworkPeersMessage(pos, type, peers), dimension);
  }

  @Override
  public void requestNetworkPeersSync(NetworkType type, BlockPos pos) {
    PacketDispatcher.sendToServer(new RequestNetworkPeersSyncMessage(pos, type));
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
  public void sendGuiStringPacket(ServerPlayerEntity listener, int windowId, int key,
      String value) {
    SetMenuStringMessage pkt = new SetMenuStringMessage(windowId, key, value);
    PacketDispatcher.sendToPlayer(pkt, (ServerPlayerEntity) listener);
  }

  //
  // public void sendGuiDataPacket(IContainerListener listener, int windowId, int key, byte[] value)
  // {
  // if (listener instanceof ServerPlayerEntity) {
  // PacketGuiData pkt = new PacketGuiData(windowId, key, value);
  // PacketDispatcher.sendToPlayer(pkt, (ServerPlayerEntity) listener);
  // }
  // }
  //
  public void sendGuiWidgetPacket(ServerPlayerEntity player, int windowId, Widget widget) {
    if (widget.hasServerSyncData(player)) {
      PacketBuffer byteBuf = new PacketBuffer(Unpooled.buffer());
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

package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.network.PacketDispatcher;
import mods.railcraft.world.signal.NetworkType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class RequestNetworkPeersSyncMessage {

  private final BlockPos blockPos;
  private final NetworkType type;

  public RequestNetworkPeersSyncMessage(BlockPos blockPos, NetworkType type) {
    this.blockPos = blockPos;
    this.type = type;
  }

  public void encode(PacketBuffer out) {
    out.writeBlockPos(this.blockPos);
    out.writeEnum(this.type);
  }

  public static RequestNetworkPeersSyncMessage decode(PacketBuffer in) {
    return new RequestNetworkPeersSyncMessage(in.readBlockPos(), in.readEnum(NetworkType.class));
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    final ServerPlayerEntity sender = ctx.get().getSender();
    ctx.get().enqueueWork(() -> this.type.getNetwork(sender.level, this.blockPos)
        .map(network -> new SyncNetworkPeersMessage(this.blockPos, this.type, network.getPeers()))
        .ifPresent(pair -> PacketDispatcher.sendToPlayer(pair, sender)));
    return true;
  }
}

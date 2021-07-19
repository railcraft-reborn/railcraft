package mods.railcraft.network.play;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;
import mods.railcraft.world.signal.NetworkType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

public class UpdatePeersMessage {

  private final BlockPos blockPos;
  private final NetworkType type;
  private final Collection<BlockPos> peers;

  public UpdatePeersMessage(BlockPos blockPos, NetworkType type,
      Collection<BlockPos> peers) {
    this.blockPos = blockPos;
    this.type = type;
    this.peers = peers;
  }

  public void encode(PacketBuffer out) {
    out.writeBlockPos(this.blockPos);
    out.writeEnum(this.type);
    out.writeByte(this.peers.size());
    this.peers.forEach(out::writeBlockPos);
  }

  public static UpdatePeersMessage decode(PacketBuffer in) {
    BlockPos blockPos = in.readBlockPos();
    NetworkType type = in.readEnum(NetworkType.class);
    BlockPos[] peers = new BlockPos[in.readByte()];
    for (int i = 0; i < peers.length; i++) {
      peers[i] = in.readBlockPos();
    }
    return new UpdatePeersMessage(blockPos, type, Arrays.asList(peers));
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> LogicalSidedProvider.CLIENTWORLD
        .<Optional<World>>get(ctx.get().getDirection().getReceptionSide())
        .flatMap(level -> this.type.getNetwork(level, this.blockPos))
        .ifPresent(network -> {
          network.clear();
          this.peers.forEach(network::add);
        }));
    return true;
  }
}

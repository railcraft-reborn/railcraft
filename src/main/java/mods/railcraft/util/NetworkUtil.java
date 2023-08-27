package mods.railcraft.util;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.PacketDistributor;

public class NetworkUtil {

  @SuppressWarnings("deprecation")
  public static void sendToTrackingChunk(Packet<?> packet, ServerLevel level, BlockPos blockPos) {
    if (level.hasChunkAt(blockPos)) {
      PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(blockPos)).send(packet);
    }
  }
}

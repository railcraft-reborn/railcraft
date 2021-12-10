package mods.railcraft.network;

import mods.railcraft.api.core.DimensionPos;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;

/**
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public final class PacketDispatcher {

  public static void sendToServer(Object packet) {
    NetworkChannel.GAME.getSimpleChannel().sendToServer(packet);
  }

  public static void sendToPlayer(Object packet, ServerPlayer player) {
    NetworkChannel.GAME.getSimpleChannel().send(
        PacketDistributor.PLAYER.with(() -> player), packet);
  }

  public static void sendToPlayer(Packet<?> packet, ServerPlayer player) {
    player.connection.send(packet);
  }

  public static void sendToAll(Object packet) {
    NetworkChannel.GAME.getSimpleChannel().send(PacketDistributor.ALL.noArg(), packet);
  }

  public static PacketDistributor.TargetPoint targetPoint(DimensionPos point, double range) {
    return targetPoint(point.getDim(), point.getPos(), range);
  }

  public static PacketDistributor.TargetPoint targetPoint(ResourceKey<Level> dim, BlockPos pos,
      double radius) {
    return new PacketDistributor.TargetPoint(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
        radius, dim);
  }

  public static PacketDistributor.TargetPoint targetPoint(ResourceKey<Level> dim, Vec3 vec,
      double radius) {
    return new PacketDistributor.TargetPoint(vec.x, vec.y, vec.z, radius, dim);
  }

  public static PacketDistributor.TargetPoint targetPoint(ResourceKey<Level> dim, double x,
      double y, double z, double radius) {
    return new PacketDistributor.TargetPoint(x, y, z, radius, dim);
  }

  public static void sendToAllAround(Object packet, PacketDistributor.TargetPoint zone) {
    NetworkChannel.GAME.getSimpleChannel().send(PacketDistributor.NEAR.with(() -> zone), packet);
  }

  public static void sendToDimension(Object packet, ResourceKey<Level> dimensionId) {
    NetworkChannel.GAME.getSimpleChannel().send(
        PacketDistributor.DIMENSION.with(() -> dimensionId), packet);
  }

  public static void sendToWatchers(Object packet, ServerLevel world, BlockPos blockPos) {
    sendToWatchers(NetworkChannel.GAME.getSimpleChannel().toVanillaPacket(packet,
        NetworkDirection.PLAY_TO_CLIENT), world, blockPos);
  }

  @SuppressWarnings("deprecation")
  public static void sendVanillaPacketToWatchers(Packet<?> packet, ServerLevel world,
      BlockPos blockPos) {
    if (world.hasChunkAt(blockPos)) {
      PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunkAt(blockPos)).send(packet);
    }
  }
}

package mods.railcraft.network;

import mods.railcraft.api.core.DimensionPos;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.PacketDistributor.TargetPoint;

/**
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public final class PacketDispatcher {

  public static void sendToServer(Object packet) {
    NetworkChannel.PLAY.getSimpleChannel().sendToServer(packet);
  }

  public static void sendToPlayer(Object packet, ServerPlayerEntity player) {
    NetworkChannel.PLAY.getSimpleChannel().send(
        PacketDistributor.PLAYER.with(() -> player), packet);
  }

  public static void sendToPlayer(IPacket<?> packet, ServerPlayerEntity player) {
    player.connection.send(packet);
  }

  public static void sendToAll(Object packet) {
    NetworkChannel.PLAY.getSimpleChannel().send(PacketDistributor.ALL.noArg(), packet);
  }

  public static TargetPoint targetPoint(DimensionPos point, double range) {
    return targetPoint(point.getDim(), point.getPos(), range);
  }

  public static TargetPoint targetPoint(RegistryKey<World> dim, BlockPos pos, double radius) {
    return new TargetPoint(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, radius, dim);
  }

  public static TargetPoint targetPoint(RegistryKey<World> dim, Vector3d vec, double radius) {
    return new TargetPoint(vec.x, vec.y, vec.z, radius, dim);
  }

  public static TargetPoint targetPoint(RegistryKey<World> dim, double x, double y, double z,
      double radius) {
    return new TargetPoint(x, y, z, radius, dim);
  }

  public static void sendToAllAround(Object packet, TargetPoint zone) {
    NetworkChannel.PLAY.getSimpleChannel().send(PacketDistributor.NEAR.with(() -> zone), packet);
  }

  public static void sendToDimension(Object packet, RegistryKey<World> dimensionId) {
    NetworkChannel.PLAY.getSimpleChannel().send(
        PacketDistributor.DIMENSION.with(() -> dimensionId), packet);
  }

  public static void sendToWatchers(Object packet, ServerWorld world, BlockPos blockPos) {
    sendToWatchers(NetworkChannel.PLAY.getSimpleChannel().toVanillaPacket(packet,
        NetworkDirection.PLAY_TO_CLIENT), world, blockPos);
  }

  @SuppressWarnings("deprecation")
  public static void sendVanillaPacketToWatchers(IPacket<?> packet, ServerWorld world,
      BlockPos blockPos) {
    if (world.hasChunkAt(blockPos)) {
      PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunkAt(blockPos)).send(packet);
    }
  }
}

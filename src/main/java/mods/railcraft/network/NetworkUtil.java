
package mods.railcraft.network;

import mods.railcraft.api.core.DimensionPos;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public class NetworkUtil {

  public static Entity getEntityOrSender(NetworkEvent.Context context, int entityId) {
    return getEntityOrSender(context, entityId, Entity.class);
  }

  public static <T extends Entity> T getEntityOrSender(NetworkEvent.Context context, int entityId,
      Class<T> clazz) {
    return switch (context.getDirection().getReceptionSide()) {
      case CLIENT -> getEntity(context, entityId, clazz);
      case SERVER -> {
        if (clazz.isInstance(context.getSender())) {
          yield clazz.cast(context.getSender());
        } else {
          throw new IllegalStateException("Sender is not instance of: " + clazz.getName());
        }
      }
      default -> throw new IllegalStateException("Invalid side");
    };
  }

  public static Entity getEntity(NetworkEvent.Context context, int entityId) {
    return getEntity(context, entityId, Entity.class);
  }

  public static <T> T getEntity(NetworkEvent.Context context, int entityId,
      Class<T> clazz) {
    return LogicalSidedProvider.CLIENTWORLD.get(context.getDirection().getReceptionSide())
        .map(level -> level.getEntity(entityId))
        .filter(clazz::isInstance)
        .map(clazz::cast)
        .orElseThrow(() -> new IllegalStateException(
            "Entity with ID %s of type %s is absent from client level."
                .formatted(entityId, clazz.getName())));
  }

  public static void sendToWatchers(Object packet, ServerLevel level, BlockPos blockPos) {
    sendToWatchers(NetworkChannel.GAME.simpleChannel().toVanillaPacket(packet,
        NetworkDirection.PLAY_TO_CLIENT), level, blockPos);
  }

  public static PacketDistributor.TargetPoint targetPoint(DimensionPos point, double range) {
    return targetPoint(point.getDim(), point.getPos(), range);
  }

  public static PacketDistributor.TargetPoint targetPoint(
      ResourceKey<Level> dimensionKey, BlockPos pos, double radius) {
    return new PacketDistributor.TargetPoint(
        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, radius, dimensionKey);
  }

  public static PacketDistributor.TargetPoint targetPoint(
      ResourceKey<Level> dimensionKey, Vec3 vec, double radius) {
    return new PacketDistributor.TargetPoint(vec.x, vec.y, vec.z, radius, dimensionKey);
  }

  @SuppressWarnings("deprecation")
  public static void sendToTrackingChunk(Packet<?> packet, ServerLevel level, BlockPos blockPos) {
    if (level.hasChunkAt(blockPos)) {
      PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(blockPos)).send(packet);
    }
  }
}

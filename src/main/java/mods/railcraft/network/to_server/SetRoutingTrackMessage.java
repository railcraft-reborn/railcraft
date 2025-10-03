package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.entity.LockableSwitchTrackActuatorBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetRoutingTrackMessage(
    BlockPos blockPos,
    LockableSwitchTrackActuatorBlockEntity.Lock lock) implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<SetRoutingTrackMessage> TYPE =
      new Type<>(RailcraftConstants.rl("set_routing_track"));

  public static final StreamCodec<FriendlyByteBuf, SetRoutingTrackMessage> STREAM_CODEC =
      StreamCodec.composite(
          BlockPos.STREAM_CODEC, SetRoutingTrackMessage::blockPos,
          NeoForgeStreamCodecs.enumCodec(LockableSwitchTrackActuatorBlockEntity.Lock.class), SetRoutingTrackMessage::lock,
          SetRoutingTrackMessage::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SetRoutingTrackMessage message, IPayloadContext context) {
    var player = context.player();
    var level = player.level();
    var senderProfile = player.nameAndId();
    level.getBlockEntity(message.blockPos, RailcraftBlockEntityTypes.ROUTING_TRACK.get())
        .filter(routingTrack -> routingTrack.canAccess(senderProfile))
        .ifPresent(routingTrack -> {
          routingTrack.setLock(
              message.lock.equals(LockableSwitchTrackActuatorBlockEntity.Lock.UNLOCKED)
                  ? null
                  : senderProfile);
          routingTrack.syncToClient();
          routingTrack.setChanged();
        });
  }
}

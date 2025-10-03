package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.util.routing.RouterBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.SwitchTrackRouterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetRoutingDetectorMessage(
    BlockPos blockPos,
    RouterBlockEntity.Railway railway,
    SwitchTrackRouterBlockEntity.Lock lock) implements CustomPacketPayload {

  public static final Type<SetRoutingDetectorMessage> TYPE =
      new Type<>(RailcraftConstants.rl("set_routing_detector"));

  public static final StreamCodec<FriendlyByteBuf, SetRoutingDetectorMessage> STREAM_CODEC =
      StreamCodec.composite(
          BlockPos.STREAM_CODEC, SetRoutingDetectorMessage::blockPos,
          NeoForgeStreamCodecs.enumCodec(RouterBlockEntity.Railway.class), SetRoutingDetectorMessage::railway,
          NeoForgeStreamCodecs.enumCodec(SwitchTrackRouterBlockEntity.Lock.class), SetRoutingDetectorMessage::lock,
          SetRoutingDetectorMessage::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SetRoutingDetectorMessage message, IPayloadContext context) {
    var player = context.player();
    var level = player.level();
    var senderProfile = player.nameAndId();
    level.getBlockEntity(message.blockPos, RailcraftBlockEntityTypes.ROUTING_DETECTOR.get())
        .filter(routingDetector -> routingDetector.canAccess(senderProfile))
        .ifPresent(routingDetector -> {
          routingDetector.setLock(
              message.lock.equals(SwitchTrackRouterBlockEntity.Lock.UNLOCKED)
                  ? null
                  : senderProfile);
          routingDetector.setRailway(
              message.railway.equals(RouterBlockEntity.Railway.PUBLIC)
                  ? null
                  : senderProfile);
          routingDetector.syncToClient();
          routingDetector.setChanged();
        });
  }
}

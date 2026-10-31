package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetDelayedLockingTrackMessage(
    BlockPos blockPos, int delay) implements CustomPacketPayload {

  public static final Type<SetDelayedLockingTrackMessage> TYPE =
      new Type<>(RailcraftConstants.rl("set_delayed_locking_track"));

  public static final StreamCodec<FriendlyByteBuf, SetDelayedLockingTrackMessage> STREAM_CODEC =
      StreamCodec.composite(
          BlockPos.STREAM_CODEC, SetDelayedLockingTrackMessage::blockPos,
          ByteBufCodecs.VAR_INT, SetDelayedLockingTrackMessage::delay,
          SetDelayedLockingTrackMessage::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SetDelayedLockingTrackMessage message, IPayloadContext context) {
    var player = context.player();
    player.level()
        .getBlockEntity(message.blockPos, RailcraftBlockEntityTypes.DELAYED_LOCKING_TRACK.get())
        .filter(track -> RailcraftBlockEntity.isStillValid(track, player, 64))
        .ifPresent(track -> {
          track.setDelay(message.delay);
          track.setChanged();
          track.syncToClient();
        });
  }
}

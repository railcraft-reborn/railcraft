package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetLauncherTrackMessage(
    BlockPos blockPos, byte force) implements CustomPacketPayload {

  public static final Type<SetLauncherTrackMessage> TYPE =
      new Type<>(RailcraftConstants.id("set_launcher_track"));

  public static final StreamCodec<FriendlyByteBuf, SetLauncherTrackMessage> STREAM_CODEC =
      StreamCodec.composite(
          BlockPos.STREAM_CODEC, SetLauncherTrackMessage::blockPos,
          ByteBufCodecs.BYTE, SetLauncherTrackMessage::force,
          SetLauncherTrackMessage::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SetLauncherTrackMessage message, IPayloadContext context) {
    context.player().level()
        .getBlockEntity(message.blockPos, RailcraftBlockEntityTypes.LAUNCHER_TRACK.get())
        .ifPresent(track -> {
          track.setLaunchForce(message.force);
          track.setChanged();
        });
  }
}

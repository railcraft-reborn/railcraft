package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.track.outfitted.EmbarkingTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetEmbarkingTrackMessage(
    BlockPos blockPos, int radius) implements CustomPacketPayload {

  public static final Type<SetEmbarkingTrackMessage> TYPE =
      new Type<>(RailcraftConstants.id("set_embarking_track"));

  public static final StreamCodec<FriendlyByteBuf, SetEmbarkingTrackMessage> STREAM_CODEC =
      StreamCodec.composite(
          BlockPos.STREAM_CODEC, SetEmbarkingTrackMessage::blockPos,
          ByteBufCodecs.VAR_INT, SetEmbarkingTrackMessage::radius,
          SetEmbarkingTrackMessage::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SetEmbarkingTrackMessage message, IPayloadContext context) {
    var level = context.player().level();
    var blockState = level.getBlockState(message.blockPos);
    if (blockState.getBlock() instanceof EmbarkingTrackBlock) {
      level.setBlockAndUpdate(message.blockPos,
          EmbarkingTrackBlock.setRadius(blockState, message.radius));
    }
  }
}

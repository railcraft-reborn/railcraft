package mods.railcraft.network.to_server;

import io.netty.buffer.ByteBuf;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.track.MessengerTrackBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetMessengerTrackMessage(
    BlockPos blockPos, String title, String subtitle, String actionbar)
    implements CustomPacketPayload {

  public static final Type<SetMessengerTrackMessage> TYPE =
      new Type<>(RailcraftConstants.rl("set_messenger_track"));

  private static final StreamCodec<ByteBuf, String> TEXT_CODEC =
      ByteBufCodecs.stringUtf8(MessengerTrackBlockEntity.MAX_LENGTH);

  public static final StreamCodec<FriendlyByteBuf, SetMessengerTrackMessage> STREAM_CODEC =
      StreamCodec.composite(
          BlockPos.STREAM_CODEC, SetMessengerTrackMessage::blockPos,
          TEXT_CODEC, SetMessengerTrackMessage::title,
          TEXT_CODEC, SetMessengerTrackMessage::subtitle,
          TEXT_CODEC, SetMessengerTrackMessage::actionbar,
          SetMessengerTrackMessage::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SetMessengerTrackMessage message, IPayloadContext context) {
    var player = context.player();
    player.level()
        .getBlockEntity(message.blockPos, RailcraftBlockEntityTypes.MESSENGER_TRACK.get())
        .filter(track -> RailcraftBlockEntity.isStillValid(track, player, 64))
        .ifPresent(track -> {
          track.setMessages(message.title, message.subtitle, message.actionbar);
          track.setChanged();
          track.syncToClient();
        });
  }
}

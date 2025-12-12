package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetTrainDetectorMessage(
    BlockPos blockPos, int trainSize) implements CustomPacketPayload {

  public static final Type<SetTrainDetectorMessage> TYPE =
      new Type<>(RailcraftConstants.id("set_train_detector"));

  public static final StreamCodec<FriendlyByteBuf, SetTrainDetectorMessage> STREAM_CODEC =
      StreamCodec.composite(
          BlockPos.STREAM_CODEC, SetTrainDetectorMessage::blockPos,
          ByteBufCodecs.VAR_INT, SetTrainDetectorMessage::trainSize,
          SetTrainDetectorMessage::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SetTrainDetectorMessage message, IPayloadContext context) {
    context.player().level()
        .getBlockEntity(message.blockPos, RailcraftBlockEntityTypes.TRAIN_DETECTOR.get())
        .ifPresent(trainDetector -> {
          trainDetector.setTrainSize(message.trainSize);
          trainDetector.setChanged();
        });
  }
}

package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.detector.TankDetectorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetTankDetectorMessage(
    BlockPos blockPos,
    TankDetectorBlockEntity.Mode mode) implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<SetTankDetectorMessage> TYPE =
      new Type<>(RailcraftConstants.id("set_tank_detector"));

  public static final StreamCodec<FriendlyByteBuf, SetTankDetectorMessage> STREAM_CODEC =
      StreamCodec.composite(
          BlockPos.STREAM_CODEC, SetTankDetectorMessage::blockPos,
          NeoForgeStreamCodecs.enumCodec(TankDetectorBlockEntity.Mode.class), SetTankDetectorMessage::mode,
          SetTankDetectorMessage::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SetTankDetectorMessage message, IPayloadContext context) {
    context.player().level()
        .getBlockEntity(message.blockPos, RailcraftBlockEntityTypes.TANK_DETECTOR.get())
        .ifPresent(blockEntity -> {
          blockEntity.setMode(message.mode);
          blockEntity.setChanged();
        });
  }
}

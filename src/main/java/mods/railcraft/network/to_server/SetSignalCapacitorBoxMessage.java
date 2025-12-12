package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.signal.SignalCapacitorBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetSignalCapacitorBoxMessage(
    BlockPos blockPos, short ticksToPower,
    SignalCapacitorBoxBlockEntity.Mode mode) implements CustomPacketPayload {

  public static final Type<SetSignalCapacitorBoxMessage> TYPE =
      new Type<>(RailcraftConstants.id("set_signal_capacitor_box"));

  public static final StreamCodec<FriendlyByteBuf, SetSignalCapacitorBoxMessage> STREAM_CODEC =
      StreamCodec.composite(
          BlockPos.STREAM_CODEC, SetSignalCapacitorBoxMessage::blockPos,
          ByteBufCodecs.SHORT, SetSignalCapacitorBoxMessage::ticksToPower,
          NeoForgeStreamCodecs.enumCodec(SignalCapacitorBoxBlockEntity.Mode.class), SetSignalCapacitorBoxMessage::mode,
          SetSignalCapacitorBoxMessage::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SetSignalCapacitorBoxMessage message, IPayloadContext context) {
    context.player().level()
        .getBlockEntity(message.blockPos, RailcraftBlockEntityTypes.SIGNAL_CAPACITOR_BOX.get())
        .ifPresent(signalBox -> {
          signalBox.setTicksToPower(message.ticksToPower);
          signalBox.setMode(message.mode);
          signalBox.syncToClient();
          signalBox.setChanged();
        });
  }
}

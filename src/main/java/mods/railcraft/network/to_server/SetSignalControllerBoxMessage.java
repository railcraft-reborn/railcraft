package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetSignalControllerBoxMessage(
    BlockPos blockPos, SignalAspect defaultAspect,
    SignalAspect poweredAspect) implements CustomPacketPayload {

  public static final Type<SetSignalControllerBoxMessage> TYPE =
      new Type<>(RailcraftConstants.id("set_signal_controller_box"));

  public static final StreamCodec<FriendlyByteBuf, SetSignalControllerBoxMessage> STREAM_CODEC =
      StreamCodec.composite(
          BlockPos.STREAM_CODEC, SetSignalControllerBoxMessage::blockPos,
          NeoForgeStreamCodecs.enumCodec(SignalAspect.class), SetSignalControllerBoxMessage::defaultAspect,
          NeoForgeStreamCodecs.enumCodec(SignalAspect.class), SetSignalControllerBoxMessage::poweredAspect,
          SetSignalControllerBoxMessage::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SetSignalControllerBoxMessage message, IPayloadContext context) {
    context.player().level()
        .getBlockEntity(message.blockPos, RailcraftBlockEntityTypes.SIGNAL_CONTROLLER_BOX.get())
        .ifPresent(signalBox -> {
          signalBox.setDefaultAspect(message.defaultAspect);
          signalBox.setPoweredAspect(message.poweredAspect);
          signalBox.setChanged();
        });
  }
}

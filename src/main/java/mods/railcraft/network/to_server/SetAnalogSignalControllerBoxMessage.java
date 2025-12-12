package mods.railcraft.network.to_server;

import java.util.BitSet;
import java.util.Map;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetAnalogSignalControllerBoxMessage(
    BlockPos blockPos,
    Map<SignalAspect, BitSet> signalAspectTriggerSignals) implements CustomPacketPayload {

  public static final Type<SetAnalogSignalControllerBoxMessage> TYPE =
      new Type<>(RailcraftConstants.id("set_analog_signal_controller_box"));

  public static final StreamCodec<FriendlyByteBuf, SetAnalogSignalControllerBoxMessage> STREAM_CODEC =
      CustomPacketPayload.codec(SetAnalogSignalControllerBoxMessage::write, SetAnalogSignalControllerBoxMessage::read);

  private static SetAnalogSignalControllerBoxMessage read(FriendlyByteBuf buf) {
    var blockPos = buf.readBlockPos();
    var signalAspectTriggerSignals =
        buf.readMap(b -> b.readEnum(SignalAspect.class), FriendlyByteBuf::readBitSet);
    return new SetAnalogSignalControllerBoxMessage(blockPos, signalAspectTriggerSignals);
  }

  private void write(FriendlyByteBuf buf) {
    buf.writeBlockPos(this.blockPos);
    buf.writeMap(this.signalAspectTriggerSignals,
        FriendlyByteBuf::writeEnum, FriendlyByteBuf::writeBitSet);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SetAnalogSignalControllerBoxMessage message, IPayloadContext context) {
    context.player().level()
        .getBlockEntity(message.blockPos, RailcraftBlockEntityTypes.ANALOG_SIGNAL_CONTROLLER_BOX.get())
        .ifPresent(signalBox -> {
          signalBox.setSignalAspectTriggerSignals(message.signalAspectTriggerSignals);
          signalBox.setChanged();
        });
  }
}

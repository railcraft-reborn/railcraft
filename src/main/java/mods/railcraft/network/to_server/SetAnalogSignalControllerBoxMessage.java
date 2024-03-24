package mods.railcraft.network.to_server;

import java.util.BitSet;
import java.util.Map;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.network.RailcraftCustomPacketPayload;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record SetAnalogSignalControllerBoxMessage(
    BlockPos blockPos,
    Map<SignalAspect, BitSet> signalAspectTriggerSignals) implements RailcraftCustomPacketPayload {

  public static final ResourceLocation ID = RailcraftConstants.rl("set_analog_signal_controller_box");

  public static SetAnalogSignalControllerBoxMessage read(FriendlyByteBuf buf) {
    var blockPos = buf.readBlockPos();
    var signalAspectTriggerSignals =
        buf.readMap(b -> b.readEnum(SignalAspect.class), FriendlyByteBuf::readBitSet);
    return new SetAnalogSignalControllerBoxMessage(blockPos, signalAspectTriggerSignals);
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeBlockPos(this.blockPos);
    buf.writeMap(this.signalAspectTriggerSignals,
        FriendlyByteBuf::writeEnum, FriendlyByteBuf::writeBitSet);
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  @Override
  public void handle(PlayPayloadContext context) {
    context.level()
        .flatMap(level -> level.getBlockEntity(this.blockPos,
            RailcraftBlockEntityTypes.ANALOG_SIGNAL_CONTROLLER_BOX.get()))
        .ifPresent(signalBox -> {
          signalBox.setSignalAspectTriggerSignals(this.signalAspectTriggerSignals);
          signalBox.setChanged();
        });
  }
}

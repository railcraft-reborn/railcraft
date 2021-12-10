package mods.railcraft.network.play;

import java.util.BitSet;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.signal.AnalogSignalControllerBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SetAnalogSignalControllerBoxAttributesMessage {

  private final BlockPos blockPos;
  private final Map<SignalAspect, BitSet> signalAspectTriggerSignals;

  public SetAnalogSignalControllerBoxAttributesMessage(BlockPos blockPos,
      Map<SignalAspect, BitSet> signalAspectTriggerSignals) {
    this.blockPos = blockPos;
    this.signalAspectTriggerSignals = signalAspectTriggerSignals;
  }

  public void encode(FriendlyByteBuf out) {
    out.writeBlockPos(this.blockPos);
    out.writeVarInt(this.signalAspectTriggerSignals.size());
    for (var entry : this.signalAspectTriggerSignals.entrySet()) {
      out.writeEnum(entry.getKey());
      out.writeByteArray(entry.getValue().toByteArray());
    }
  }

  public static SetAnalogSignalControllerBoxAttributesMessage decode(FriendlyByteBuf in) {
    var blockPos = in.readBlockPos();
    var size = in.readVarInt();
    Map<SignalAspect, BitSet> signalAspectTriggerSignals = new EnumMap<>(SignalAspect.class);
    for (var i = 0; i < size; i++) {
      signalAspectTriggerSignals.put(in.readEnum(SignalAspect.class),
          BitSet.valueOf(in.readByteArray(2048)));
    }
    return new SetAnalogSignalControllerBoxAttributesMessage(blockPos,
        signalAspectTriggerSignals);
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    var level = context.get().getSender().getLevel();
    LevelUtil.getBlockEntity(level, this.blockPos, AnalogSignalControllerBoxBlockEntity.class)
        .ifPresent(
            signalBox -> signalBox.setSignalAspectTriggerSignals(this.signalAspectTriggerSignals));
    return true;
  }
}

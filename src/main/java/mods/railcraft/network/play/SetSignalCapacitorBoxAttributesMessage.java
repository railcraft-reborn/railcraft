package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.signal.SignalCapacitorBoxBlockEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

public class SetSignalCapacitorBoxAttributesMessage {

  private final BlockPos blockPos;
  private final short ticksToPower;
  private final SignalCapacitorBoxBlockEntity.Mode mode;

  public SetSignalCapacitorBoxAttributesMessage(BlockPos blockPos, short ticksToPower,
      SignalCapacitorBoxBlockEntity.Mode mode) {
    this.blockPos = blockPos;
    this.ticksToPower = ticksToPower;
    this.mode = mode;
  }

  public void encode(PacketBuffer out) {
    out.writeBlockPos(this.blockPos);
    out.writeShort(this.ticksToPower);
    out.writeEnum(this.mode);
  }

  public static SetSignalCapacitorBoxAttributesMessage decode(PacketBuffer in) {
    return new SetSignalCapacitorBoxAttributesMessage(in.readBlockPos(), in.readShort(),
        in.readEnum(SignalCapacitorBoxBlockEntity.Mode.class));
  }

  public void handle(Supplier<NetworkEvent.Context> context) {
    ServerWorld level = context.get().getSender().getLevel();
    LevelUtil.getBlockEntity(level, this.blockPos, SignalCapacitorBoxBlockEntity.class)
        .ifPresent(signalBox -> {
          signalBox.setTicksToPower(this.ticksToPower);
          signalBox.getModeButtonController().setCurrentState(this.mode);
        });
  }
}

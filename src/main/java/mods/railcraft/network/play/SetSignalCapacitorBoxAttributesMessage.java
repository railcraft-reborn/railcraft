package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.signal.SignalCapacitorBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

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

  public void encode(FriendlyByteBuf out) {
    out.writeBlockPos(this.blockPos);
    out.writeShort(this.ticksToPower);
    out.writeEnum(this.mode);
  }

  public static SetSignalCapacitorBoxAttributesMessage decode(FriendlyByteBuf in) {
    return new SetSignalCapacitorBoxAttributesMessage(in.readBlockPos(), in.readShort(),
        in.readEnum(SignalCapacitorBoxBlockEntity.Mode.class));
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    var level = context.get().getSender().getLevel();
    level.getBlockEntity(this.blockPos, RailcraftBlockEntityTypes.SIGNAL_CAPACITOR_BOX.get())
        .ifPresent(signalBox -> {
          signalBox.setTicksToPower(this.ticksToPower);
          signalBox.setMode(this.mode);
          signalBox.syncToClient();
        });
    return true;
  }
}

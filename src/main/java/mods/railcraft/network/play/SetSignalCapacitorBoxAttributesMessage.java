package mods.railcraft.network.play;

import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.signal.SignalCapacitorBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.NetworkEvent;

public record SetSignalCapacitorBoxAttributesMessage(BlockPos blockPos, short ticksToPower,
    SignalCapacitorBoxBlockEntity.Mode mode) {

  public void encode(FriendlyByteBuf out) {
    out.writeBlockPos(this.blockPos);
    out.writeShort(this.ticksToPower);
    out.writeEnum(this.mode);
  }

  public static SetSignalCapacitorBoxAttributesMessage decode(FriendlyByteBuf in) {
    return new SetSignalCapacitorBoxAttributesMessage(in.readBlockPos(), in.readShort(),
        in.readEnum(SignalCapacitorBoxBlockEntity.Mode.class));
  }

  public boolean handle(NetworkEvent.Context context) {
    var level = context.getSender().level();
    level.getBlockEntity(this.blockPos, RailcraftBlockEntityTypes.SIGNAL_CAPACITOR_BOX.get())
        .ifPresent(signalBox -> {
          signalBox.setTicksToPower(this.ticksToPower);
          signalBox.setMode(this.mode);
          signalBox.syncToClient();
          signalBox.setChanged();
        });
    return true;
  }
}

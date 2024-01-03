package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.network.RailcraftCustomPacketPayload;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.signal.SignalCapacitorBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record SetSignalCapacitorBoxMessage(
    BlockPos blockPos, short ticksToPower,
    SignalCapacitorBoxBlockEntity.Mode mode) implements RailcraftCustomPacketPayload {

  public static final ResourceLocation ID = RailcraftConstants.rl("set_signal_capacitor_box");

  public static SetSignalCapacitorBoxMessage read(FriendlyByteBuf buf) {
    return new SetSignalCapacitorBoxMessage(buf.readBlockPos(), buf.readShort(),
        buf.readEnum(SignalCapacitorBoxBlockEntity.Mode.class));
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeBlockPos(this.blockPos);
    buf.writeShort(this.ticksToPower);
    buf.writeEnum(this.mode);
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  @Override
  public void handle(PlayPayloadContext context) {
    context.level()
        .flatMap(level -> level
            .getBlockEntity(this.blockPos, RailcraftBlockEntityTypes.SIGNAL_CAPACITOR_BOX.get()))
        .ifPresent(signalBox -> {
          signalBox.setTicksToPower(this.ticksToPower);
          signalBox.setMode(this.mode);
          signalBox.syncToClient();
          signalBox.setChanged();
        });
  }
}

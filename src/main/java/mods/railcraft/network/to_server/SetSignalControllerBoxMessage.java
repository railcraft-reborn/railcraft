package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.network.RailcraftCustomPacketPayload;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record SetSignalControllerBoxMessage(
    BlockPos blockPos, SignalAspect defaultAspect,
    SignalAspect poweredAspect) implements RailcraftCustomPacketPayload {

  public static final ResourceLocation ID = RailcraftConstants.rl("set_signal_controller_box");

  public static SetSignalControllerBoxMessage read(FriendlyByteBuf buf) {
    return new SetSignalControllerBoxMessage(buf.readBlockPos(),
        buf.readEnum(SignalAspect.class), buf.readEnum(SignalAspect.class));
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeBlockPos(this.blockPos);
    buf.writeEnum(this.defaultAspect);
    buf.writeEnum(this.poweredAspect);
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  @Override
  public void handle(PlayPayloadContext context) {
    context.level()
        .flatMap(level -> level
            .getBlockEntity(this.blockPos, RailcraftBlockEntityTypes.SIGNAL_CONTROLLER_BOX.get()))
        .ifPresent(signalBox -> {
          signalBox.setDefaultAspect(this.defaultAspect);
          signalBox.setPoweredAspect(this.poweredAspect);
          signalBox.setChanged();
        });
  }
}

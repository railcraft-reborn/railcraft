package mods.railcraft.network.to_server;

import mods.railcraft.Railcraft;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record SetSignalControllerBoxMessage(BlockPos blockPos, SignalAspect defaultAspect,
                                            SignalAspect poweredAspect) implements
    CustomPacketPayload {

  public static final ResourceLocation ID = Railcraft.rl("set_signal_controller_box");

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

package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.NetworkEvent;

public record SetSignalControllerBoxAttributesMessage(BlockPos blockPos, SignalAspect defaultAspect,
    SignalAspect poweredAspect) {

  public void encode(FriendlyByteBuf out) {
    out.writeBlockPos(this.blockPos);
    out.writeEnum(this.defaultAspect);
    out.writeEnum(this.poweredAspect);
  }

  public static SetSignalControllerBoxAttributesMessage decode(FriendlyByteBuf in) {
    return new SetSignalControllerBoxAttributesMessage(in.readBlockPos(),
        in.readEnum(SignalAspect.class), in.readEnum(SignalAspect.class));
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    var level = context.get().getSender().level();
    level.getBlockEntity(this.blockPos, RailcraftBlockEntityTypes.SIGNAL_CONTROLLER_BOX.get())
        .ifPresent(signalBox -> {
          signalBox.setDefaultAspect(this.defaultAspect);
          signalBox.setPoweredAspect(this.poweredAspect);
          signalBox.setChanged();
        });
    return true;
  }
}

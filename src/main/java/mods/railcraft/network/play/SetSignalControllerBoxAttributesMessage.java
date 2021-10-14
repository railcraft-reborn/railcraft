package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.signal.SignalControllerBoxBlockEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

public class SetSignalControllerBoxAttributesMessage {

  private final BlockPos blockPos;
  private final SignalAspect defaultAspect;
  private final SignalAspect poweredAspect;

  public SetSignalControllerBoxAttributesMessage(BlockPos blockPos, SignalAspect defaultAspect,
      SignalAspect poweredAspect) {
    this.blockPos = blockPos;
    this.defaultAspect = defaultAspect;
    this.poweredAspect = poweredAspect;
  }

  public void encode(PacketBuffer out) {
    out.writeBlockPos(this.blockPos);
    out.writeEnum(this.defaultAspect);
    out.writeEnum(this.poweredAspect);
  }

  public static SetSignalControllerBoxAttributesMessage decode(PacketBuffer in) {
    return new SetSignalControllerBoxAttributesMessage(in.readBlockPos(),
        in.readEnum(SignalAspect.class), in.readEnum(SignalAspect.class));
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    ServerWorld level = context.get().getSender().getLevel();
    LevelUtil.getBlockEntity(level, this.blockPos, SignalControllerBoxBlockEntity.class)
        .ifPresent(signalBox -> {
          signalBox.setDefaultAspect(this.defaultAspect);
          signalBox.setPoweredAspect(this.poweredAspect);
        });
    return true;
  }
}

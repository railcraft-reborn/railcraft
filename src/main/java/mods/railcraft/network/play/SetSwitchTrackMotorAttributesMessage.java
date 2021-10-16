package mods.railcraft.network.play;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.Supplier;
import com.mojang.authlib.GameProfile;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.LockableSwitchTrackActuatorBlockEntity;
import mods.railcraft.world.level.block.entity.SwitchTrackMotorBlockEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

public class SetSwitchTrackMotorAttributesMessage {

  private final BlockPos blockPos;
  private final Set<SignalAspect> actionSignalAspects;
  private final boolean redstoneTriggered;
  private final LockableSwitchTrackActuatorBlockEntity.Lock lock;

  public SetSwitchTrackMotorAttributesMessage(BlockPos blockPos,
      Set<SignalAspect> actionSignalAspects, boolean redstoneTriggered,
      LockableSwitchTrackActuatorBlockEntity.Lock lock) {
    this.blockPos = blockPos;
    this.actionSignalAspects = actionSignalAspects;
    this.redstoneTriggered = redstoneTriggered;
    this.lock = lock;
  }

  public void encode(PacketBuffer out) {
    out.writeBlockPos(this.blockPos);
    out.writeVarInt(this.actionSignalAspects.size());
    this.actionSignalAspects.forEach(out::writeEnum);
    out.writeBoolean(this.redstoneTriggered);
    out.writeEnum(this.lock);
  }

  public static SetSwitchTrackMotorAttributesMessage decode(PacketBuffer in) {
    BlockPos blockPos = in.readBlockPos();
    int size = in.readVarInt();
    Set<SignalAspect> actionSignalAspects = EnumSet.noneOf(SignalAspect.class);
    for (int i = 0; i < size; i++) {
      actionSignalAspects.add(in.readEnum(SignalAspect.class));
    }
    boolean redstoneTriggered = in.readBoolean();
    LockableSwitchTrackActuatorBlockEntity.Lock lock =
        in.readEnum(LockableSwitchTrackActuatorBlockEntity.Lock.class);
    return new SetSwitchTrackMotorAttributesMessage(blockPos, actionSignalAspects,
        redstoneTriggered, lock);
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    ServerWorld level = context.get().getSender().getLevel();
    GameProfile senderProfile = context.get().getSender().getGameProfile();
    LevelUtil.getBlockEntity(level, this.blockPos, SwitchTrackMotorBlockEntity.class)
        .filter(signalBox -> signalBox.canAccess(senderProfile))
        .ifPresent(signalBox -> {
          signalBox.getActionSignalAspects().clear();
          signalBox.getActionSignalAspects().addAll(this.actionSignalAspects);
          signalBox.setRedstoneTriggered(this.redstoneTriggered);
          signalBox.setLock(this.lock);
          if (this.lock == LockableSwitchTrackActuatorBlockEntity.Lock.LOCKED) {
            signalBox.setOwner(senderProfile);
          } else {
            signalBox.setOwner(null);
          }
          signalBox.syncToClient();
        });
    return true;
  }
}

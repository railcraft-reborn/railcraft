package mods.railcraft.network.play;

import java.util.EnumSet;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.world.level.block.entity.LockableSwitchTrackActuatorBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.NetworkEvent;

public record SetSwitchTrackMotorAttributesMessage(BlockPos blockPos,
    EnumSet<SignalAspect> actionSignalAspects, boolean redstoneTriggered,
    LockableSwitchTrackActuatorBlockEntity.Lock lock) {

  public void encode(FriendlyByteBuf out) {
    out.writeBlockPos(this.blockPos);
    out.writeEnumSet(this.actionSignalAspects, SignalAspect.class);
    out.writeBoolean(this.redstoneTriggered);
    out.writeEnum(this.lock);
  }

  public static SetSwitchTrackMotorAttributesMessage decode(FriendlyByteBuf in) {
    var blockPos = in.readBlockPos();
    var actionSignalAspects = in.readEnumSet(SignalAspect.class);
    var redstoneTriggered = in.readBoolean();
    var lock = in.readEnum(LockableSwitchTrackActuatorBlockEntity.Lock.class);
    return new SetSwitchTrackMotorAttributesMessage(blockPos, actionSignalAspects,
        redstoneTriggered, lock);
  }

  public boolean handle(NetworkEvent.Context context) {
    var player = context.getSender();
    var level = player.level();
    var senderProfile = player.getGameProfile();
    level.getBlockEntity(this.blockPos, RailcraftBlockEntityTypes.SWITCH_TRACK_MOTOR.get())
        .filter(switchTrack -> switchTrack.canAccess(senderProfile))
        .ifPresent(switchTrack -> {
          switchTrack.getActionSignalAspects().clear();
          switchTrack.getActionSignalAspects().addAll(this.actionSignalAspects);
          switchTrack.setRedstoneTriggered(this.redstoneTriggered);
          switchTrack.setLock(
              this.lock.equals(LockableSwitchTrackActuatorBlockEntity.Lock.UNLOCKED)
                  ? null : senderProfile);
          switchTrack.syncToClient();
          switchTrack.setChanged();
        });
    return true;
  }
}

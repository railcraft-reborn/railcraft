package mods.railcraft.network.play;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.Supplier;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.world.level.block.entity.LockableSwitchTrackActuatorBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record SetSwitchTrackMotorAttributesMessage(BlockPos blockPos,
    Set<SignalAspect> actionSignalAspects, boolean redstoneTriggered,
    LockableSwitchTrackActuatorBlockEntity.Lock lock) {

  public void encode(FriendlyByteBuf out) {
    out.writeBlockPos(this.blockPos);
    out.writeVarInt(this.actionSignalAspects.size());
    this.actionSignalAspects.forEach(out::writeEnum);
    out.writeBoolean(this.redstoneTriggered);
    out.writeEnum(this.lock);
  }

  public static SetSwitchTrackMotorAttributesMessage decode(FriendlyByteBuf in) {
    var blockPos = in.readBlockPos();
    var size = in.readVarInt();
    var actionSignalAspects = EnumSet.noneOf(SignalAspect.class);
    for (int i = 0; i < size; i++) {
      actionSignalAspects.add(in.readEnum(SignalAspect.class));
    }
    var redstoneTriggered = in.readBoolean();
    var lock = in.readEnum(LockableSwitchTrackActuatorBlockEntity.Lock.class);
    return new SetSwitchTrackMotorAttributesMessage(blockPos, actionSignalAspects,
        redstoneTriggered, lock);
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    var player = context.get().getSender();
    var level = player.getLevel();
    var senderProfile = player.getGameProfile();
    level.getBlockEntity(this.blockPos, RailcraftBlockEntityTypes.SWITCH_TRACK_MOTOR.get())
        .filter(signalBox -> signalBox.canAccess(senderProfile))
        .ifPresent(signalBox -> {
          signalBox.getActionSignalAspects().clear();
          signalBox.getActionSignalAspects().addAll(this.actionSignalAspects);
          signalBox.setRedstoneTriggered(this.redstoneTriggered);
          signalBox.setLock(
              this.lock.equals(LockableSwitchTrackActuatorBlockEntity.Lock.UNLOCKED)
                  ? null : senderProfile);
          signalBox.syncToClient();
        });
    return true;
  }
}

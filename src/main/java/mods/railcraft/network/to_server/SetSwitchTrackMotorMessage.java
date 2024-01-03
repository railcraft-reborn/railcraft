package mods.railcraft.network.to_server;

import java.util.EnumSet;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.network.RailcraftCustomPacketPayload;
import mods.railcraft.world.level.block.entity.LockableSwitchTrackActuatorBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record SetSwitchTrackMotorMessage(
    BlockPos blockPos,
    EnumSet<SignalAspect> actionSignalAspects,
    boolean redstoneTriggered,
    LockableSwitchTrackActuatorBlockEntity.Lock lock) implements RailcraftCustomPacketPayload {

  public static final ResourceLocation ID = RailcraftConstants.rl("set_switch_track_motor");

  public static SetSwitchTrackMotorMessage read(FriendlyByteBuf buf) {
    var blockPos = buf.readBlockPos();
    var actionSignalAspects = buf.readEnumSet(SignalAspect.class);
    var redstoneTriggered = buf.readBoolean();
    var lock = buf.readEnum(LockableSwitchTrackActuatorBlockEntity.Lock.class);
    return new SetSwitchTrackMotorMessage(blockPos, actionSignalAspects,
        redstoneTriggered, lock);
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeBlockPos(this.blockPos);
    buf.writeEnumSet(this.actionSignalAspects, SignalAspect.class);
    buf.writeBoolean(this.redstoneTriggered);
    buf.writeEnum(this.lock);
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  @Override
  public void handle(PlayPayloadContext context) {
    context.player().ifPresent(player -> {
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
    });
  }
}

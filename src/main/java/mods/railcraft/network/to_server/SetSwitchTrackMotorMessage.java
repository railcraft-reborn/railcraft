package mods.railcraft.network.to_server;

import java.util.EnumSet;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.world.level.block.entity.LockableSwitchTrackActuatorBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetSwitchTrackMotorMessage(
    BlockPos blockPos,
    EnumSet<SignalAspect> actionSignalAspects,
    boolean redstoneTriggered,
    LockableSwitchTrackActuatorBlockEntity.Lock lock) implements CustomPacketPayload {

  public static final Type<SetSwitchTrackMotorMessage> TYPE =
      new Type<>(RailcraftConstants.id("set_switch_track_motor"));

  public static final StreamCodec<FriendlyByteBuf, SetSwitchTrackMotorMessage> STREAM_CODEC =
      CustomPacketPayload.codec(SetSwitchTrackMotorMessage::write, SetSwitchTrackMotorMessage::read);

  private static SetSwitchTrackMotorMessage read(FriendlyByteBuf buf) {
    var blockPos = buf.readBlockPos();
    var actionSignalAspects = buf.readEnumSet(SignalAspect.class);
    var redstoneTriggered = buf.readBoolean();
    var lock = buf.readEnum(LockableSwitchTrackActuatorBlockEntity.Lock.class);
    return new SetSwitchTrackMotorMessage(blockPos, actionSignalAspects,
        redstoneTriggered, lock);
  }

  private void write(FriendlyByteBuf buf) {
    buf.writeBlockPos(this.blockPos);
    buf.writeEnumSet(this.actionSignalAspects, SignalAspect.class);
    buf.writeBoolean(this.redstoneTriggered);
    buf.writeEnum(this.lock);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SetSwitchTrackMotorMessage message, IPayloadContext context) {
    var player = context.player();
    var level = player.level();
    var senderProfile = player.nameAndId();
    level.getBlockEntity(message.blockPos, RailcraftBlockEntityTypes.SWITCH_TRACK_MOTOR.get())
        .filter(switchTrack -> switchTrack.canAccess(senderProfile))
        .ifPresent(switchTrack -> {
          switchTrack.getActionSignalAspects().clear();
          switchTrack.getActionSignalAspects().addAll(message.actionSignalAspects);
          switchTrack.setRedstoneTriggered(message.redstoneTriggered);
          switchTrack.setLock(
              message.lock.equals(LockableSwitchTrackActuatorBlockEntity.Lock.UNLOCKED)
                  ? null : senderProfile);
          switchTrack.syncToClient();
          switchTrack.setChanged();
        });
  }
}

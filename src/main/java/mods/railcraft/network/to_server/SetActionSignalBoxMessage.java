package mods.railcraft.network.to_server;

import java.util.EnumSet;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.signal.ActionSignalBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.LockableSignalBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetActionSignalBoxMessage(
    BlockPos blockPos,
    EnumSet<SignalAspect> actionSignalAspects,
    LockableSignalBoxBlockEntity.Lock lock) implements CustomPacketPayload {

  public static final Type<SetActionSignalBoxMessage> TYPE =
      new Type<>(RailcraftConstants.id("set_action_signal_box"));

  public static final StreamCodec<FriendlyByteBuf, SetActionSignalBoxMessage> STREAM_CODEC =
      CustomPacketPayload.codec(SetActionSignalBoxMessage::write, SetActionSignalBoxMessage::read);

  private static SetActionSignalBoxMessage read(FriendlyByteBuf buf) {
    var blockPos = buf.readBlockPos();
    var actionSignalAspects = buf.readEnumSet(SignalAspect.class);
    var lock = buf.readEnum(LockableSignalBoxBlockEntity.Lock.class);
    return new SetActionSignalBoxMessage(blockPos, actionSignalAspects, lock);
  }

  private void write(FriendlyByteBuf buf) {
    buf.writeBlockPos(this.blockPos);
    buf.writeEnumSet(this.actionSignalAspects, SignalAspect.class);
    buf.writeEnum(this.lock);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SetActionSignalBoxMessage message, IPayloadContext context) {
    var player = context.player();
    var senderProfile = player.nameAndId();
    var level = player.level();
    LevelUtil.getBlockEntity(level, message.blockPos, ActionSignalBoxBlockEntity.class)
        .filter(signalBox -> signalBox.canAccess(senderProfile))
        .ifPresent(signalBox -> {
          signalBox.getActionSignalAspects().clear();
          signalBox.getActionSignalAspects().addAll(message.actionSignalAspects);
          signalBox.setLock(message.lock);
          signalBox.setOwner(message.lock == LockableSignalBoxBlockEntity.Lock.LOCKED
              ? senderProfile
              : null);
          signalBox.syncToClient();
          signalBox.setChanged();
        });
  }
}

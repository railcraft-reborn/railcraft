package mods.railcraft.network.to_server;

import java.util.EnumSet;
import mods.railcraft.Railcraft;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.signal.ActionSignalBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.LockableSignalBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record SetActionSignalBoxMessage(
    BlockPos blockPos,
    EnumSet<SignalAspect> actionSignalAspects,
    LockableSignalBoxBlockEntity.Lock lock) implements CustomPacketPayload {

  public static final ResourceLocation ID = Railcraft.rl("set_action_signal_box");

  public static SetActionSignalBoxMessage read(FriendlyByteBuf buf) {
    var blockPos = buf.readBlockPos();
    var actionSignalAspects = buf.readEnumSet(SignalAspect.class);
    var lock = buf.readEnum(LockableSignalBoxBlockEntity.Lock.class);
    return new SetActionSignalBoxMessage(blockPos, actionSignalAspects, lock);
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeBlockPos(this.blockPos);
    buf.writeEnumSet(this.actionSignalAspects, SignalAspect.class);
    buf.writeEnum(this.lock);
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  public void handle(PlayPayloadContext context) {
    context.player().ifPresent(player -> {
      var senderProfile = player.getGameProfile();
      var level = player.level();
      LevelUtil.getBlockEntity(level, this.blockPos, ActionSignalBoxBlockEntity.class)
          .filter(signalBox -> signalBox.canAccess(senderProfile))
          .ifPresent(signalBox -> {
            signalBox.getActionSignalAspects().clear();
            signalBox.getActionSignalAspects().addAll(this.actionSignalAspects);
            signalBox.setLock(this.lock);
            signalBox.setOwner(this.lock == LockableSignalBoxBlockEntity.Lock.LOCKED
                ? senderProfile
                : null);
            signalBox.syncToClient();
            signalBox.setChanged();
          });
    });
  }
}

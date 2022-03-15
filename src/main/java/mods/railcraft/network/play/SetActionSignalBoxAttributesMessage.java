package mods.railcraft.network.play;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.Supplier;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.signal.ActionSignalBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.LockableSignalBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record SetActionSignalBoxAttributesMessage(BlockPos blockPos,
    Set<SignalAspect> actionSignalAspects, LockableSignalBoxBlockEntity.Lock lock) {

  public void encode(FriendlyByteBuf out) {
    out.writeBlockPos(this.blockPos);
    out.writeVarInt(this.actionSignalAspects.size());
    this.actionSignalAspects.forEach(out::writeEnum);
    out.writeEnum(this.lock);
  }

  public static SetActionSignalBoxAttributesMessage decode(FriendlyByteBuf in) {
    var blockPos = in.readBlockPos();
    var size = in.readVarInt();
    var actionSignalAspects = EnumSet.noneOf(SignalAspect.class);
    for (int i = 0; i < size; i++) {
      actionSignalAspects.add(in.readEnum(SignalAspect.class));
    }
    var lock = in.readEnum(LockableSignalBoxBlockEntity.Lock.class);
    return new SetActionSignalBoxAttributesMessage(blockPos, actionSignalAspects, lock);
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    var level = context.get().getSender().getLevel();
    var senderProfile = context.get().getSender().getGameProfile();
    LevelUtil.getBlockEntity(level, this.blockPos, ActionSignalBoxBlockEntity.class)
        .filter(signalBox -> signalBox.canAccess(senderProfile))
        .ifPresent(signalBox -> {
          signalBox.getActionSignalAspects().clear();
          signalBox.getActionSignalAspects().addAll(this.actionSignalAspects);
          signalBox.setLock(this.lock);
          if (this.lock == LockableSignalBoxBlockEntity.Lock.LOCKED) {
            signalBox.setOwner(senderProfile);
          } else {
            signalBox.setOwner(null);
          }
          signalBox.syncToClient();
        });
    return true;
  }
}

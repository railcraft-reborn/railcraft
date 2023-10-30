package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.world.level.block.entity.LockableSwitchTrackActuatorBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.NetworkEvent;

public record SetRoutingTrackAttributesMessage(BlockPos blockPos,
                                               LockableSwitchTrackActuatorBlockEntity.Lock lock) {

  public static SetRoutingTrackAttributesMessage decode(FriendlyByteBuf in) {
    var blockPos = in.readBlockPos();
    var lock = in.readEnum(LockableSwitchTrackActuatorBlockEntity.Lock.class);
    return new SetRoutingTrackAttributesMessage(blockPos, lock);
  }

  public void encode(FriendlyByteBuf out) {
    out.writeBlockPos(this.blockPos);
    out.writeEnum(this.lock);
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    var player = context.get().getSender();
    var level = player.level();
    var senderProfile = player.getGameProfile();
    level.getBlockEntity(this.blockPos, RailcraftBlockEntityTypes.ROUTING_TRACK.get())
        .filter(routingTrack -> routingTrack.canAccess(senderProfile))
        .ifPresent(routingTrack -> {
          routingTrack.setLock(
              this.lock.equals(LockableSwitchTrackActuatorBlockEntity.Lock.UNLOCKED)
                  ? null
                  : senderProfile);
          routingTrack.syncToClient();
          routingTrack.setChanged();
        });
    return true;
  }
}

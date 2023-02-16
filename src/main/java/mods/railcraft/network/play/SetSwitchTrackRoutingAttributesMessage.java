package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.SwitchTrackRoutingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record SetSwitchTrackRoutingAttributesMessage(BlockPos blockPos,
                                                     SwitchTrackRoutingBlockEntity.Railway railway,
                                                     SwitchTrackRoutingBlockEntity.Lock lock) {

  public static SetSwitchTrackRoutingAttributesMessage decode(FriendlyByteBuf in) {
    var blockPos = in.readBlockPos();
    var railway = in.readEnum(SwitchTrackRoutingBlockEntity.Railway.class);
    var lock = in.readEnum(SwitchTrackRoutingBlockEntity.Lock.class);
    return new SetSwitchTrackRoutingAttributesMessage(blockPos, railway, lock);
  }

  public void encode(FriendlyByteBuf out) {
    out.writeBlockPos(this.blockPos);
    out.writeEnum(this.railway);
    out.writeEnum(this.lock);
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    var player = context.get().getSender();
    var level = player.getLevel();
    var senderProfile = player.getGameProfile();
    level.getBlockEntity(this.blockPos, RailcraftBlockEntityTypes.SWITCH_TRACK_ROUTING.get())
        .filter(switchTrackRouting -> switchTrackRouting.canAccess(senderProfile))
        .ifPresent(switchTrackRouting -> {
          switchTrackRouting.setLock(
              this.lock.equals(SwitchTrackRoutingBlockEntity.Lock.UNLOCKED)
                  ? null : senderProfile);
          switchTrackRouting.setRailway(
              this.railway.equals(SwitchTrackRoutingBlockEntity.Railway.PUBLIC)
                  ? null : senderProfile);
          switchTrackRouting.syncToClient();
          switchTrackRouting.setChanged();
        });
    return true;
  }
}

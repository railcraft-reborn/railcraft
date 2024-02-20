package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.util.routing.RouterBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.SwitchTrackRouterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record SetSwitchTrackRouterAttributesMessage(
    BlockPos blockPos,
    RouterBlockEntity.Railway railway,
    SwitchTrackRouterBlockEntity.Lock lock) {

  public static SetSwitchTrackRouterAttributesMessage decode(FriendlyByteBuf in) {
    var blockPos = in.readBlockPos();
    var railway = in.readEnum(RouterBlockEntity.Railway.class);
    var lock = in.readEnum(SwitchTrackRouterBlockEntity.Lock.class);
    return new SetSwitchTrackRouterAttributesMessage(blockPos, railway, lock);
  }

  public void encode(FriendlyByteBuf out) {
    out.writeBlockPos(this.blockPos);
    out.writeEnum(this.railway);
    out.writeEnum(this.lock);
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    var player = context.get().getSender();
    var level = player.level();
    var senderProfile = player.getGameProfile();
    level.getBlockEntity(this.blockPos, RailcraftBlockEntityTypes.SWITCH_TRACK_ROUTER.get())
        .filter(switchTrackRouter -> switchTrackRouter.canAccess(senderProfile))
        .ifPresent(switchTrackRouter -> {
          switchTrackRouter.setLock(
              this.lock.equals(SwitchTrackRouterBlockEntity.Lock.UNLOCKED)
                  ? null
                  : senderProfile);
          switchTrackRouter.setRailway(
              this.railway.equals(RouterBlockEntity.Railway.PUBLIC)
                  ? null
                  : senderProfile);
          switchTrackRouter.syncToClient();
          switchTrackRouter.setChanged();
        });
    return true;
  }
}

package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.SwitchTrackRouterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record SetRoutingDetectorAttributesMessage(
    BlockPos blockPos,
    SwitchTrackRouterBlockEntity.Railway railway,
    SwitchTrackRouterBlockEntity.Lock lock) {

  public static SetRoutingDetectorAttributesMessage decode(FriendlyByteBuf in) {
    var blockPos = in.readBlockPos();
    var railway = in.readEnum(SwitchTrackRouterBlockEntity.Railway.class);
    var lock = in.readEnum(SwitchTrackRouterBlockEntity.Lock.class);
    return new SetRoutingDetectorAttributesMessage(blockPos, railway, lock);
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
    level.getBlockEntity(this.blockPos, RailcraftBlockEntityTypes.ROUTING_DETECTOR.get())
        .filter(routingDetector -> routingDetector.canAccess(senderProfile))
        .ifPresent(routingDetector -> {
          routingDetector.setLock(
              this.lock.equals(SwitchTrackRouterBlockEntity.Lock.UNLOCKED)
                  ? null
                  : senderProfile);
          routingDetector.setRailway(
              this.railway.equals(SwitchTrackRouterBlockEntity.Railway.PUBLIC)
                  ? null
                  : senderProfile);
          routingDetector.syncToClient();
          routingDetector.setChanged();
        });
    return true;
  }
}

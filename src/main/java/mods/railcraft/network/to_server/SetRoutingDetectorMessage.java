package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.network.RailcraftCustomPacketPayload;
import mods.railcraft.util.routing.RouterBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.SwitchTrackRouterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record SetRoutingDetectorMessage(
    BlockPos blockPos,
    RouterBlockEntity.Railway railway,
    SwitchTrackRouterBlockEntity.Lock lock) implements RailcraftCustomPacketPayload {

  public static final ResourceLocation ID = RailcraftConstants.rl("set_routing_detector");

  public static SetRoutingDetectorMessage read(FriendlyByteBuf in) {
    var blockPos = in.readBlockPos();
    var railway = in.readEnum(RouterBlockEntity.Railway.class);
    var lock = in.readEnum(SwitchTrackRouterBlockEntity.Lock.class);
    return new SetRoutingDetectorMessage(blockPos, railway, lock);
  }

  @Override
  public void write(FriendlyByteBuf out) {
    out.writeBlockPos(this.blockPos);
    out.writeEnum(this.railway);
    out.writeEnum(this.lock);
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
      level.getBlockEntity(this.blockPos, RailcraftBlockEntityTypes.ROUTING_DETECTOR.get())
          .filter(routingDetector -> routingDetector.canAccess(senderProfile))
          .ifPresent(routingDetector -> {
            routingDetector.setLock(
                this.lock.equals(SwitchTrackRouterBlockEntity.Lock.UNLOCKED)
                    ? null
                    : senderProfile);
            routingDetector.setRailway(
                this.railway.equals(RouterBlockEntity.Railway.PUBLIC)
                    ? null
                    : senderProfile);
            routingDetector.syncToClient();
            routingDetector.setChanged();
          });
    });
  }
}

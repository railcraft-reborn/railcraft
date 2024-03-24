package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.network.RailcraftCustomPacketPayload;
import mods.railcraft.world.level.block.entity.LockableSwitchTrackActuatorBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record SetRoutingTrackMessage(
    BlockPos blockPos,
    LockableSwitchTrackActuatorBlockEntity.Lock lock) implements RailcraftCustomPacketPayload {

  public static final ResourceLocation ID = RailcraftConstants.rl("set_routing_track");

  public static SetRoutingTrackMessage read(FriendlyByteBuf buf) {
    var blockPos = buf.readBlockPos();
    var lock = buf.readEnum(LockableSwitchTrackActuatorBlockEntity.Lock.class);
    return new SetRoutingTrackMessage(blockPos, lock);
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeBlockPos(this.blockPos);
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
    });
  }
}

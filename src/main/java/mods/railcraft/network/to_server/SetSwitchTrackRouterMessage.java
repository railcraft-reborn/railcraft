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

public record SetSwitchTrackRouterMessage(
    BlockPos blockPos,
    RouterBlockEntity.Railway railway,
    SwitchTrackRouterBlockEntity.Lock lock) implements RailcraftCustomPacketPayload {

  public static final ResourceLocation ID = RailcraftConstants.rl("set_switch_track_router");

  public static SetSwitchTrackRouterMessage read(FriendlyByteBuf buf) {
    var blockPos = buf.readBlockPos();
    var railway = buf.readEnum(RouterBlockEntity.Railway.class);
    var lock = buf.readEnum(SwitchTrackRouterBlockEntity.Lock.class);
    return new SetSwitchTrackRouterMessage(blockPos, railway, lock);
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeBlockPos(this.blockPos);
    buf.writeEnum(this.railway);
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
    });
  }
}

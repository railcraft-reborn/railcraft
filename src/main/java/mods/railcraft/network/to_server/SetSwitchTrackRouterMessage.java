package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.util.routing.RouterBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.SwitchTrackRouterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetSwitchTrackRouterMessage(
    BlockPos blockPos,
    RouterBlockEntity.Railway railway,
    SwitchTrackRouterBlockEntity.Lock lock) implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<SetSwitchTrackRouterMessage> TYPE =
      new Type<>(RailcraftConstants.id("set_switch_track_router"));

  public static final StreamCodec<FriendlyByteBuf, SetSwitchTrackRouterMessage> STREAM_CODEC =
      StreamCodec.composite(
          BlockPos.STREAM_CODEC, SetSwitchTrackRouterMessage::blockPos,
          NeoForgeStreamCodecs.enumCodec(RouterBlockEntity.Railway.class), SetSwitchTrackRouterMessage::railway,
          NeoForgeStreamCodecs.enumCodec(SwitchTrackRouterBlockEntity.Lock.class), SetSwitchTrackRouterMessage::lock,
          SetSwitchTrackRouterMessage::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SetSwitchTrackRouterMessage message, IPayloadContext context) {
    var player = context.player();
    var level = player.level();
    var senderProfile = player.nameAndId();
    level.getBlockEntity(message.blockPos, RailcraftBlockEntityTypes.SWITCH_TRACK_ROUTER.get())
        .filter(switchTrackRouter -> switchTrackRouter.canAccess(senderProfile))
        .ifPresent(switchTrackRouter -> {
          switchTrackRouter.setLock(
              message.lock.equals(SwitchTrackRouterBlockEntity.Lock.UNLOCKED)
                  ? null
                  : senderProfile);
          switchTrackRouter.setRailway(
              message.railway.equals(RouterBlockEntity.Railway.PUBLIC)
                  ? null
                  : senderProfile);
          switchTrackRouter.syncToClient();
          switchTrackRouter.setChanged();
        });
  }
}

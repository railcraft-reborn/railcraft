package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetLocomotiveMessage(
    int entityId, Locomotive.Mode mode,
    Locomotive.Speed speed, Locomotive.Lock lock,
    boolean reverse) implements CustomPacketPayload {

  public static final Type<SetLocomotiveMessage> TYPE =
      new Type<>(RailcraftConstants.rl("set_locomotive"));

  public static final StreamCodec<FriendlyByteBuf, SetLocomotiveMessage> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.VAR_INT, SetLocomotiveMessage::entityId,
          NeoForgeStreamCodecs.enumCodec(Locomotive.Mode.class), SetLocomotiveMessage::mode,
          NeoForgeStreamCodecs.enumCodec(Locomotive.Speed.class), SetLocomotiveMessage::speed,
          NeoForgeStreamCodecs.enumCodec(Locomotive.Lock.class), SetLocomotiveMessage::lock,
          ByteBufCodecs.BOOL, SetLocomotiveMessage::reverse,
          SetLocomotiveMessage::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SetLocomotiveMessage message, IPayloadContext context) {
    var player = context.player();
    var entity = player.level().getEntity(message.entityId);
    if (entity instanceof Locomotive locomotive && locomotive.canControl(player)) {
      Locomotive.applyAction(player, locomotive, false, loco -> {
        loco.setMode(message.mode);
        loco.setSpeed(message.speed);
        loco.setReverse(message.reverse);
        if (!loco.isLocked() || loco.getOwnerOrThrow().equals(player.nameAndId())) {
          loco.setLock(message.lock);
          loco.setOwner(message.lock == Locomotive.Lock.UNLOCKED
              ? null
              : player.nameAndId());
        }
      });
    }
  }
}

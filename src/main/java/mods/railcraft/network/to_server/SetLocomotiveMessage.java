package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record SetLocomotiveMessage(
    int entityId, Locomotive.Mode mode,
    Locomotive.Speed speed, Locomotive.Lock lock,
    boolean reverse) implements CustomPacketPayload {

  public static final ResourceLocation ID = RailcraftConstants.rl("set_locomotive");

  public static SetLocomotiveMessage read(FriendlyByteBuf buf) {
    return new SetLocomotiveMessage(buf.readVarInt(),
        buf.readEnum(Locomotive.Mode.class),
        buf.readEnum(Locomotive.Speed.class),
        buf.readEnum(Locomotive.Lock.class), buf.readBoolean());
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeVarInt(this.entityId);
    buf.writeEnum(this.mode);
    buf.writeEnum(this.speed);
    buf.writeEnum(this.lock);
    buf.writeBoolean(this.reverse);
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  public void handle(PlayPayloadContext context) {
    context.player().ifPresent(player -> {
      var entity = player.level().getEntity(this.entityId);
      if (entity instanceof Locomotive locomotive && locomotive.canControl(player)) {
        locomotive.applyAction(player, false, loco -> {
          loco.setMode(this.mode);
          loco.setSpeed(this.speed);
          loco.setReverse(this.reverse);
          if (!loco.isLocked() || loco.getOwnerOrThrow().equals(player.getGameProfile())) {
            loco.setLock(this.lock);
            loco.setOwner(this.lock == Locomotive.Lock.UNLOCKED
                ? null
                : player.getGameProfile());
          }
        });
      }
    });
  }
}

package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record SetLocomotiveAttributesMessage(int entityId, Locomotive.Mode mode,
    Locomotive.Speed speed, Locomotive.Lock lock, boolean reverse) {

  public void encode(FriendlyByteBuf out) {
    out.writeVarInt(this.entityId);
    out.writeEnum(this.mode);
    out.writeEnum(this.speed);
    out.writeEnum(this.lock);
    out.writeBoolean(this.reverse);
  }

  public static SetLocomotiveAttributesMessage decode(FriendlyByteBuf in) {
    return new SetLocomotiveAttributesMessage(in.readVarInt(),
        in.readEnum(Locomotive.Mode.class),
        in.readEnum(Locomotive.Speed.class),
        in.readEnum(Locomotive.Lock.class), in.readBoolean());
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    var player = ctx.get().getSender();
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
        loco.setChanged();
      });
    }
    return true;
  }
}

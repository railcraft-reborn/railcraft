package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.world.entity.cart.locomotive.LocomotiveEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SetLocomotiveAttributesMessage {

  private final int entityId;
  private final LocomotiveEntity.Mode mode;
  private final LocomotiveEntity.Speed speed;
  private final LocomotiveEntity.Lock lock;
  private final boolean reverse;

  public SetLocomotiveAttributesMessage(int entityId, LocomotiveEntity.Mode mode,
      LocomotiveEntity.Speed speed, LocomotiveEntity.Lock lock, boolean reverse) {
    this.entityId = entityId;
    this.mode = mode;
    this.speed = speed;
    this.lock = lock;
    this.reverse = reverse;
  }

  public void encode(FriendlyByteBuf out) {
    out.writeVarInt(this.entityId);
    out.writeEnum(this.mode);
    out.writeEnum(this.speed);
    out.writeEnum(this.lock);
    out.writeBoolean(this.reverse);
  }

  public static SetLocomotiveAttributesMessage decode(FriendlyByteBuf in) {
    return new SetLocomotiveAttributesMessage(in.readVarInt(),
        in.readEnum(LocomotiveEntity.Mode.class),
        in.readEnum(LocomotiveEntity.Speed.class),
        in.readEnum(LocomotiveEntity.Lock.class), in.readBoolean());
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      var player = ctx.get().getSender();
      var entity = player.level.getEntity(this.entityId);
      if (entity instanceof LocomotiveEntity loco && loco.canControl(player)) {
        loco.setMode(this.mode);
        loco.setSpeed(this.speed);
        if (!loco.isLocked() || loco.getOwnerOrThrow().equals(player.getGameProfile())) {
          loco.setLock(this.lock);
          if (this.lock == LocomotiveEntity.Lock.UNLOCKED) {
            loco.setOwner(null);
          } else {
            loco.setOwner(player.getGameProfile());
          }
        }
        loco.setReverse(this.reverse);
      }
    });
    return true;
  }
}

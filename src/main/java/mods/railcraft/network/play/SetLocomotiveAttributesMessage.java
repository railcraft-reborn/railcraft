package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.world.entity.LocomotiveEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

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

  public void encode(PacketBuffer out) {
    out.writeVarInt(this.entityId);
    out.writeEnum(this.mode);
    out.writeEnum(this.speed);
    out.writeEnum(this.lock);
    out.writeBoolean(this.reverse);
  }

  public static SetLocomotiveAttributesMessage decode(PacketBuffer in) {
    return new SetLocomotiveAttributesMessage(in.readVarInt(),
        in.readEnum(LocomotiveEntity.Mode.class),
        in.readEnum(LocomotiveEntity.Speed.class),
        in.readEnum(LocomotiveEntity.Lock.class), in.readBoolean());
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      ServerPlayerEntity player = ctx.get().getSender();
      Entity entity = player.level.getEntity(this.entityId);
      if (entity instanceof LocomotiveEntity) {
        LocomotiveEntity loco = (LocomotiveEntity) entity;
        if (loco.canControl(player.getGameProfile())) {
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
      }
    });
    return true;
  }
}

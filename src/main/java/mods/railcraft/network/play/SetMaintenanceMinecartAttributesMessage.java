package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.world.entity.vehicle.MaintenanceMinecart;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.NetworkEvent;

public record SetMaintenanceMinecartAttributesMessage(int entityId, MaintenanceMinecart.Mode mode) {

  public void encode(FriendlyByteBuf out) {
    out.writeVarInt(this.entityId);
    out.writeEnum(this.mode);
  }

  public static SetMaintenanceMinecartAttributesMessage decode(FriendlyByteBuf in) {
    return new SetMaintenanceMinecartAttributesMessage(in.readVarInt(),
        in.readEnum(MaintenanceMinecart.Mode.class));
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    var player = ctx.get().getSender();
    var entity = player.level().getEntity(this.entityId);
    if (entity instanceof MaintenanceMinecart minecart) {
      minecart.setMode(this.mode);
    }
    return true;
  }
}

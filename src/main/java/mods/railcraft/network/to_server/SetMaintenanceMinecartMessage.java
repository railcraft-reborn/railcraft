package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.entity.vehicle.MaintenanceMinecart;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record SetMaintenanceMinecartMessage(
    int entityId, MaintenanceMinecart.Mode mode) implements CustomPacketPayload {

  public static final ResourceLocation ID = RailcraftConstants.rl("set_maintenance_minecart");


  public static SetMaintenanceMinecartMessage read(FriendlyByteBuf buf) {
    return new SetMaintenanceMinecartMessage(buf.readVarInt(),
        buf.readEnum(MaintenanceMinecart.Mode.class));
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeVarInt(this.entityId);
    buf.writeEnum(this.mode);
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  public void handle(PlayPayloadContext context) {
    context.level().ifPresent(level -> {
      var entity = level.getEntity(this.entityId);
      if (entity instanceof MaintenanceMinecart minecart) {
        minecart.setMode(this.mode);
      }
    });
  }
}

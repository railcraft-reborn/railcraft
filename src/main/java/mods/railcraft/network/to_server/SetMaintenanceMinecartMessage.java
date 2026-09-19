package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.entity.vehicle.MaintenanceMinecart;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetMaintenanceMinecartMessage(
    int entityId, MaintenanceMinecart.Mode mode) implements CustomPacketPayload {

  public static final Type<SetMaintenanceMinecartMessage> TYPE =
      new Type<>(RailcraftConstants.id("set_maintenance_minecart"));

  public static final StreamCodec<FriendlyByteBuf, SetMaintenanceMinecartMessage> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.VAR_INT, SetMaintenanceMinecartMessage::entityId,
          NeoForgeStreamCodecs.enumCodec(MaintenanceMinecart.Mode.class), SetMaintenanceMinecartMessage::mode,
          SetMaintenanceMinecartMessage::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SetMaintenanceMinecartMessage message, IPayloadContext context) {
    var level = context.player().level();
    var entity = level.getEntity(message.entityId);
    if (entity instanceof MaintenanceMinecart minecart) {
      minecart.setMode(message.mode);
    }
  }
}

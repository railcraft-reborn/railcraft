package mods.railcraft.network;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.network.to_client.LinkedCartsMessage;
import mods.railcraft.network.to_client.OpenLogBookScreen;
import mods.railcraft.network.to_client.SyncWidgetMessage;
import mods.railcraft.network.to_server.EditRoutingTableBookMessage;
import mods.railcraft.network.to_server.EditTicketMessage;
import mods.railcraft.network.to_server.SetActionSignalBoxMessage;
import mods.railcraft.network.to_server.SetAnalogSignalControllerBoxMessage;
import mods.railcraft.network.to_server.SetEmbarkingTrackMessage;
import mods.railcraft.network.to_server.SetFluidManipulatorMessage;
import mods.railcraft.network.to_server.SetLauncherTrackMessage;
import mods.railcraft.network.to_server.SetLocomotiveMessage;
import mods.railcraft.network.to_server.SetMaintenanceMinecartMessage;
import mods.railcraft.network.to_server.SetSignalCapacitorBoxMessage;
import mods.railcraft.network.to_server.SetSignalControllerBoxMessage;
import mods.railcraft.network.to_server.SetSwitchTrackMotorMessage;
import mods.railcraft.network.to_server.SetSwitchTrackRouterMessage;
import mods.railcraft.network.to_server.UpdateAuraByKeyMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

public final class PacketHandler {

  private PacketHandler() {
  }

  public static void registerPayloadHandler(RegisterPayloadHandlerEvent event) {
    var registrar = event.registrar(RailcraftConstants.ID).versioned("1");
    registerClientToServer(registrar);
    registerServerToClient(registrar);
  }

  private static void registerClientToServer(IPayloadRegistrar registrar) {
    registrar.play(EditRoutingTableBookMessage.ID, EditRoutingTableBookMessage::read,
        handler -> handler.server(EditRoutingTableBookMessage::handle));
    registrar.play(EditTicketMessage.ID, EditTicketMessage::read,
        handler -> handler.server(EditTicketMessage::handle));
    registrar.play(SetActionSignalBoxMessage.ID, SetActionSignalBoxMessage::read,
        handler -> handler.server(SetActionSignalBoxMessage::handle));
    registrar.play(SetAnalogSignalControllerBoxMessage.ID,
        SetAnalogSignalControllerBoxMessage::read,
        handler -> handler.server(SetAnalogSignalControllerBoxMessage::handle));
    registrar.play(SetEmbarkingTrackMessage.ID, SetEmbarkingTrackMessage::read,
        handler -> handler.server(SetEmbarkingTrackMessage::handle));
    registrar.play(SetFluidManipulatorMessage.ID, SetFluidManipulatorMessage::read,
        handler -> handler.server(SetFluidManipulatorMessage::handle));
    registrar.play(SetLauncherTrackMessage.ID, SetLauncherTrackMessage::read,
        handler -> handler.server(SetLauncherTrackMessage::handle));
    registrar.play(SetLocomotiveMessage.ID, SetLocomotiveMessage::read,
        handler -> handler.server(SetLocomotiveMessage::handle));
    registrar.play(SetMaintenanceMinecartMessage.ID, SetMaintenanceMinecartMessage::read,
        handler -> handler.server(SetMaintenanceMinecartMessage::handle));
    registrar.play(SetSignalCapacitorBoxMessage.ID, SetSignalCapacitorBoxMessage::read,
        handler -> handler.server(SetSignalCapacitorBoxMessage::handle));
    registrar.play(SetSignalControllerBoxMessage.ID, SetSignalControllerBoxMessage::read,
        handler -> handler.server(SetSignalControllerBoxMessage::handle));
    registrar.play(SetSwitchTrackMotorMessage.ID, SetSwitchTrackMotorMessage::read,
        handler -> handler.server(SetSwitchTrackMotorMessage::handle));
    registrar.play(SetSwitchTrackRouterMessage.ID, SetSwitchTrackRouterMessage::read,
        handler -> handler.server(SetSwitchTrackRouterMessage::handle));
    registrar.play(UpdateAuraByKeyMessage.ID, UpdateAuraByKeyMessage::read,
        handler -> handler.server(UpdateAuraByKeyMessage::handle));
  }

  private static void registerServerToClient(IPayloadRegistrar registrar) {
    registrar.play(SyncWidgetMessage.ID, SyncWidgetMessage::read,
        handler -> handler.client(SyncWidgetMessage::handle));
    registrar.play(LinkedCartsMessage.ID, LinkedCartsMessage::read,
        handler -> handler.client(LinkedCartsMessage::handle));
    registrar.play(OpenLogBookScreen.ID, OpenLogBookScreen::read,
        handler -> handler.client(OpenLogBookScreen::handle));
  }

  public static void sendToServer(CustomPacketPayload packet) {
    PacketDistributor.SERVER.noArg().send(packet);
  }

  public static void sendTo(ServerPlayer player, CustomPacketPayload packet) {
    PacketDistributor.PLAYER.with(player).send(packet);
  }

  @SuppressWarnings("deprecation")
  public static void sendToTrackingChunk(Packet<?> packet, ServerLevel level, BlockPos blockPos) {
    if (level.hasChunkAt(blockPos)) {
      PacketDistributor.TRACKING_CHUNK.with(level.getChunkAt(blockPos)).send(packet);
    }
  }

  public void sendToAll(CustomPacketPayload packet) {
    PacketDistributor.ALL.noArg().send(packet);
  }

  public void sendToAllAround(CustomPacketPayload packet, PacketDistributor.TargetPoint zone) {
    PacketDistributor.NEAR.with(zone).send(packet);
  }

  public void sendToDimension(CustomPacketPayload packet, ResourceKey<Level> dimensionId) {
    PacketDistributor.DIMENSION.with(dimensionId).send(packet);
  }
}

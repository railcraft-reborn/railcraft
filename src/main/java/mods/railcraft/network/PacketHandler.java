package mods.railcraft.network;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.network.to_client.LinkedCartsMessage;
import mods.railcraft.network.to_client.OpenLogBookScreen;
import mods.railcraft.network.to_client.SyncWidgetMessage;
import mods.railcraft.network.to_server.EditRoutingTableBookMessage;
import mods.railcraft.network.to_server.EditTicketMessage;
import mods.railcraft.network.to_server.SetActionSignalBoxMessage;
import mods.railcraft.network.to_server.SetAnalogSignalControllerBoxMessage;
import mods.railcraft.network.to_server.SetDelayedLockingTrackMessage;
import mods.railcraft.network.to_server.SetEmbarkingTrackMessage;
import mods.railcraft.network.to_server.SetFilterSlotMessage;
import mods.railcraft.network.to_server.SetFluidManipulatorMessage;
import mods.railcraft.network.to_server.SetItemDetectorMessage;
import mods.railcraft.network.to_server.SetItemManipulatorMessage;
import mods.railcraft.network.to_server.SetLauncherTrackMessage;
import mods.railcraft.network.to_server.SetLocomotiveByKeyMessage;
import mods.railcraft.network.to_server.SetLocomotiveMessage;
import mods.railcraft.network.to_server.SetMaintenanceMinecartMessage;
import mods.railcraft.network.to_server.SetMessengerTrackMessage;
import mods.railcraft.network.to_server.SetRoutingDetectorMessage;
import mods.railcraft.network.to_server.SetRoutingTrackMessage;
import mods.railcraft.network.to_server.SetSignalCapacitorBoxMessage;
import mods.railcraft.network.to_server.SetSignalControllerBoxMessage;
import mods.railcraft.network.to_server.SetSwitchTrackMotorMessage;
import mods.railcraft.network.to_server.SetSwitchTrackRouterMessage;
import mods.railcraft.network.to_server.SetTankDetectorMessage;
import mods.railcraft.network.to_server.SetTrainDetectorMessage;
import mods.railcraft.network.to_server.SleepInBedMinecartMessage;
import mods.railcraft.network.to_server.UpdateAuraByKeyMessage;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class PacketHandler {

  private PacketHandler() {
  }

  public static void register(IEventBus modEventBus) {
    modEventBus.addListener(RegisterPayloadHandlersEvent.class, event -> {
      var registrar = event.registrar(RailcraftConstants.ID).versioned("1");
      registerClientToServer(registrar);
      registerServerToClient(registrar);
    });
  }

  private static void registerClientToServer(PayloadRegistrar registrar) {
    registrar.playToServer(EditRoutingTableBookMessage.TYPE,
        EditRoutingTableBookMessage.STREAM_CODEC, EditRoutingTableBookMessage::handle);
    registrar.playToServer(EditTicketMessage.TYPE,
        EditTicketMessage.STREAM_CODEC, EditTicketMessage::handle);
    registrar.playToServer(SetActionSignalBoxMessage.TYPE,
        SetActionSignalBoxMessage.STREAM_CODEC, SetActionSignalBoxMessage::handle);
    registrar.playToServer(SetAnalogSignalControllerBoxMessage.TYPE,
        SetAnalogSignalControllerBoxMessage.STREAM_CODEC, SetAnalogSignalControllerBoxMessage::handle);
    registrar.playToServer(SetEmbarkingTrackMessage.TYPE,
        SetEmbarkingTrackMessage.STREAM_CODEC, SetEmbarkingTrackMessage::handle);
    registrar.playToServer(SetFluidManipulatorMessage.TYPE,
        SetFluidManipulatorMessage.STREAM_CODEC, SetFluidManipulatorMessage::handle);
    registrar.playToServer(SetItemManipulatorMessage.TYPE,
        SetItemManipulatorMessage.STREAM_CODEC, SetItemManipulatorMessage::handle);
    registrar.playToServer(SetLauncherTrackMessage.TYPE,
        SetLauncherTrackMessage.STREAM_CODEC, SetLauncherTrackMessage::handle);
    registrar.playToServer(SetDelayedLockingTrackMessage.TYPE,
        SetDelayedLockingTrackMessage.STREAM_CODEC, SetDelayedLockingTrackMessage::handle);
    registrar.playToServer(SetMessengerTrackMessage.TYPE,
        SetMessengerTrackMessage.STREAM_CODEC, SetMessengerTrackMessage::handle);
    registrar.playToServer(SetLocomotiveByKeyMessage.TYPE,
        SetLocomotiveByKeyMessage.STREAM_CODEC, SetLocomotiveByKeyMessage::handle);
    registrar.playToServer(SetLocomotiveMessage.TYPE,
        SetLocomotiveMessage.STREAM_CODEC, SetLocomotiveMessage::handle);
    registrar.playToServer(SetMaintenanceMinecartMessage.TYPE,
        SetMaintenanceMinecartMessage.STREAM_CODEC, SetMaintenanceMinecartMessage::handle);
    registrar.playToServer(SetRoutingTrackMessage.TYPE,
        SetRoutingTrackMessage.STREAM_CODEC, SetRoutingTrackMessage::handle);
    registrar.playToServer(SetSignalCapacitorBoxMessage.TYPE,
        SetSignalCapacitorBoxMessage.STREAM_CODEC, SetSignalCapacitorBoxMessage::handle);
    registrar.playToServer(SetSignalControllerBoxMessage.TYPE,
        SetSignalControllerBoxMessage.STREAM_CODEC, SetSignalControllerBoxMessage::handle);
    registrar.playToServer(SetSwitchTrackMotorMessage.TYPE,
        SetSwitchTrackMotorMessage.STREAM_CODEC, SetSwitchTrackMotorMessage::handle);
    registrar.playToServer(SetSwitchTrackRouterMessage.TYPE,
        SetSwitchTrackRouterMessage.STREAM_CODEC, SetSwitchTrackRouterMessage::handle);
    registrar.playToServer(UpdateAuraByKeyMessage.TYPE,
        UpdateAuraByKeyMessage.STREAM_CODEC, UpdateAuraByKeyMessage::handle);
    registrar.playToServer(SetTrainDetectorMessage.TYPE,
        SetTrainDetectorMessage.STREAM_CODEC, SetTrainDetectorMessage::handle);
    registrar.playToServer(SetItemDetectorMessage.TYPE,
        SetItemDetectorMessage.STREAM_CODEC, SetItemDetectorMessage::handle);
    registrar.playToServer(SetRoutingDetectorMessage.TYPE,
        SetRoutingDetectorMessage.STREAM_CODEC, SetRoutingDetectorMessage::handle);
    registrar.playToServer(SetTankDetectorMessage.TYPE,
        SetTankDetectorMessage.STREAM_CODEC, SetTankDetectorMessage::handle);
    registrar.playToServer(SetFilterSlotMessage.TYPE,
        SetFilterSlotMessage.STREAM_CODEC, SetFilterSlotMessage::handle);
    registrar.playToServer(SleepInBedMinecartMessage.TYPE,
        SleepInBedMinecartMessage.STREAM_CODEC, SleepInBedMinecartMessage::handle);
  }

  private static void registerServerToClient(PayloadRegistrar registrar) {
    registrar.playToClient(SyncWidgetMessage.TYPE,
        SyncWidgetMessage.STREAM_CODEC, SyncWidgetMessage::handle);
    registrar.playToClient(LinkedCartsMessage.TYPE,
        LinkedCartsMessage.STREAM_CODEC, LinkedCartsMessage::handle);
    registrar.playToClient(OpenLogBookScreen.TYPE,
        OpenLogBookScreen.STREAM_CODEC, OpenLogBookScreen::handle);
  }
}

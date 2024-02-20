package mods.railcraft.network;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.network.play.EditRoutingTableBookMessage;
import mods.railcraft.network.play.EditTicketAttributeMessage;
import mods.railcraft.network.play.LinkedCartsMessage;
import mods.railcraft.network.play.OpenLogBookScreen;
import mods.railcraft.network.play.SetActionSignalBoxAttributesMessage;
import mods.railcraft.network.play.SetAnalogSignalControllerBoxAttributesMessage;
import mods.railcraft.network.play.SetEmbarkingTrackAttributesMessage;
import mods.railcraft.network.play.SetFluidManipulatorAttributesMessage;
import mods.railcraft.network.play.SetItemDetectorAttributesMessage;
import mods.railcraft.network.play.SetItemManipulatorAttributesMessage;
import mods.railcraft.network.play.SetLauncherTrackAttributesMessage;
import mods.railcraft.network.play.SetLocomotiveAttributesMessage;
import mods.railcraft.network.play.SetMaintenanceMinecartAttributesMessage;
import mods.railcraft.network.play.SetRoutingTrackAttributesMessage;
import mods.railcraft.network.play.SetSignalCapacitorBoxAttributesMessage;
import mods.railcraft.network.play.SetSignalControllerBoxAttributesMessage;
import mods.railcraft.network.play.SetSwitchTrackMotorAttributesMessage;
import mods.railcraft.network.play.SetSwitchTrackRouterAttributesMessage;
import mods.railcraft.network.play.SetTankDetectorAttributesMessage;
import mods.railcraft.network.play.SetTrainDetectorAttributesMessage;
import mods.railcraft.network.play.SyncWidgetMessage;
import mods.railcraft.network.play.UpdateAuraByKeyMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * Holds {@link SimpleChannel} instances for each network channel type.
 */
public enum NetworkChannel {

  GAME("game") {
    @Override
    public void registerMessages(SimpleChannel simpleChannel) {
      simpleChannel
          .messageBuilder(SetLauncherTrackAttributesMessage.class, 0x00,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetLauncherTrackAttributesMessage::encode)
          .decoder(SetLauncherTrackAttributesMessage::decode)
          .consumerMainThread(SetLauncherTrackAttributesMessage::handle)
          .add();
      simpleChannel.messageBuilder(SyncWidgetMessage.class, 0x01, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(SyncWidgetMessage::encode)
          .decoder(SyncWidgetMessage::decode)
          .consumerMainThread(SyncWidgetMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetLocomotiveAttributesMessage.class, 0x02,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetLocomotiveAttributesMessage::encode)
          .decoder(SetLocomotiveAttributesMessage::decode)
          .consumerMainThread(SetLocomotiveAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetSignalControllerBoxAttributesMessage.class, 0x03,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetSignalControllerBoxAttributesMessage::encode)
          .decoder(SetSignalControllerBoxAttributesMessage::decode)
          .consumerMainThread(SetSignalControllerBoxAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetAnalogSignalControllerBoxAttributesMessage.class, 0x04,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetAnalogSignalControllerBoxAttributesMessage::encode)
          .decoder(SetAnalogSignalControllerBoxAttributesMessage::decode)
          .consumerMainThread(SetAnalogSignalControllerBoxAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetSignalCapacitorBoxAttributesMessage.class, 0x05,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetSignalCapacitorBoxAttributesMessage::encode)
          .decoder(SetSignalCapacitorBoxAttributesMessage::decode)
          .consumerMainThread(SetSignalCapacitorBoxAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(LinkedCartsMessage.class, 0x06, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(LinkedCartsMessage::encode)
          .decoder(LinkedCartsMessage::decode)
          .consumerNetworkThread(LinkedCartsMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetActionSignalBoxAttributesMessage.class, 0x07,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetActionSignalBoxAttributesMessage::encode)
          .decoder(SetActionSignalBoxAttributesMessage::decode)
          .consumerMainThread(SetActionSignalBoxAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetSwitchTrackMotorAttributesMessage.class, 0x08,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetSwitchTrackMotorAttributesMessage::encode)
          .decoder(SetSwitchTrackMotorAttributesMessage::decode)
          .consumerMainThread(SetSwitchTrackMotorAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetItemManipulatorAttributesMessage.class, 0x09,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetItemManipulatorAttributesMessage::encode)
          .decoder(SetItemManipulatorAttributesMessage::decode)
          .consumerMainThread(SetItemManipulatorAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetFluidManipulatorAttributesMessage.class, 0x0A,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetFluidManipulatorAttributesMessage::encode)
          .decoder(SetFluidManipulatorAttributesMessage::decode)
          .consumerMainThread(SetFluidManipulatorAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetEmbarkingTrackAttributesMessage.class, 0x0B,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetEmbarkingTrackAttributesMessage::encode)
          .decoder(SetEmbarkingTrackAttributesMessage::decode)
          .consumerMainThread(SetEmbarkingTrackAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(EditTicketAttributeMessage.class, 0x0C,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(EditTicketAttributeMessage::encode)
          .decoder(EditTicketAttributeMessage::decode)
          .consumerMainThread(EditTicketAttributeMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(EditRoutingTableBookMessage.class, 0x0D,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(EditRoutingTableBookMessage::encode)
          .decoder(EditRoutingTableBookMessage::decode)
          .consumerMainThread(EditRoutingTableBookMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetSwitchTrackRouterAttributesMessage.class, 0x0E,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetSwitchTrackRouterAttributesMessage::encode)
          .decoder(SetSwitchTrackRouterAttributesMessage::decode)
          .consumerMainThread(SetSwitchTrackRouterAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetRoutingTrackAttributesMessage.class, 0x0F,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetRoutingTrackAttributesMessage::encode)
          .decoder(SetRoutingTrackAttributesMessage::decode)
          .consumerMainThread(SetRoutingTrackAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(OpenLogBookScreen.class, 0x10, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(OpenLogBookScreen::encode)
          .decoder(OpenLogBookScreen::decode)
          .consumerMainThread(OpenLogBookScreen::handle)
          .add();
      simpleChannel
          .messageBuilder(SetMaintenanceMinecartAttributesMessage.class, 0x11,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetMaintenanceMinecartAttributesMessage::encode)
          .decoder(SetMaintenanceMinecartAttributesMessage::decode)
          .consumerMainThread(SetMaintenanceMinecartAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(UpdateAuraByKeyMessage.class, 0x12, NetworkDirection.PLAY_TO_SERVER)
          .encoder(UpdateAuraByKeyMessage::encode)
          .decoder(UpdateAuraByKeyMessage::decode)
          .consumerMainThread(UpdateAuraByKeyMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetTankDetectorAttributesMessage.class, 0x13,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetTankDetectorAttributesMessage::encode)
          .decoder(SetTankDetectorAttributesMessage::decode)
          .consumerMainThread(SetTankDetectorAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetTrainDetectorAttributesMessage.class, 0x14,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetTrainDetectorAttributesMessage::encode)
          .decoder(SetTrainDetectorAttributesMessage::decode)
          .consumerMainThread(SetTrainDetectorAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetItemDetectorAttributesMessage.class, 0x15,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetItemDetectorAttributesMessage::encode)
          .decoder(SetItemDetectorAttributesMessage::decode)
          .consumerMainThread(SetItemDetectorAttributesMessage::handle)
          .add();
    }
  };

  /**
   * Prevents re-registering messages.
   */
  private static boolean REGISTERED;
  private final SimpleChannel simpleChannel;

  NetworkChannel(String name) {
    var networkVersion = "1";
    this.simpleChannel = NetworkRegistry.ChannelBuilder
        .named(RailcraftConstants.rl(name))
        .clientAcceptedVersions(networkVersion::equals)
        .serverAcceptedVersions(networkVersion::equals)
        .networkProtocolVersion(() -> networkVersion)
        .simpleChannel();
  }

  protected abstract void registerMessages(SimpleChannel simpleChannel);

  // ================================================================================
  // Send Helper Methods
  // ================================================================================

  public void sendToServer(Object packet) {
    this.simpleChannel.sendToServer(packet);
  }

  public void sendTo(Object packet, ServerPlayer player) {
    this.simpleChannel.send(PacketDistributor.PLAYER.with(() -> player), packet);
  }

  public void sendToAll(Object packet) {
    this.simpleChannel.send(PacketDistributor.ALL.noArg(), packet);
  }

  public void sendToAllAround(Object packet, PacketDistributor.TargetPoint zone) {
    this.simpleChannel.send(PacketDistributor.NEAR.with(() -> zone), packet);
  }

  public void sendToDimension(Object packet, ResourceKey<Level> dimensionId) {
    this.simpleChannel.send(PacketDistributor.DIMENSION.with(() -> dimensionId), packet);
  }

  @SuppressWarnings("deprecation")
  public static void sendToTrackingChunk(Packet<?> packet, ServerLevel level, BlockPos blockPos) {
    if (level.hasChunkAt(blockPos)) {
      PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(blockPos)).send(packet);
    }
  }

  public static void registerAll() {
    if (!REGISTERED) {
      for (var channel : values()) {
        channel.registerMessages(channel.simpleChannel);
      }
      REGISTERED = true;
    }
  }
}

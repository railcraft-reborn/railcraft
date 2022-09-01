
package mods.railcraft.network;

import mods.railcraft.Railcraft;
import mods.railcraft.network.play.LinkedCartsMessage;
import mods.railcraft.network.play.SetActionSignalBoxAttributesMessage;
import mods.railcraft.network.play.SetAnalogSignalControllerBoxAttributesMessage;
import mods.railcraft.network.play.SetEmbarkingTrackAttributesMessage;
import mods.railcraft.network.play.SetFluidManipulatorAttributesMessage;
import mods.railcraft.network.play.SetItemManipulatorAttributesMessage;
import mods.railcraft.network.play.SetLauncherTrackAttributesMessage;
import mods.railcraft.network.play.SetLocomotiveAttributesMessage;
import mods.railcraft.network.play.SetSignalCapacitorBoxAttributesMessage;
import mods.railcraft.network.play.SetSignalControllerBoxAttributesMessage;
import mods.railcraft.network.play.SetSwitchTrackMotorAttributesMessage;
import mods.railcraft.network.play.SyncWidgetMessage;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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

  GAME(new ResourceLocation(Railcraft.ID, "game")) {
    @Override
    public void registerMessages(SimpleChannel simpleChannel) {
      int id = 0;
      simpleChannel
          .messageBuilder(SetLauncherTrackAttributesMessage.class, id++,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetLauncherTrackAttributesMessage::encode)
          .decoder(SetLauncherTrackAttributesMessage::decode)
          .consumerMainThread(SetLauncherTrackAttributesMessage::handle)
          .add();
      simpleChannel.messageBuilder(SyncWidgetMessage.class, id++, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(SyncWidgetMessage::encode)
          .decoder(SyncWidgetMessage::decode)
          .consumerMainThread(SyncWidgetMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetLocomotiveAttributesMessage.class, id++,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetLocomotiveAttributesMessage::encode)
          .decoder(SetLocomotiveAttributesMessage::decode)
          .consumerMainThread(SetLocomotiveAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetSignalControllerBoxAttributesMessage.class, id++,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetSignalControllerBoxAttributesMessage::encode)
          .decoder(SetSignalControllerBoxAttributesMessage::decode)
          .consumerMainThread(SetSignalControllerBoxAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetAnalogSignalControllerBoxAttributesMessage.class, id++,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetAnalogSignalControllerBoxAttributesMessage::encode)
          .decoder(SetAnalogSignalControllerBoxAttributesMessage::decode)
          .consumerMainThread(SetAnalogSignalControllerBoxAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetSignalCapacitorBoxAttributesMessage.class, id++,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetSignalCapacitorBoxAttributesMessage::encode)
          .decoder(SetSignalCapacitorBoxAttributesMessage::decode)
          .consumerMainThread(SetSignalCapacitorBoxAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(LinkedCartsMessage.class, id++, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(LinkedCartsMessage::encode)
          .decoder(LinkedCartsMessage::decode)
          .consumerNetworkThread(LinkedCartsMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetActionSignalBoxAttributesMessage.class, id++,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetActionSignalBoxAttributesMessage::encode)
          .decoder(SetActionSignalBoxAttributesMessage::decode)
          .consumerMainThread(SetActionSignalBoxAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetSwitchTrackMotorAttributesMessage.class, id++,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetSwitchTrackMotorAttributesMessage::encode)
          .decoder(SetSwitchTrackMotorAttributesMessage::decode)
          .consumerMainThread(SetSwitchTrackMotorAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetItemManipulatorAttributesMessage.class, id++,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetItemManipulatorAttributesMessage::encode)
          .decoder(SetItemManipulatorAttributesMessage::decode)
          .consumerMainThread(SetItemManipulatorAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetFluidManipulatorAttributesMessage.class, id++,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetFluidManipulatorAttributesMessage::encode)
          .decoder(SetFluidManipulatorAttributesMessage::decode)
          .consumerMainThread(SetFluidManipulatorAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetEmbarkingTrackAttributesMessage.class, id++,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetEmbarkingTrackAttributesMessage::encode)
          .decoder(SetEmbarkingTrackAttributesMessage::decode)
          .consumerMainThread(SetEmbarkingTrackAttributesMessage::handle)
          .add();
    }
  };

  /**
   * Network protocol version.
   */
  private static final String NETWORK_VERSION = "1";
  /**
   * Prevents re-registering messages.
   */
  private static boolean registered;
  /**
   * Simple channel.
   */
  private final SimpleChannel simpleChannel;

  private NetworkChannel(ResourceLocation channelName) {
    this.simpleChannel = NetworkRegistry.ChannelBuilder
        .named(channelName)
        .clientAcceptedVersions(NETWORK_VERSION::equals)
        .serverAcceptedVersions(NETWORK_VERSION::equals)
        .networkProtocolVersion(() -> NETWORK_VERSION)
        .simpleChannel();
  }

  protected abstract void registerMessages(SimpleChannel simpleChannel);

  public SimpleChannel simpleChannel() {
    return this.simpleChannel;
  }

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

  public static void registerAll() {
    if (!registered) {
      for (var channel : values()) {
        channel.registerMessages(channel.simpleChannel);
      }
      registered = true;
    }
  }
}

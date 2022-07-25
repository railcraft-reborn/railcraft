
package mods.railcraft.network;

import mods.railcraft.Railcraft;
import mods.railcraft.network.play.EffectMessage;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * Instance handlig all of railcraft network IOs.
 */
public enum NetworkChannel {

  GAME(new ResourceLocation(Railcraft.ID, "game")) {
    @Override
    public void registerMessages(SimpleChannel simpleChannel) {
      simpleChannel
          .messageBuilder(SetLauncherTrackAttributesMessage.class, 0x00,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetLauncherTrackAttributesMessage::encode)
          .decoder(SetLauncherTrackAttributesMessage::decode)
          .consumerMainThread(SetLauncherTrackAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(EffectMessage.class, 0x01, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(EffectMessage::encode)
          .decoder(EffectMessage::decode)
          .consumerMainThread(EffectMessage::handle)
          .add();
      simpleChannel.messageBuilder(SyncWidgetMessage.class, 0x02, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(SyncWidgetMessage::encode)
          .decoder(SyncWidgetMessage::decode)
          .consumerMainThread(SyncWidgetMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetLocomotiveAttributesMessage.class, 0x03,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetLocomotiveAttributesMessage::encode)
          .decoder(SetLocomotiveAttributesMessage::decode)
          .consumerMainThread(SetLocomotiveAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetSignalControllerBoxAttributesMessage.class, 0x04,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetSignalControllerBoxAttributesMessage::encode)
          .decoder(SetSignalControllerBoxAttributesMessage::decode)
          .consumerMainThread(SetSignalControllerBoxAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetAnalogSignalControllerBoxAttributesMessage.class, 0x05,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetAnalogSignalControllerBoxAttributesMessage::encode)
          .decoder(SetAnalogSignalControllerBoxAttributesMessage::decode)
          .consumerMainThread(SetAnalogSignalControllerBoxAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetSignalCapacitorBoxAttributesMessage.class, 0x06,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetSignalCapacitorBoxAttributesMessage::encode)
          .decoder(SetSignalCapacitorBoxAttributesMessage::decode)
          .consumerMainThread(SetSignalCapacitorBoxAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(LinkedCartsMessage.class, 0x07, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(LinkedCartsMessage::encode)
          .decoder(LinkedCartsMessage::decode)
          .consumerMainThread(LinkedCartsMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetActionSignalBoxAttributesMessage.class, 0x08,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetActionSignalBoxAttributesMessage::encode)
          .decoder(SetActionSignalBoxAttributesMessage::decode)
          .consumerMainThread(SetActionSignalBoxAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetSwitchTrackMotorAttributesMessage.class, 0x09,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetSwitchTrackMotorAttributesMessage::encode)
          .decoder(SetSwitchTrackMotorAttributesMessage::decode)
          .consumerMainThread(SetSwitchTrackMotorAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetItemManipulatorAttributesMessage.class, 0x0A,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetItemManipulatorAttributesMessage::encode)
          .decoder(SetItemManipulatorAttributesMessage::decode)
          .consumerMainThread(SetItemManipulatorAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetFluidManipulatorAttributesMessage.class, 0x0B,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetFluidManipulatorAttributesMessage::encode)
          .decoder(SetFluidManipulatorAttributesMessage::decode)
          .consumerMainThread(SetFluidManipulatorAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetEmbarkingTrackAttributesMessage.class, 0x0C,
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
  private static final String NETWORK_VERSION = "0.0.1.0";
  /**
   * Prevents re-registering messages.
   */
  private static boolean loaded;
  /**
   * Simple channel.
   */
  private final SimpleChannel simpleChannel;

  NetworkChannel(ResourceLocation channelName) {
    this.simpleChannel = NetworkRegistry.ChannelBuilder
        .named(channelName)
        .clientAcceptedVersions(NETWORK_VERSION::equals)
        .serverAcceptedVersions(NETWORK_VERSION::equals)
        .networkProtocolVersion(() -> NETWORK_VERSION)
        .simpleChannel();
  }

  protected abstract void registerMessages(SimpleChannel simpleChannel);

  public SimpleChannel getSimpleChannel() {
    return this.simpleChannel;
  }

  public static void registerAll() {
    if (!loaded) {
      for (var channel : NetworkChannel.values()) {
        channel.registerMessages(channel.simpleChannel);
      }
      loaded = true;
    }
  }
}

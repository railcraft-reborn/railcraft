
package mods.railcraft.network;

import mods.railcraft.Railcraft;
import mods.railcraft.network.play.PacketEffect;
import mods.railcraft.network.play.RequestPeersUpdateMessage;
import mods.railcraft.network.play.SetLocomotiveAttributesMessage;
import mods.railcraft.network.play.SetMenuStringMessage;
import mods.railcraft.network.play.SyncEntityMessage;
import mods.railcraft.network.play.SyncWidgetMessage;
import mods.railcraft.network.play.UpdatePeersMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public enum NetworkChannel {

  PLAY(new ResourceLocation(Railcraft.ID, "play")) {
    @Override
    public void registerMessages(SimpleChannel simpleChannel) {
      simpleChannel
          .messageBuilder(SyncEntityMessage.class, 0x00, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(SyncEntityMessage::encode)
          .decoder(SyncEntityMessage::decode)
          .consumer(SyncEntityMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(PacketEffect.class, 0x01, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(PacketEffect::encode)
          .decoder(PacketEffect::decode)
          .consumer(PacketEffect::handle)
          .add();
      simpleChannel
          .messageBuilder(SetMenuStringMessage.class, 0x02, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(SetMenuStringMessage::encode)
          .decoder(SetMenuStringMessage::decode)
          .consumer(SetMenuStringMessage::handle)
          .add();
      simpleChannel.messageBuilder(SyncWidgetMessage.class, 0x03, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(SyncWidgetMessage::encode)
          .decoder(SyncWidgetMessage::decode)
          .consumer(SyncWidgetMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SetLocomotiveAttributesMessage.class, 0x04,
              NetworkDirection.PLAY_TO_SERVER)
          .encoder(SetLocomotiveAttributesMessage::encode)
          .decoder(SetLocomotiveAttributesMessage::decode)
          .consumer(SetLocomotiveAttributesMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(UpdatePeersMessage.class, 0x05, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(UpdatePeersMessage::encode)
          .decoder(UpdatePeersMessage::decode)
          .consumer(UpdatePeersMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(RequestPeersUpdateMessage.class, 0x06, NetworkDirection.PLAY_TO_SERVER)
          .encoder(RequestPeersUpdateMessage::encode)
          .decoder(RequestPeersUpdateMessage::decode)
          .consumer(RequestPeersUpdateMessage::handle)
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

  private NetworkChannel(ResourceLocation channelName) {
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
      for (NetworkChannel channel : NetworkChannel.values()) {
        channel.registerMessages(channel.simpleChannel);
      }
      loaded = true;
    }
  }
}

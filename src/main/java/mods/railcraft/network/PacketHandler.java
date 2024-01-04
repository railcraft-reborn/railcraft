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
import mods.railcraft.network.to_server.SetItemManipulatorMessage;
import mods.railcraft.network.to_server.SetLauncherTrackMessage;
import mods.railcraft.network.to_server.SetLocomotiveMessage;
import mods.railcraft.network.to_server.SetMaintenanceMinecartMessage;
import mods.railcraft.network.to_server.SetRoutingTrackMessage;
import mods.railcraft.network.to_server.SetSignalCapacitorBoxMessage;
import mods.railcraft.network.to_server.SetSignalControllerBoxMessage;
import mods.railcraft.network.to_server.SetSwitchTrackMotorMessage;
import mods.railcraft.network.to_server.SetSwitchTrackRouterMessage;
import mods.railcraft.network.to_server.UpdateAuraByKeyMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IDirectionAwarePayloadHandlerBuilder;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

public final class PacketHandler {

  private PacketHandler() {
  }

  public static void registerPayloadHandler(RegisterPayloadHandlerEvent event) {
    var registrar = event.registrar(RailcraftConstants.ID).versioned("1");
    registerClientToServer(
        new PacketRegistrar(registrar, IDirectionAwarePayloadHandlerBuilder::server));
    registerServerToClient(
        new PacketRegistrar(registrar, IDirectionAwarePayloadHandlerBuilder::client));
  }

  @FunctionalInterface
  private interface ContextAwareHandler {
    <P extends CustomPacketPayload, T> void accept(IDirectionAwarePayloadHandlerBuilder<P, T> builder, T handler);
  }

  private record PacketRegistrar(IPayloadRegistrar registrar, ContextAwareHandler contextAwareHandler) {
    <T extends RailcraftCustomPacketPayload> void play(ResourceLocation id, FriendlyByteBuf.Reader<T> reader) {
      registrar.play(id, reader, builder ->
          contextAwareHandler.accept(builder, RailcraftCustomPacketPayload::handleMainThread));
    }
  }

  private static void registerClientToServer(PacketRegistrar registrar) {
    registrar.play(EditRoutingTableBookMessage.ID, EditRoutingTableBookMessage::read);
    registrar.play(EditTicketMessage.ID, EditTicketMessage::read);
    registrar.play(SetActionSignalBoxMessage.ID, SetActionSignalBoxMessage::read);
    registrar.play(SetAnalogSignalControllerBoxMessage.ID,
        SetAnalogSignalControllerBoxMessage::read);
    registrar.play(SetEmbarkingTrackMessage.ID, SetEmbarkingTrackMessage::read);
    registrar.play(SetFluidManipulatorMessage.ID, SetFluidManipulatorMessage::read);
    registrar.play(SetItemManipulatorMessage.ID, SetItemManipulatorMessage::read);
    registrar.play(SetLauncherTrackMessage.ID, SetLauncherTrackMessage::read);
    registrar.play(SetLocomotiveMessage.ID, SetLocomotiveMessage::read);
    registrar.play(SetMaintenanceMinecartMessage.ID, SetMaintenanceMinecartMessage::read);
    registrar.play(SetRoutingTrackMessage.ID, SetRoutingTrackMessage::read);
    registrar.play(SetSignalCapacitorBoxMessage.ID, SetSignalCapacitorBoxMessage::read);
    registrar.play(SetSignalControllerBoxMessage.ID, SetSignalControllerBoxMessage::read);
    registrar.play(SetSwitchTrackMotorMessage.ID, SetSwitchTrackMotorMessage::read);
    registrar.play(SetSwitchTrackRouterMessage.ID, SetSwitchTrackRouterMessage::read);
    registrar.play(UpdateAuraByKeyMessage.ID, UpdateAuraByKeyMessage::read);
  }

  private static void registerServerToClient(PacketRegistrar registrar) {
    registrar.play(SyncWidgetMessage.ID, SyncWidgetMessage::read);
    registrar.play(LinkedCartsMessage.ID, LinkedCartsMessage::read);
    registrar.play(OpenLogBookScreen.ID, OpenLogBookScreen::read);
  }

  public static void sendToServer(RailcraftCustomPacketPayload packet) {
    PacketDistributor.SERVER.noArg().send(packet);
  }

  public static void sendTo(ServerPlayer player, RailcraftCustomPacketPayload packet) {
    PacketDistributor.PLAYER.with(player).send(packet);
  }

  @SuppressWarnings("deprecation")
  public static void sendToTrackingChunk(Packet<?> packet, ServerLevel level, BlockPos blockPos) {
    if (level.hasChunkAt(blockPos)) {
      PacketDistributor.TRACKING_CHUNK.with(level.getChunkAt(blockPos)).send(packet);
    }
  }

  public static void sendToAll(RailcraftCustomPacketPayload packet) {
    PacketDistributor.ALL.noArg().send(packet);
  }

  public static void sendToAllAround(RailcraftCustomPacketPayload packet,
      PacketDistributor.TargetPoint zone) {
    PacketDistributor.NEAR.with(zone).send(packet);
  }

  public static void sendToDimension(RailcraftCustomPacketPayload packet,
      ResourceKey<Level> dimensionId) {
    PacketDistributor.DIMENSION.with(dimensionId).send(packet);
  }
}

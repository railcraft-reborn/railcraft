package mods.railcraft.client.renderer.entity;

import mods.railcraft.client.renderer.entity.cart.ElectricLocomotiveRenderer;
import mods.railcraft.client.renderer.entity.cart.SteamLocomotiveRenderer;
import mods.railcraft.client.renderer.entity.cart.TankMinecartRenderer;
import mods.railcraft.client.renderer.entity.cart.TrackLayerMinecartRenderer;
import mods.railcraft.client.renderer.entity.cart.TrackRelayerMinecartRenderer;
import mods.railcraft.client.renderer.entity.cart.TrackRemoverMinecartRenderer;
import mods.railcraft.client.renderer.entity.cart.TrackUndercutterMinecartRenderer;
import mods.railcraft.client.renderer.entity.cart.TunnelBoreRenderer;
import mods.railcraft.client.renderer.entity.cart.WorldSpikeMinecartRenderer;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class RailcraftEntityRenderers {

  public static void register(EntityRenderersEvent.RegisterRenderers event) {
    event.registerEntityRenderer(RailcraftEntityTypes.TANK_MINECART.get(),
        TankMinecartRenderer::new);
    event.registerEntityRenderer(RailcraftEntityTypes.WORLD_SPIKE.get(),
        WorldSpikeMinecartRenderer::new);
    event.registerEntityRenderer(RailcraftEntityTypes.TRACK_LAYER.get(),
        TrackLayerMinecartRenderer::new);
    event.registerEntityRenderer(RailcraftEntityTypes.TRACK_RELAYER.get(),
        TrackRelayerMinecartRenderer::new);
    event.registerEntityRenderer(RailcraftEntityTypes.TRACK_REMOVER.get(),
        TrackRemoverMinecartRenderer::new);
    event.registerEntityRenderer(RailcraftEntityTypes.TRACK_UNDERCUTTER.get(),
        TrackUndercutterMinecartRenderer::new);
    event.registerEntityRenderer(RailcraftEntityTypes.CREATIVE_LOCOMOTIVE.get(),
        ElectricLocomotiveRenderer::new);
    event.registerEntityRenderer(RailcraftEntityTypes.STEAM_LOCOMOTIVE.get(),
        SteamLocomotiveRenderer::new);
    event.registerEntityRenderer(RailcraftEntityTypes.ELECTRIC_LOCOMOTIVE.get(),
        ElectricLocomotiveRenderer::new);
    event.registerEntityRenderer(RailcraftEntityTypes.TUNNEL_BORE.get(),
        TunnelBoreRenderer::new);
    event.registerEntityRenderer(RailcraftEntityTypes.FIRESTONE.get(),
        ItemEntityRenderer::new);
  }
}

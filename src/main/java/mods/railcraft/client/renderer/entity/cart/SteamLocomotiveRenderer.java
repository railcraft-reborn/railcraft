package mods.railcraft.client.renderer.entity.cart;

import mods.railcraft.client.model.RailcraftModelLayers;
import mods.railcraft.client.model.SteamLocomotiveModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SteamLocomotiveRenderer extends DefaultLocomotiveRenderer {

  public SteamLocomotiveRenderer(EntityRendererProvider.Context context) {
    super(context, "steam",
        new SteamLocomotiveModel(context.bakeLayer(RailcraftModelLayers.STEAM_LOCOMOTIVE)),
        new SteamLocomotiveModel(context.bakeLayer(RailcraftModelLayers.STEAM_LOCOMOTIVE_SNOW)));
  }
}

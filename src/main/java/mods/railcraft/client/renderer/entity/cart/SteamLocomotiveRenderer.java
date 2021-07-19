package mods.railcraft.client.renderer.entity.cart;

import mods.railcraft.client.model.SteamLocomotiveModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;

public class SteamLocomotiveRenderer extends DefaultLocomotiveRenderer {

  public SteamLocomotiveRenderer(EntityRendererManager dispatcher) {
    super(dispatcher, "steam", new SteamLocomotiveModel(), new SteamLocomotiveModel(0.125F));
  }
}

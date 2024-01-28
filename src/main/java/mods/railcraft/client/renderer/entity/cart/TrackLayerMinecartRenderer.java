package mods.railcraft.client.renderer.entity.cart;

import mods.railcraft.Railcraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TrackLayerMinecartRenderer extends MaintenanceMinecartRenderer {

  public TrackLayerMinecartRenderer(EntityRendererProvider.Context context) {
    super(context, Railcraft.rl("textures/entity/minecart/track_layer_contents.png"));
  }
}

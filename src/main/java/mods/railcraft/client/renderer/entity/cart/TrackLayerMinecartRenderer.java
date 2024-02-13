package mods.railcraft.client.renderer.entity.cart;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TrackLayerMinecartRenderer extends MaintenanceMinecartRenderer {

  public TrackLayerMinecartRenderer(EntityRendererProvider.Context context) {
    super(context, RailcraftConstants.rl("textures/entity/minecart/track_layer_contents.png"));
  }
}

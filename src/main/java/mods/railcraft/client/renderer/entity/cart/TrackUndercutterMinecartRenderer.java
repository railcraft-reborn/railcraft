package mods.railcraft.client.renderer.entity.cart;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TrackUndercutterMinecartRenderer extends MaintenanceMinecartRenderer {

  public TrackUndercutterMinecartRenderer(EntityRendererProvider.Context context) {
    super(context, RailcraftConstants.rl("textures/entity/minecart/track_undercutter_contents.png"));
  }
}

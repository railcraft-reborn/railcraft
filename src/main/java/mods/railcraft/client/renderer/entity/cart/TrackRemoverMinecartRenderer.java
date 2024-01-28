package mods.railcraft.client.renderer.entity.cart;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TrackRemoverMinecartRenderer extends MaintenanceMinecartRenderer {

  public TrackRemoverMinecartRenderer(EntityRendererProvider.Context context) {
    super(context, RailcraftConstants.rl("textures/entity/minecart/track_remover_contents.png"));
  }
}

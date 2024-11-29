package mods.railcraft.client.renderer.entity.cart;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.renderer.entity.state.MaintenanceMinecartRendererState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TrackRemoverMinecartRenderer extends MaintenanceMinecartRenderer {

  public TrackRemoverMinecartRenderer(EntityRendererProvider.Context context) {
    super(context, RailcraftConstants.rl("textures/entity/minecart/track_remover_contents.png"));
  }

  @Override
  public MaintenanceMinecartRendererState createRenderState() {
    return new MaintenanceMinecartRendererState();
  }
}

package mods.railcraft.client.renderer.entity.cart;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.renderer.entity.state.MaintenanceMinecartRendererState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TrackRelayerMinecartRenderer extends MaintenanceMinecartRenderer {

  public TrackRelayerMinecartRenderer(EntityRendererProvider.Context context) {
    super(context, RailcraftConstants.id("textures/entity/minecart/track_relayer_contents.png"));
  }

  @Override
  public MaintenanceMinecartRendererState createRenderState() {
    return new MaintenanceMinecartRendererState();
  }
}

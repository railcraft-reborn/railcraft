package mods.railcraft.client.renderer.entity.cart;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TrackRelayerMinecartRenderer extends MaintenanceMinecartRenderer {

  public TrackRelayerMinecartRenderer(EntityRendererProvider.Context context) {
    super(context, RailcraftConstants.rl("textures/entity/minecart/track_relayer_contents.png"));
  }
}

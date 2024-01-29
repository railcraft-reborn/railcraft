package mods.railcraft.world.item.tunnelbore;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.RailcraftItemTier;
import net.minecraft.resources.ResourceLocation;

public class BronzeTunnelBoreHeadItem extends TunnelBoreHeadItem {

  private static final ResourceLocation TEXTURE_LOCATION =
      RailcraftConstants.rl("textures/entity/tunnel_bore/bronze_tunnel_bore.png");

  public BronzeTunnelBoreHeadItem(Properties properties) {
    super(RailcraftItemTier.BRONZE, properties);
  }

  @Override
  public ResourceLocation getTextureLocation() {
    return TEXTURE_LOCATION;
  }

  @Override
  public double getDigModifier() {
    return 1.25D;
  }
}

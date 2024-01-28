package mods.railcraft.world.item.tunnelbore;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tiers;

public class IronTunnelBoreHeadItem extends TunnelBoreHeadItem {

  private static final ResourceLocation TEXTURE_LOCATION =
      RailcraftConstants.rl("textures/entity/tunnel_bore/iron_tunnel_bore.png");

  public IronTunnelBoreHeadItem(Properties properties) {
    super(Tiers.IRON, properties);
  }

  @Override
  public ResourceLocation getTextureLocation() {
    return TEXTURE_LOCATION;
  }

  @Override
  public double getDigModifier() {
    return 1.0D;
  }
}

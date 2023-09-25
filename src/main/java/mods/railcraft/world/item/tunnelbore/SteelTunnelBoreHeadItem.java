package mods.railcraft.world.item.tunnelbore;

import mods.railcraft.Railcraft;
import mods.railcraft.world.item.RailcraftItemTier;
import net.minecraft.resources.ResourceLocation;

public class SteelTunnelBoreHeadItem extends TunnelBoreHeadItem {

  private static final ResourceLocation TEXTURE_LOCATION =
      Railcraft.rl("textures/entity/tunnel_bore/steel_tunnel_bore.png");

  public SteelTunnelBoreHeadItem(Properties properties) {
    super(RailcraftItemTier.STEEL, properties);
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

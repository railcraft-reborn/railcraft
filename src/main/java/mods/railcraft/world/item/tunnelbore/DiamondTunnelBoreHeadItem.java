package mods.railcraft.world.item.tunnelbore;

import mods.railcraft.Railcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tiers;

public class DiamondTunnelBoreHeadItem extends TunnelBoreHeadItem {

  private static final ResourceLocation TEXTURE =
      Railcraft.rl("textures/entity/tunnel_bore/diamond_tunnel_bore.png");

  public DiamondTunnelBoreHeadItem(Properties properties) {
    super(Tiers.DIAMOND, properties);
  }

  @Override
  public ResourceLocation getTextureLocation() {
    return TEXTURE;
  }

  @Override
  public double getDigModifier() {
    return 1.0D / 0.6D;
  }
}

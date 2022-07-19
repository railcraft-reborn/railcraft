package mods.railcraft.world.item;

import mods.railcraft.Railcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tiers;

public class DiamondTunnelBoreHeadItem extends TunnelBoreHeadItem {

  public static final ResourceLocation TEXTURE = new ResourceLocation(Railcraft.ID,
      "textures/entity/tunnel_bore/diamond_tunnel_bore.png");

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

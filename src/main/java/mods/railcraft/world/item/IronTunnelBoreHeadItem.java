package mods.railcraft.world.item;

import mods.railcraft.Railcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tiers;

import net.minecraft.world.item.Item.Properties;

public class IronTunnelBoreHeadItem extends TunnelBoreHeadItem {

  public static final ResourceLocation TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "textures/entity/tunnel_bore/iron_tunnel_bore.png");

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

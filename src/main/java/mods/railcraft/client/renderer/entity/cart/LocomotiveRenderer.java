package mods.railcraft.client.renderer.entity.cart;

import javax.annotation.Nullable;
import mods.railcraft.client.emblem.EmblemToolsClient;
import mods.railcraft.season.Seasons;
import mods.railcraft.world.entity.cart.locomotive.LocomotiveEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.DyeColor;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public abstract class LocomotiveRenderer<T extends LocomotiveEntity>
    extends CustomMinecartRenderer<T> {

  public LocomotiveRenderer(EntityRendererProvider.Context context) {
    super(context);
  }

  protected float[] getPrimaryColor(T loco) {
    return Seasons.isGhostTrain(loco)
        ? DyeColor.LIGHT_GRAY.getTextureDiffuseColors()
        : loco.getPrimaryColor();
  }

  protected float[] getSecondaryColor(T loco) {
    return Seasons.isGhostTrain(loco)
        ? DyeColor.LIGHT_GRAY.getTextureDiffuseColors()
        : loco.getSecondaryColor();
  }

  @Nullable
  protected ResourceLocation getEmblemTexture(T loco) {
    String emblem = loco.getEmblem();
    ResourceLocation emblemTexture = null;
    if (!StringUtil.isNullOrEmpty(emblem) && EmblemToolsClient.packageManager != null)
      emblemTexture = EmblemToolsClient.packageManager.getEmblemTextureLocation(emblem);
    return emblemTexture;
  }
}

package mods.railcraft.client.renderer.entity.cart;

import javax.annotation.Nullable;
import mods.railcraft.client.emblem.EmblemToolsClient;
import mods.railcraft.season.Seasons;
import mods.railcraft.world.entity.LocomotiveEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public abstract class LocomotiveRenderer<T extends LocomotiveEntity>
    extends CustomMinecartRenderer<T> {

  public LocomotiveRenderer(EntityRendererManager renderManager) {
    super(renderManager);
  }

  protected int getPrimaryColor(T loco) {
    return Seasons.isGhostTrain(loco)
        ? DyeColor.LIGHT_GRAY.getColorValue()
        : loco.getPrimaryColor();
  }

  protected int getSecondaryColor(T loco) {
    return Seasons.isGhostTrain(loco)
        ? DyeColor.LIGHT_GRAY.getColorValue()
        : loco.getSecondaryColor();
  }

  @Nullable
  protected ResourceLocation getEmblemTexture(T loco) {
    String emblem = loco.getEmblem();
    ResourceLocation emblemTexture = null;
    if (!StringUtils.isNullOrEmpty(emblem) && EmblemToolsClient.packageManager != null)
      emblemTexture = EmblemToolsClient.packageManager.getEmblemTextureLocation(emblem);
    return emblemTexture;
  }
}

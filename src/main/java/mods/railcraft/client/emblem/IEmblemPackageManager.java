package mods.railcraft.client.emblem;

import javax.annotation.Nullable;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;

/**
 *
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public interface IEmblemPackageManager {

  AbstractTexture getEmblemTexture(String ident);

  ResourceLocation getEmblemTextureLocation(String ident);

  @Nullable
  Emblem getEmblem(String ident);
}

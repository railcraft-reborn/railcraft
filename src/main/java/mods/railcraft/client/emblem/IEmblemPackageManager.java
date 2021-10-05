package mods.railcraft.client.emblem;

import javax.annotation.Nullable;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.util.ResourceLocation;

/**
 *
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public interface IEmblemPackageManager {

  Texture getEmblemTexture(String ident);

  ResourceLocation getEmblemTextureLocation(String ident);

  @Nullable
  Emblem getEmblem(String ident);
}

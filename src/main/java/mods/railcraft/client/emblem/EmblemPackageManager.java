package mods.railcraft.client.emblem;

import java.util.Optional;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;

/**
 *
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public interface EmblemPackageManager {

  AbstractTexture getEmblemTexture(String ident);

  ResourceLocation getEmblemTextureLocation(String ident);

  Optional<Emblem> getEmblem(String ident);
}

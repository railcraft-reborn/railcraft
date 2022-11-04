package mods.railcraft.client.emblem;

import java.util.Optional;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;

public class EmblemPackageManagerImpl implements EmblemPackageManager {

  @Override
  public AbstractTexture getEmblemTexture(String ident) {
    return null;
  }

  @Override
  public ResourceLocation getEmblemTextureLocation(String ident) {
    return null;
  }

  @Override
  public Optional<Emblem> getEmblem(String ident) {
    return Optional.empty();
  }
}

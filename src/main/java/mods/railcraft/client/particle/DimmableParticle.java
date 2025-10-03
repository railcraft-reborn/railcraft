package mods.railcraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public abstract class DimmableParticle extends SingleQuadParticle {

  protected DimmableParticle(ClientLevel level, double x, double y, double z,
      TextureAtlasSprite sprite) {
    super(level, x, y, z, sprite);
  }

  protected DimmableParticle(ClientLevel level, double x, double y, double z,
      double dx, double dy, double dz, TextureAtlasSprite sprite) {
    super(level, x, y, z, dx, dy, dz, sprite);
  }

  @Override
  public int getLightColor(float partialTicks) {
    int lightColor = super.getLightColor(partialTicks);
    double progress = Math.pow((double) this.age / this.lifetime, 3);
    int var4 = lightColor & 255;
    int var5 = lightColor >> 16 & 255;
    var5 += (int) (progress * 15 * 16);

    if (var5 > 240) {
      var5 = 240;
    }

    return var4 | var5 << 16;
  }
}

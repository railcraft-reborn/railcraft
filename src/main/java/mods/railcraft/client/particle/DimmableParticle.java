package mods.railcraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;

public abstract class DimmableParticle extends TextureSheetParticle {

  protected DimmableParticle(ClientLevel level, double x, double y, double z) {
    super(level, x, y, z);
  }

  protected DimmableParticle(
      ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
    super(level, x, y, z, dx, dy, dz);
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

package mods.railcraft.client.particle;

import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;

public abstract class DimmableParticle extends SpriteTexturedParticle {

  protected DimmableParticle(
      ClientWorld level, double x, double y, double z) {
    super(level, x, y, z);
  }

  protected DimmableParticle(
      ClientWorld level, double x, double y, double z, double dx, double dy, double dz) {
    super(level, x, y, z, dx, dy, dz);
  }

  @Override
  public int getLightColor(float par1) {
    int var2 = super.getLightColor(par1);
    float var3 = (float) this.age / (float) this.lifetime;
    var3 *= var3;
    var3 *= var3;
    int var4 = var2 & 255;
    int var5 = var2 >> 16 & 255;
    var5 += (int) (var3 * 15.0F * 16.0F);

    if (var5 > 240) {
      var5 = 240;
    }

    return var4 | var5 << 16;
  }
}

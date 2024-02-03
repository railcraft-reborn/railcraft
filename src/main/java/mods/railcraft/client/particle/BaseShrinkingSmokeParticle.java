package mods.railcraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;

public abstract class BaseShrinkingSmokeParticle extends BaseSmokeParticle {

  protected BaseShrinkingSmokeParticle(ClientLevel level, double x, double y, double z, double dx,
      double dy, double dz, float scale) {
    super(level, x, y, z, dx, dy, dz, scale);
  }

  @Override
  public float getQuadSize(float partialTicks) {
    return this.quadSize * Mth.clamp(
        (this.age + partialTicks) / this.lifetime * 32.0F, 0, 1.0F);
  }
}

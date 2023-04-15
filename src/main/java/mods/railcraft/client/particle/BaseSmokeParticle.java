package mods.railcraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;

/**
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public abstract class BaseSmokeParticle extends DimmableParticle {

  public static final float SMOKE_GRAVITY = -0.1F;

  protected BaseSmokeParticle(ClientLevel level, double x, double y, double z) {
    this(level, x, y, z, 0.0D, 0.0D, 0.0D, 3.0F);
  }

  protected BaseSmokeParticle(ClientLevel level, double x, double y, double z, double dx, double dy,
      double dz, float scale) {
    super(level, x, y, z);
    this.xd *= 0.1;
    this.yd *= 0.1;
    this.zd *= 0.1;
    this.xd += dx;
    this.yd += dy;
    this.zd += dz;
    this.quadSize *= 0.75F * scale;
    this.lifetime = (int) ((24.0F / (this.random.nextFloat() * 0.5F + 0.2F)) * scale);
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }
}

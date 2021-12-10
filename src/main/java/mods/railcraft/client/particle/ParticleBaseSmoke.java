package mods.railcraft.client.particle;

import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.multiplayer.ClientLevel;

/**
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public abstract class ParticleBaseSmoke extends DimmableParticle {

  public static final float SMOKE_GRAVITY = -0.1F;

  protected ParticleBaseSmoke(ClientLevel world, double x, double y, double z) {
    this(world, x, y, z, 0.0D, 0.0D, 0.0D, 3.0F);
  }

  protected ParticleBaseSmoke(ClientLevel world, double x, double y, double z, double dx, double dy,
      double dz, float scale) {
    super(world, x, y, z);
    this.xd *= 0.1;
    this.yd *= 0.1;
    this.zd *= 0.1;
    this.xd += dx;
    this.yd += dy;
    this.zd += dz;
    this.quadSize *= 0.75F * scale;
    this.lifetime = (int) ((24.0D / (Math.random() * 0.5D + 0.2D)) * scale);
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }
}

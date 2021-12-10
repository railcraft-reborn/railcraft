package mods.railcraft.client.particle;

import java.util.Random;

import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

/**
 * Created by CovertJaguar on 7/31/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class ParticleSpark extends DimmableParticle {
  private static final Random rand = new Random();

  private ParticleSpark(ClientLevel world, double x, double y, double z, double dx, double dy,
      double dz) {
    super(world, x, y, z, dx, dy, dz);
    this.gravity = 1.0F;
    this.setSize(0.15F, 0.15F); //AABB bounding cube so we can bounce
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  public static class SparkParticleFactory implements ParticleProvider<SimpleParticleType> {

    private final SpriteSet sprite;

    public SparkParticleFactory(SpriteSet sprite) {
      this.sprite = sprite;
    }

    @Override
    public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
        double x, double y, double z, double dx, double dy, double dz) {
      ParticleSpark particle = new ParticleSpark(level, x, y, z, dx, dy + 1.0D, dz);
      particle.setLifetime(rand.nextInt(10) + 10); // 10-20, 0.5sec to 1 sec
      particle.pickSprite(this.sprite);
      return particle;
    }
  }
}

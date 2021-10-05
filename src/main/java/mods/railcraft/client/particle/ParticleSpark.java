package mods.railcraft.client.particle;

import java.util.Random;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;

/**
 * Created by CovertJaguar on 7/31/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class ParticleSpark extends DimmableParticle {
  private static final Random rand = new Random();

  private ParticleSpark(ClientWorld world, double x, double y, double z, double dx, double dy,
      double dz) {
    super(world, x, y, z, dx, dy, dz);
    this.gravity = 1.0F;
    this.setSize(0.15F, 0.15F); //AABB bounding cube so we can bounce
  }

  @Override
  public IParticleRenderType getRenderType() {
    return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  public static class SparkParticleFactory implements IParticleFactory<BasicParticleType> {

    private final IAnimatedSprite sprite;

    public SparkParticleFactory(IAnimatedSprite sprite) {
      this.sprite = sprite;
    }

    @Override
    public Particle createParticle(BasicParticleType particleType, ClientWorld level,
        double x, double y, double z, double dx, double dy, double dz) {
      ParticleSpark particle = new ParticleSpark(level, x, y, z, dx, dy + 1.0D, dz);
      particle.setLifetime(rand.nextInt(10) + 10); // 10-20, 0.5sec to 1 sec
      particle.pickSprite(this.sprite);
      return particle;
    }
  }
}

package mods.railcraft.client.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;

/**
 * Created by CovertJaguar on 7/31/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class ParticleSpark extends DimmableParticle {

  public ParticleSpark(ClientWorld world, double x, double y, double z, double dx, double dy,
      double dz) {
    super(world, x, y, z, dx, dy, dz);
    this.gravity = 1.0F;
  }

  @Override
  public IParticleRenderType getRenderType() {
    return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  public static class Factory implements IParticleFactory<BasicParticleType> {

    private final IAnimatedSprite sprite;

    public Factory(IAnimatedSprite sprite) {
      this.sprite = sprite;
    }

    @Override
    public Particle createParticle(BasicParticleType particleType, ClientWorld level,
        double x, double y, double z, double dx, double dy, double dz) {
      ParticleSpark particle = new ParticleSpark(level, x, y, z, dx, dy + 1.0D, dz);
      particle.setLifetime((int) (8.0D / (Math.random() * 0.8D + 0.2D)));
      particle.pickSprite(this.sprite);
      return particle;
    }
  }
}

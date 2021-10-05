package mods.railcraft.client.particle;

import java.util.Random;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.MathHelper;

/**
 * Created by CovertJaguar on 7/31/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class ParticlePumpkin extends ParticleSteam {
  private static final Random rand = new Random();

  public ParticlePumpkin(ClientWorld world, double x, double y, double z, double dx, double dy,
    double dz) {
    super(world, x, y, z, dx, dy, dz);
    this.gravity = -0.01F;
    this.lifetime = (int) (16.0D / (rand.nextGaussian() * 0.8D + 0.2D));
  }

  @Override
  public float getQuadSize(float partialTicks) {
    return this.quadSize * (float) Math.sin(MathHelper.clamp(
        (this.age + partialTicks) / this.lifetime, 0.0F, 1.0F) * Math.PI);
  }

  public static class PumpkinParticleFactory implements IParticleFactory<BasicParticleType> {
    private final IAnimatedSprite spriteSet;

    public PumpkinParticleFactory(IAnimatedSprite spriteSet) {
      this.spriteSet = spriteSet;
    }

    @Override
    public Particle createParticle(BasicParticleType typeIn, ClientWorld worldIn,
        double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      ParticlePumpkin steam = new ParticlePumpkin(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
      steam.pickSprite(this.spriteSet);
      return steam;
    }
  }
}

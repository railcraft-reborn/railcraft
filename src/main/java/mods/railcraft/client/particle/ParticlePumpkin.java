package mods.railcraft.client.particle;

import java.util.Random;

import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

/**
 * Created by CovertJaguar on 7/31/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class ParticlePumpkin extends ParticleSteam {
  private static final Random rand = new Random();

  public ParticlePumpkin(ClientLevel world, double x, double y, double z, double dx, double dy,
    double dz) {
    super(world, x, y, z, dx, dy, dz);
    this.gravity = -0.01F;
    this.lifetime = (int) (16.0D / (rand.nextGaussian() * 0.8D + 0.2D));
  }

  @Override
  public float getQuadSize(float partialTicks) {
    return this.quadSize * (float) Math.sin(Mth.clamp(
        (this.age + partialTicks) / this.lifetime, 0.0F, 1.0F) * Math.PI);
  }

  public static class PumpkinParticleFactory implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet spriteSet;

    public PumpkinParticleFactory(SpriteSet spriteSet) {
      this.spriteSet = spriteSet;
    }

    @Override
    public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn,
        double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      ParticlePumpkin steam = new ParticlePumpkin(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
      steam.pickSprite(this.spriteSet);
      return steam;
    }
  }
}

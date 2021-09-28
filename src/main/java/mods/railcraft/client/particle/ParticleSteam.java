package mods.railcraft.client.particle;

import java.util.Random;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;

/**
 * Steam particle
 * Does NOT use CampfireParticle as an extendor due to private constructor (which we need!)
 * @see net.minecraft.client.particle.CampfireParticle Campfire for Reference
 */
public class ParticleSteam extends SpriteTexturedParticle {
  private static final Random rand = new Random();

  public ParticleSteam(ClientWorld world, double x, double y, double z, double dx, double dy,
      double dz) {
    super(world, x, y, z, dx, dy, dz);

    this.scale(1.7F);
    this.setSize(0.25F, 0.25F); //AABB bounding cube
    // 0.6 min (0.6-1.0), steam isn't as dark as smog
    this.rCol = this.gCol = this.bCol = (0.6f + rand.nextFloat() * 0.4f);
    // 20 ticks (1sec) 40 tics (max)
    this.lifetime = random.nextInt(50) + 30;
    this.gravity = 3.0E-6F;
    this.xd = dx;
    this.yd = dy + (this.random.nextDouble() / 500.0D);
    this.zd = dz;
  }

  /**
   * too lazy didnt do custom physics
   * @see net.minecraft.client.particle.CampfireParticle Campfire for Reference
   */
  @Override
  public void tick() {
    this.xo = this.x;
    this.yo = this.y;
    this.zo = this.z;
    if (this.age++ < this.lifetime && (this.alpha > 0.0F)) {
      this.xd += (random.nextFloat() / 5000.0F * (random.nextBoolean() ? 1F : -1F));
      this.zd += (random.nextFloat() / 5000.0F * (random.nextBoolean() ? 1F : -1F));
      this.yd -= this.gravity;
      this.move(this.xd, this.yd, this.zd);
      if (this.age >= this.lifetime - 60 && this.alpha > 0.01F) {
        this.alpha -= 0.015F;
      }
    } else {
      this.remove();
    }
  }

  @Override
  public IParticleRenderType getRenderType() {
    return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  public static class SteamParticleFactory implements IParticleFactory<BasicParticleType> {
    private final IAnimatedSprite spriteSet;

    public SteamParticleFactory(IAnimatedSprite spriteSet) {
      this.spriteSet = spriteSet;
    }

    @Override
    public Particle createParticle(BasicParticleType typeIn, ClientWorld worldIn,
        double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      ParticleSteam steam = new ParticleSteam(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
      steam.pickSprite(this.spriteSet);
      return steam;
    }
  }
}

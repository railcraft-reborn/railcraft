package mods.railcraft.client.particle;

import mods.railcraft.client.ClientEffects;
import mods.railcraft.particle.TuningAuraParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.world.phys.Vec3;

public class TuningAuraParticle extends DimmableParticle {

  private final Vec3 destination;

  public TuningAuraParticle(ClientLevel level, double x, double y, double z, double dx, double dy,
      double dz, TuningAuraParticleOptions options, SpriteSet sprites) {
    super(level, x, y, z, dx, dy, dz);
    this.destination = options.getDestination();
    this.calculateVector();

    this.scale(0.5F);

    float c1 = (float) (options.getColor() >> 16 & 255) / 255.0F;
    float c2 = (float) (options.getColor() >> 8 & 255) / 255.0F;
    float c3 = (float) (options.getColor() & 255) / 255.0F;

    float variant = this.random.nextFloat() * 0.6F + 0.4F;
    this.rCol = c1 * variant;
    this.gCol = c2 * variant;
    this.bCol = c3 * variant;
    this.setLifetime(2000);
    this.hasPhysics = false;
    this.pickSprite(sprites);
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  private void calculateVector() {
    Vec3 vecParticle = new Vec3(this.x, this.y, this.z);

    Vec3 vel = this.destination.subtract(vecParticle);
    vel = vel.normalize();

    float velScale = 0.1f;
    this.xd = vel.x * velScale;
    this.yd = vel.y * velScale;
    this.zd = vel.z * velScale;
  }

  /**
   * Called to update the entity's position/logic.
   */
  @Override
  public void tick() {
    if (!ClientEffects.INSTANCE.isTuningAuraActive()) {
      this.remove();
      return;
    }

    if (this.destination.distanceToSqr(this.x, this.y, this.z) <= 0.3) {
      this.remove();
      return;
    }

    super.tick();
  }

  public static class Provider implements ParticleProvider<TuningAuraParticleOptions> {

    private final SpriteSet sprites;

    public Provider(SpriteSet sprites) {
      this.sprites = sprites;
    }

    @Override
    public Particle createParticle(TuningAuraParticleOptions options, ClientLevel level,
        double x, double y, double z, double dx, double dy, double dz) {
      return new TuningAuraParticle(level, x, y, z, dx, dy, dz, options, this.sprites);
    }
  }
}

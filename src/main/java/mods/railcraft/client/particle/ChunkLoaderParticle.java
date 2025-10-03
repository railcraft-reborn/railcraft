package mods.railcraft.client.particle;

import mods.railcraft.particle.ChunkLoaderParticleOptions;
import mods.railcraft.world.item.GogglesItem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class ChunkLoaderParticle extends SingleQuadParticle {

  private final Vec3 dest;

  private ChunkLoaderParticle(ClientLevel level, double x, double y, double z, double dx,
      double dy, double dz, ChunkLoaderParticleOptions options, TextureAtlasSprite sprite) {
    super(level, x, y, z, dx, dy, dz, sprite);
    this.dest = options.destination();
    this.calculateVector();
    this.scale(1.2F);

    float brightness = random.nextFloat() * 0.6F + 0.4F;
    this.rCol = this.gCol = this.bCol = 1 * brightness;
    this.gCol *= 0.3F;
    this.rCol *= 0.9F;
    this.setLifetime(250);
    this.hasPhysics = false;
  }

  private void calculateVector() {
    var vecParticle = new Vec3(this.x, this.y, this.z);

    var vel = this.dest.subtract(vecParticle);
    vel = vel.normalize();

    float velScale = 0.08F;
    this.xd = vel.x * velScale;
    this.yd = vel.y * velScale;
    this.zd = vel.z * velScale;
  }

  @Override
  public void extract(QuadParticleRenderState state, Camera camera, float partialTicks) {
    // TODO: Test if this works
    if (Minecraft.getInstance().player.distanceToSqr(dest) > 25600)
      return;
    if(GogglesItem.isGoggleAuraActive(GogglesItem.Aura.WORLDSPIKE)) {
      super.extract(state, camera, partialTicks);
    }
  }

  @Override
  public void tick() {
    this.xo = this.x;
    this.yo = this.y;
    this.zo = this.z;

    if(!GogglesItem.isGoggleAuraActive(GogglesItem.Aura.WORLDSPIKE)) {
      this.remove();
      return;
    }

    if (!level.isLoaded(BlockPos.containing(dest))) {
      this.remove();
      return;
    }

    if (this.age++ >= this.lifetime) {
      this.remove();
      return;
    }

    if (this.getPos().distanceToSqr(this.dest) <= 0.5) {
      this.remove();
      return;
    }
    this.move(this.xd, this.yd, this.zd);
  }

  @Override
  protected Layer getLayer() {
    return Layer.TRANSLUCENT;
  }

  public static class Provider implements ParticleProvider<ChunkLoaderParticleOptions> {

    private final SpriteSet sprites;

    public Provider(SpriteSet sprites) {
      this.sprites = sprites;
    }

    @Override
    public Particle createParticle(ChunkLoaderParticleOptions options, ClientLevel level,
        double x, double y, double z, double dx, double dy, double dz, RandomSource randomSource) {
      return new ChunkLoaderParticle(level, x, y, z, dx, dy, dz, options, this.sprites.get(randomSource));
    }
  }
}

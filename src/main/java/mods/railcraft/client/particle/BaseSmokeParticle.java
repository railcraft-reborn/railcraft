package mods.railcraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public abstract class BaseSmokeParticle extends DimmableParticle {

  public static final float SMOKE_GRAVITY = -0.1F;

  protected BaseSmokeParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
    this(level, x, y, z, 0, 0, 0, 3.0F, sprite);
  }

  protected BaseSmokeParticle(ClientLevel level, double x, double y, double z, double dx, double dy,
      double dz, float scale, TextureAtlasSprite sprite) {
    super(level, x, y, z, sprite);
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
  protected Layer getLayer() {
    return Layer.TRANSLUCENT;
  }
}

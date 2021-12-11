package mods.railcraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;

public class ForceSpawnParticle extends BaseShrinkingSmokeParticle {

  public static final float SMOKE_GRAVITY = -0.1F;

  public ForceSpawnParticle(ClientLevel level, double x, double y, double z, double dx, double dy,
      double dz, int color) {
    this(level, x, y, z, dx, dy, dz, color, 1.0F);
  }

  public ForceSpawnParticle(ClientLevel level, double x, double y, double z, double dx, double dy,
      double dz, int color, float scale) {
    super(level, x, y, z, dx, dy, dz, scale);
    this.gravity = SMOKE_GRAVITY;
    this.rCol = ((color >> 16) & 0xFF) / 255F;
    this.gCol = ((color >> 8) & 0xFF) / 255F;
    this.bCol = ((color) & 0xFF) / 255F;
    this.lifetime = (int) (8.0F / (this.random.nextFloat() * 0.8F + 0.2F));
    this.lifetime = (int) ((float) this.lifetime * scale);
    this.hasPhysics = false;
  }
}

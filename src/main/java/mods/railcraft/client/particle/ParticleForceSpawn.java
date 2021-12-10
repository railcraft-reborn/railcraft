package mods.railcraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;

public class ParticleForceSpawn extends ParticleBaseSmokeShrinking {

  public static final float SMOKE_GRAVITY = -0.1F;

  public ParticleForceSpawn(ClientLevel world, double x, double y, double z, double dx, double dy,
      double dz, int color) {
    this(world, x, y, z, dx, dy, dz, color, 1.0F);
  }

  public ParticleForceSpawn(ClientLevel world, double x, double y, double z, double dx, double dy,
      double dz, int color, float scale) {
    super(world, x, y, z, dx, dy, dz, scale);
    this.gravity = SMOKE_GRAVITY;
    this.rCol = ((color >> 16) & 0xFF) / 255F;
    this.gCol = ((color >> 8) & 0xFF) / 255F;
    this.bCol = ((color) & 0xFF) / 255F;
    this.lifetime = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
    this.lifetime = (int) ((float) this.lifetime * scale);
    this.hasPhysics = false;
  }
}

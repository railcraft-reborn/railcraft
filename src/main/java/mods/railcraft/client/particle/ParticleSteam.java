package mods.railcraft.client.particle;

import net.minecraft.client.world.ClientWorld;

public class ParticleSteam extends ParticleBaseSmokeShrinking {

  public ParticleSteam(
      ClientWorld world, double x, double y, double z, double dx, double dy, double dz) {
    this(world, x, y, z, dx, dy, dz, 1.0F);
  }

  public ParticleSteam(ClientWorld world, double x, double y, double z, double dx, double dy,
      double dz, float scale) {
    super(world, x, y, z, dx, dy, dz, scale);
    this.gravity = SMOKE_GRAVITY;
    this.rCol = this.gCol = this.bCol = (float) (Math.random() * 0.4) + 0.4f;
    this.lifetime = (int) ((8.0D / (Math.random() * 0.8D + 0.2D)) * scale);
  }

}

package mods.railcraft.client.particle;

import net.minecraft.client.world.ClientWorld;

public class ParticleLocomotive extends ParticleBaseSmoke {

  public ParticleLocomotive(ClientWorld par1World, double x, double y, double z) {
    this(par1World, x, y, z, 0, 0, 0, 2.5f);
  }

  public ParticleLocomotive(ClientWorld par1World, double x, double y, double z, double dx,
      double dy,
      double dz, float scale) {
    super(par1World, x, y, z, dx, dy, dz, scale);
    this.gravity = -0.01F;
    this.rCol = this.gCol =
        this.bCol = (float) (Math.abs(this.random.nextGaussian()) * 0.2);
    this.lifetime = (int) ((24.0D / (Math.random() * 0.5D + 0.2D)) * scale);
  }
}

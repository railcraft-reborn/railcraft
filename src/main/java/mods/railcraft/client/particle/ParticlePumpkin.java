package mods.railcraft.client.particle;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;

/**
 * Created by CovertJaguar on 7/31/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class ParticlePumpkin extends ParticleBaseSmoke {

  public ParticlePumpkin(ClientWorld par1World, double x, double y, double z) {
    this(par1World, x, y, z, 0, 0, 0, 2.5F);
  }

  public ParticlePumpkin(ClientWorld world, double x, double y, double z, double dx, double dy,
      double dz,
      float scale) {
    super(world, x, y, z, dx, dy, dz, scale);
    this.gravity = -0.01F;
    this.lifetime = (int) (16.0D / (Math.random() * 0.8D + 0.2D));
  }

  @Override
  public float getQuadSize(float partialTicks) {
    return this.quadSize * (float) Math.sin(MathHelper.clamp(
        ((float) this.age + partialTicks) / (float) this.lifetime, 0.0F, 1.0F) * Math.PI);
  }
}

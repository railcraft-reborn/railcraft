package mods.railcraft.client.particle;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.DyeColor;
import net.minecraft.util.math.MathHelper;

public class ParticleChimney extends ParticleBaseSmoke {

  public ParticleChimney(ClientWorld par1World, double x, double y, double z) {
    this(par1World, x, y, z, DyeColor.BLACK.getColorValue());
  }

  public ParticleChimney(ClientWorld par1World, double x, double y, double z, int color) {
    this(par1World, x, y, z, 0.0D, 0.0D, 0.0D, 3f, color);
  }

  public ParticleChimney(ClientWorld par1World, double x, double y, double z, double dx, double dy,
      double dz, float scale, int color) {
    super(par1World, x, y, z, dx, dy, dz, scale);
    this.gravity = SMOKE_GRAVITY;
    this.rCol = MathHelper.clamp(
        (float) (Math.random() * 0.1f - 0.05f) + ((color >> 16) & 0xFF) / 255f, 0f,
        1f);
    this.gCol = MathHelper.clamp(
        (float) (Math.random() * 0.1f - 0.05f) + ((color >> 8) & 0xFF) / 255f, 0f,
        1f);
    this.bCol = MathHelper.clamp(
        (float) (Math.random() * 0.1f - 0.05f) + ((color) & 0xFF) / 255f, 0f, 1f);
    this.lifetime = (int) ((24.0D / (Math.random() * 0.5D + 0.2D)) * scale);
  }
}

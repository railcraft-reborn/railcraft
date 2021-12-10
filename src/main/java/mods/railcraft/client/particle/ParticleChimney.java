package mods.railcraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.MaterialColor;

public class ParticleChimney extends ParticleBaseSmoke {

  public ParticleChimney(ClientLevel par1World, double x, double y, double z) {
    this(par1World, x, y, z, MaterialColor.COLOR_BLACK.col);
  }

  public ParticleChimney(ClientLevel par1World, double x, double y, double z, int color) {
    this(par1World, x, y, z, 0.0D, 0.0D, 0.0D, 3f, color);
  }

  public ParticleChimney(ClientLevel par1World, double x, double y, double z, double dx, double dy,
      double dz, float scale, int color) {
    super(par1World, x, y, z, dx, dy, dz, scale);
    this.gravity = SMOKE_GRAVITY;
    this.rCol = Mth.clamp(
        (float) (Math.random() * 0.1f - 0.05f) + ((color >> 16) & 0xFF) / 255f, 0f,
        1f);
    this.gCol = Mth.clamp(
        (float) (Math.random() * 0.1f - 0.05f) + ((color >> 8) & 0xFF) / 255f, 0f,
        1f);
    this.bCol = Mth.clamp(
        (float) (Math.random() * 0.1f - 0.05f) + ((color) & 0xFF) / 255f, 0f, 1f);
    this.lifetime = (int) ((24.0D / (Math.random() * 0.5D + 0.2D)) * scale);
  }
}

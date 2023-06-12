package mods.railcraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.MapColor;

public class ChimneyParticle extends BaseSmokeParticle {

  public ChimneyParticle(ClientLevel level, double x, double y, double z) {
    this(level, x, y, z, MapColor.COLOR_BLACK.col);
  }

  public ChimneyParticle(ClientLevel level, double x, double y, double z, int color) {
    this(level, x, y, z, 0.0D, 0.0D, 0.0D, 3f, color);
  }

  public ChimneyParticle(ClientLevel level, double x, double y, double z, double dx, double dy,
      double dz, float scale, int color) {
    super(level, x, y, z, dx, dy, dz, scale);
    this.gravity = SMOKE_GRAVITY;
    this.rCol =
        Mth.clamp((this.random.nextFloat() * 0.1f - 0.05f) + ((color >> 16) & 0xFF) / 255f, 0f, 1f);
    this.gCol =
        Mth.clamp((this.random.nextFloat() * 0.1f - 0.05f) + ((color >> 8) & 0xFF) / 255f, 0f, 1f);
    this.bCol =
        Mth.clamp((this.random.nextFloat() * 0.1f - 0.05f) + ((color) & 0xFF) / 255f, 0f, 1f);
    this.lifetime = (int) ((24.0F / (this.random.nextFloat() * 0.5F + 0.2F)) * scale);
  }
}

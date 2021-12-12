package mods.railcraft.client.particle;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;

public class BlockCrackParticle extends TerrainParticle {

  public BlockCrackParticle(ClientLevel level, double x, double y, double z,
      double dx, double dy, double dz, BlockState blockState) {
    super(level, x, y, z, dx, dy, dz, blockState);
  }

  public void setVelocity(Vec3 speed) {
    this.xd = speed.x;
    this.yd = speed.y;
    this.zd = speed.z;
  }
}

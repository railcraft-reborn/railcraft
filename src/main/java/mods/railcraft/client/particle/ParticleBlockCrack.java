package mods.railcraft.client.particle;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;

public class ParticleBlockCrack extends TerrainParticle {

  public ParticleBlockCrack(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn,
      double xSpeedIn, double ySpeedIn, double zSpeedIn, BlockState state) {
    super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, state);
  }

  public void setVelocity(Vec3 speed) {
    this.xd = speed.x;
    this.yd = speed.y;
    this.zd = speed.z;
  }
}

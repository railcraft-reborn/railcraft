package mods.railcraft.client.particle;

import net.minecraft.block.BlockState;
import net.minecraft.client.particle.DiggingParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.vector.Vector3d;

public class ParticleBlockCrack extends DiggingParticle {

  public ParticleBlockCrack(ClientWorld worldIn, double xCoordIn, double yCoordIn, double zCoordIn,
      double xSpeedIn, double ySpeedIn, double zSpeedIn, BlockState state) {
    super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, state);
  }

  public void setVelocity(Vector3d speed) {
    this.xd = speed.x;
    this.yd = speed.y;
    this.zd = speed.z;
  }
}

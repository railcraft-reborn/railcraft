package mods.railcraft.world.level.block.entity.track;

import mods.railcraft.RailcraftConfig;
import mods.railcraft.world.entity.vehicle.MinecartExtension;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.track.outfitted.PoweredOutfittedTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LauncherTrackBlockEntity extends BlockEntity {

  public static final int MIN_LAUNCH_FORCE = 5;
  private static final float LAUNCH_THRESHOLD = 0.01F;
  private byte launchForce = 5;

  public LauncherTrackBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.LAUNCHER_TRACK.get(), blockPos, blockState);
  }

  // Called by block
  public void minecartPassed(AbstractMinecart cart) {
    if (PoweredOutfittedTrackBlock.isPowered(this.getBlockState())) {
      var motion = cart.getDeltaMovement();
      var newMotionX = motion.x();
      var newMotionZ = motion.z();
      if (Math.abs(motion.x()) > LAUNCH_THRESHOLD) {
        newMotionX = Math.copySign(0.6F, motion.x());
      }
      if (Math.abs(motion.z()) > LAUNCH_THRESHOLD) {
        newMotionZ = Math.copySign(0.6F, motion.z());
      }
      cart.setMaxSpeedAirLateral(0.6F);
      cart.setMaxSpeedAirVertical(0.5F);
      cart.setDragAir(0.99999);
      MinecartExtension.getOrThrow(cart).launch();
      cart.setDeltaMovement(newMotionX, this.getLaunchForce() * 0.1D, newMotionZ);
      cart.move(MoverType.SELF, cart.getDeltaMovement());
    }
  }

  public byte getLaunchForce() {
    return this.launchForce;
  }

  public void setLaunchForce(byte launchForce) {
    this.launchForce = (byte) Mth.clamp(launchForce, MIN_LAUNCH_FORCE,
        RailcraftConfig.server.maxLauncherTrackForce.get());
  }
}

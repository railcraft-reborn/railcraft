package mods.railcraft.world.level.block.entity.track;

import mods.railcraft.api.carts.CartUtil;
import mods.railcraft.util.TrackShapeHelper;
import mods.railcraft.world.level.block.track.TrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LockingModeController;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public class HoldingLockingProfile implements LockingModeController {

  protected static final float DIR_THRESHOLD = 0.01F;
  protected boolean launchForward = true;

  private final LockingTrackBlockEntity lockingTrack;

  public HoldingLockingProfile(LockingTrackBlockEntity lockingTrack) {
    this.lockingTrack = lockingTrack;
  }

  @Override
  public void locked(AbstractMinecartEntity cart) {
    this.setLaunchDirection(cart);
  }

  @Override
  public void passed(AbstractMinecartEntity cart) {
    this.setLaunchDirection(cart);
  }

  @Override
  public void released(AbstractMinecartEntity cart) {
    RailShape railShape = TrackBlock.getRailShapeRaw(this.lockingTrack.getBlockState());
    cart.setDeltaMovement(this.applyBoost(
        TrackShapeHelper.isNorthSouth(railShape) ? Direction.Axis.Z : Direction.Axis.X,
        cart.getDeltaMovement()));
  }

  private Vector3d applyBoost(Direction.Axis axis, Vector3d deltaMovement) {
    double speed = CartUtil.getCartSpeedUncapped(deltaMovement);
    double boost = speed > 0.005D
        ? (Math.abs(deltaMovement.get(axis)) / speed) * LockingTrackBlockEntity.BOOST_FACTOR
        : LockingTrackBlockEntity.START_BOOST;
    Vector3d newDeltaMovement;
    if (this.launchForward) {
      newDeltaMovement = deltaMovement.add(axis == Direction.Axis.X ? boost : 0.0D, 0.0D,
          axis == Direction.Axis.Z ? boost : 0.0D);
    } else {
      newDeltaMovement = deltaMovement.subtract(axis == Direction.Axis.X ? boost : 0.0D, 0.0D,
          axis == Direction.Axis.Z ? boost : 0.0D);
    }
    return newDeltaMovement;
  }

  protected void setLaunchDirection(AbstractMinecartEntity cart) {
    RailShape railShape = TrackBlock.getRailShapeRaw(this.lockingTrack.getBlockState());
    final Vector3d deltaMovement = cart.getDeltaMovement();
    if (CartUtil.getCartSpeedUncapped(deltaMovement) > DIR_THRESHOLD) {
      this.launchForward =
          TrackShapeHelper.isNorthSouth(railShape) ? deltaMovement.z() > 0.0D : deltaMovement.x() > 0.0D;
    }
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT tag = new CompoundNBT();
    tag.putBoolean("launchForward", this.launchForward);
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundNBT data) {
    this.launchForward = data.getBoolean("launchForward");
  }
}

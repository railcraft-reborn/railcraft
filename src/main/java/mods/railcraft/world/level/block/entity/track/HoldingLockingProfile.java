package mods.railcraft.world.level.block.entity.track;

import mods.railcraft.api.carts.CartUtil;
import mods.railcraft.api.track.RailShapeUtil;
import mods.railcraft.world.level.block.track.TrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LockingModeController;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

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
  public void locked(AbstractMinecart cart) {
    this.setLaunchDirection(cart);
  }

  @Override
  public void passed(AbstractMinecart cart) {
    this.setLaunchDirection(cart);
  }

  @Override
  public void released(AbstractMinecart cart) {
    RailShape railShape = TrackBlock.getRailShapeRaw(this.lockingTrack.getBlockState());
    cart.setDeltaMovement(this.applyBoost(
        RailShapeUtil.isNorthSouth(railShape) ? Direction.Axis.Z : Direction.Axis.X,
        cart.getDeltaMovement()));
  }

  private Vec3 applyBoost(Direction.Axis axis, Vec3 deltaMovement) {
    double speed = CartUtil.getCartSpeedUncapped(deltaMovement);
    double boost = speed > 0.005D
        ? (Math.abs(deltaMovement.get(axis)) / speed) * LockingTrackBlockEntity.BOOST_FACTOR
        : LockingTrackBlockEntity.START_BOOST;
    Vec3 newDeltaMovement;
    if (this.launchForward) {
      newDeltaMovement = deltaMovement.add(axis == Direction.Axis.X ? boost : 0.0D, 0.0D,
          axis == Direction.Axis.Z ? boost : 0.0D);
    } else {
      newDeltaMovement = deltaMovement.subtract(axis == Direction.Axis.X ? boost : 0.0D, 0.0D,
          axis == Direction.Axis.Z ? boost : 0.0D);
    }
    return newDeltaMovement;
  }

  protected void setLaunchDirection(AbstractMinecart cart) {
    RailShape railShape = TrackBlock.getRailShapeRaw(this.lockingTrack.getBlockState());
    final Vec3 deltaMovement = cart.getDeltaMovement();
    if (CartUtil.getCartSpeedUncapped(deltaMovement) > DIR_THRESHOLD) {
      this.launchForward =
          RailShapeUtil.isNorthSouth(railShape) ? deltaMovement.z() > 0.0D : deltaMovement.x() > 0.0D;
    }
  }

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag tag = new CompoundTag();
    tag.putBoolean("launchForward", this.launchForward);
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag data) {
    this.launchForward = data.getBoolean("launchForward");
  }
}

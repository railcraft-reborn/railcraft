package mods.railcraft.world.level.block.entity.track;

import mods.railcraft.api.carts.CartUtil;
import mods.railcraft.util.TrackShapeHelper;
import mods.railcraft.world.level.block.track.TrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LockingModeController;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public class BoardingLockingProfile implements LockingModeController {

  private final LockingTrackBlockEntity lockingTrack;
  private final boolean reversed;

  private BoardingLockingProfile(LockingTrackBlockEntity lockingTrack, boolean reversed) {
    this.lockingTrack = lockingTrack;
    this.reversed = reversed;
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
    if (this.reversed) {
      newDeltaMovement = deltaMovement.add(axis == Direction.Axis.X ? -boost : 0.0D, 0.0D,
          axis == Direction.Axis.Z ? boost : 0.0D);
    } else {
      newDeltaMovement = deltaMovement.add(axis == Direction.Axis.X ? boost : 0.0D, 0.0D,
          axis == Direction.Axis.Z ? -boost : 0.0D);
    }
    return newDeltaMovement;
  }

  public static BoardingLockingProfile normal(LockingTrackBlockEntity lockingTrack) {
    return new BoardingLockingProfile(lockingTrack, false);
  }

  public static BoardingLockingProfile reversed(LockingTrackBlockEntity lockingTrack) {
    return new BoardingLockingProfile(lockingTrack, true);
  }
}

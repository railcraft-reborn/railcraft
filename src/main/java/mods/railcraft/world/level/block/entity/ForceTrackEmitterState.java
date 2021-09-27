package mods.railcraft.world.level.block.entity;

import mods.railcraft.api.tracks.LockdownTrack;
import mods.railcraft.util.TrackTools;
import mods.railcraft.world.level.block.ForceTrackEmitterBlock;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public enum ForceTrackEmitterState {

  /**
   * A state when the track is fully built and ready for carts.
   */
  EXTENDED(true) {
    @Override
    ForceTrackEmitterState afterUseCharge(
        ForceTrackEmitterBlockEntity emitter) {
      return emitter.clock(TICKS_PER_REFRESH) ? EXTENDING : this;
    }

    @Override
    void onTransition(ForceTrackEmitterBlockEntity emitter) {
      BlockPos pos = emitter.getBlockPos().above();
      BlockState blockState = emitter.getLevel().getBlockState(pos);
      if (blockState.getBlock() instanceof LockdownTrack) {
        LockdownTrack lockdownTrack = (LockdownTrack) blockState.getBlock();
        lockdownTrack.releaseCart(blockState, emitter.getLevel(), pos);
      }
    }
  },
  /**
   * A state in which no track presents.
   */
  RETRACTED(false) {
    @Override
    ForceTrackEmitterState whenNoCharge(ForceTrackEmitterBlockEntity emitter) {
      return this;
    }
  },
  /**
   * A state in which the track is in progress of building.
   */
  EXTENDING(true) {
    @Override
    ForceTrackEmitterState afterUseCharge(
        ForceTrackEmitterBlockEntity emitter) {
      if (emitter.isOutOfPower())
        return HALTED;
      if (emitter.getNumTracks() >= MAX_TRACKS)
        return EXTENDED;
      if (emitter.clock(TICKS_PER_ACTION)) {
        Direction facing = emitter.getBlockState().getValue(ForceTrackEmitterBlock.FACING);
        BlockPos toPlace =
            emitter.getBlockPos().above().relative(facing, emitter.getNumTracks() + 1);
        if (emitter.getLevel().isLoaded(toPlace)) {
          BlockState blockState = emitter.getLevel().getBlockState(toPlace);
          RailShape direction = TrackTools.getAxisAlignedDirection(facing);
          if (!emitter.placeTrack(toPlace, blockState, direction))
            return EXTENDED;
        } else {
          return HALTED;
        }
      }
      return this;
    }
  },
  /**
   * A state in which the tracks are destroyed.
   */
  RETRACTING(false) {
    @Override
    ForceTrackEmitterState whenNoCharge(
        ForceTrackEmitterBlockEntity emitter) {
      if (emitter.getNumTracks() > 0) {
        if (emitter.clock(TICKS_PER_ACTION)) {
          emitter.removeFirstTrack();
        }
        return this;
      } else {
        return RETRACTED;
      }
    }
  },
  /**
   * A state in which the state will wait for a transition.
   */
  HALTED(false);

  private static final int TICKS_PER_ACTION = 2;
  private static final int TICKS_PER_REFRESH = 64;
  public static final int MAX_TRACKS = 64;

  static final ForceTrackEmitterState[] VALUES = values();
  final boolean appearPowered;

  private ForceTrackEmitterState(boolean appearPowered) {
    this.appearPowered = appearPowered;
  }

  /**
   * Determines what state the emitter will be after using charge.
   *
   * @param emitter The emitter
   * @return The new state
   */
  ForceTrackEmitterState afterUseCharge(ForceTrackEmitterBlockEntity emitter) {
    return EXTENDING;
  }

  /**
   * Determines what state the emitter will be if there is no charge available.
   *
   * @return The new state
   */
  ForceTrackEmitterState whenNoCharge(ForceTrackEmitterBlockEntity emitter) {
    return RETRACTING;
  }

  void onTransition(ForceTrackEmitterBlockEntity emitter) {}
}

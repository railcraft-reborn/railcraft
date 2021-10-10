package mods.railcraft.world.level.block.entity;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.track.LockingTrack;
import mods.railcraft.util.TrackTools;
import mods.railcraft.world.level.block.ForceTrackEmitterBlock;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;

public enum ForceTrackEmitterState implements IStringSerializable {

  /**
   * A state when the track is fully built and ready for carts.
   */
  EXTENDED("extended", true) {
    @Override
    ForceTrackEmitterState charged(
        ForceTrackEmitterBlockEntity emitter) {
      return emitter.clock(TICKS_PER_REFRESH) ? EXTENDING : this;
    }

    @Override
    void load(ForceTrackEmitterBlockEntity emitter) {
      BlockPos pos = emitter.getBlockPos().above();
      BlockState blockState = emitter.getLevel().getBlockState(pos);
      if (blockState.getBlock() instanceof LockingTrack) {
        ((LockingTrack) blockState.getBlock()).releaseCart();
      }
    }
  },
  /**
   * A state in which no track presents.
   */
  RETRACTED("retracted", false) {
    @Override
    ForceTrackEmitterState uncharged(ForceTrackEmitterBlockEntity emitter) {
      return this;
    }
  },
  /**
   * A state in which the track is in progress of building.
   */
  EXTENDING("extending", true) {
    @Override
    ForceTrackEmitterState charged(
        ForceTrackEmitterBlockEntity emitter) {
      if (!Charge.distribution.network(emitter.getLevel())
          .access(emitter.getBlockPos())
          .hasCapacity(
              ForceTrackEmitterBlockEntity.getMaintenanceCost(emitter.getTrackCount() + 1))) {
        return HALTED;
      }
      if (emitter.getTrackCount() >= MAX_TRACKS) {
        return EXTENDED;
      }
      if (emitter.clock(TICKS_PER_ACTION)) {
        Direction facing = emitter.getBlockState().getValue(ForceTrackEmitterBlock.FACING);
        BlockPos toPlace =
            emitter.getBlockPos().above().relative(facing, emitter.getTrackCount() + 1);
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
  RETRACTING("retracting", false) {
    @Override
    ForceTrackEmitterState uncharged(
        ForceTrackEmitterBlockEntity emitter) {
      if (emitter.getTrackCount() > 0) {
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
  HALTED("halted", false);

  private static final int TICKS_PER_ACTION = 2;
  private static final int TICKS_PER_REFRESH = 64;
  private static final int MAX_TRACKS = 64;

  private static final Map<String, ForceTrackEmitterState> byName = Arrays.stream(values())
      .collect(Collectors.toMap(ForceTrackEmitterState::getSerializedName, Function.identity()));

  private final String name;
  private final boolean visuallyPowered;

  private ForceTrackEmitterState(String name, boolean visuallyPowered) {
    this.name = name;
    this.visuallyPowered = visuallyPowered;
  }

  boolean isVisuallyPowered() {
    return this.visuallyPowered;
  }

  /**
   * Determines what state the emitter will be after using charge.
   *
   * @param emitter The emitter
   * @return The new state
   */
  ForceTrackEmitterState charged(ForceTrackEmitterBlockEntity emitter) {
    return EXTENDING;
  }

  /**
   * Determines what state the emitter will be if there is no charge available.
   *
   * @return The new state
   */
  ForceTrackEmitterState uncharged(ForceTrackEmitterBlockEntity emitter) {
    return RETRACTING;
  }

  void load(ForceTrackEmitterBlockEntity emitter) {}

  @Override
  public String getSerializedName() {
    return this.name;
  }

  public static Optional<ForceTrackEmitterState> getByName(String name) {
    return Optional.ofNullable(byName.get(name));
  }
}

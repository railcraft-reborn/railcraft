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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;

public enum ForceTrackEmitterState implements StringRepresentable {

  /**
   * A state when the track is fully built and ready for carts.
   */
  EXTENDED("extended", true, emitter -> new Instance() {

    private int ticks;

    @Override
    public Optional<ForceTrackEmitterState> charged() {
      return this.ticks++ >= TICKS_PER_REFRESH ? Optional.of(EXTENDING) : Optional.empty();
    }

    @Override
    public ForceTrackEmitterState getState() {
      return EXTENDED;
    }
  }) {

    @Override
    public Instance load(ForceTrackEmitterBlockEntity emitter) {
      var pos = emitter.getBlockPos().above();
      var blockState = emitter.getLevel().getBlockState(pos);
      if (blockState.getBlock() instanceof LockingTrack lockingTrack) {
        lockingTrack.releaseCart();
      }

      return super.load(emitter);
    }
  },
  /**
   * A state in which no track presents.
   */
  RETRACTED("retracted", false, emitter -> new Instance() {

    @Override
    public Optional<ForceTrackEmitterState> uncharged() {
      return Optional.empty();
    }

    @Override
    public ForceTrackEmitterState getState() {
      return RETRACTED;
    }
  }),
  /**
   * A state in which the track is in progress of building.
   */
  EXTENDING("extending", true, emitter -> new Instance() {

    private int ticks;

    @Override
    public Optional<ForceTrackEmitterState> charged() {
      if (!Charge.distribution
          .network((ServerLevel) emitter.getLevel())
          .access(emitter.getBlockPos())
          .hasCapacity(
              ForceTrackEmitterBlockEntity.getMaintenanceCost(emitter.getTrackCount() + 1))) {
        return Optional.of(HALTED);
      }
      if (emitter.getTrackCount() >= MAX_TRACKS) {
        return Optional.of(EXTENDED);
      }
      if (this.ticks++ >= TICKS_PER_ACTION) {
        this.ticks = 0;
        var facing = emitter.getBlockState().getValue(ForceTrackEmitterBlock.FACING);
        var toPlace = emitter.getBlockPos().above().relative(facing, emitter.getTrackCount() + 1);
        if (!emitter.getLevel().isLoaded(toPlace)) {
          return Optional.of(HALTED);
        }

        var blockState = emitter.getLevel().getBlockState(toPlace);
        var direction = TrackTools.getAxisAlignedDirection(facing);
        if (!emitter.placeTrack(toPlace, blockState, direction)) {
          return Optional.of(EXTENDED);
        }
      }
      return Optional.empty();
    }

    @Override
    public ForceTrackEmitterState getState() {
      return EXTENDING;
    }
  }),
  /**
   * A state in which the tracks are destroyed.
   */
  RETRACTING("retracting", false, emitter -> new Instance() {

    private int ticks;

    @Override
    public Optional<ForceTrackEmitterState> uncharged() {
      if (emitter.getTrackCount() == 0) {
        return Optional.of(RETRACTED);
      }

      if (this.ticks++ >= TICKS_PER_ACTION) {
        this.ticks = 0;
        emitter.removeFirstTrack();
      }
      return Optional.empty();
    }

    @Override
    public ForceTrackEmitterState getState() {
      return RETRACTING;
    }
  }),
  /**
   * A state in which the state will wait for a transition.
   */
  HALTED("halted", false, __ -> new Instance() {

    @Override
    public ForceTrackEmitterState getState() {
      return HALTED;
    }
  });

  private static final int TICKS_PER_ACTION = 2;
  private static final int TICKS_PER_REFRESH = 64;
  private static final int MAX_TRACKS = 64;

  private static final Map<String, ForceTrackEmitterState> byName = Arrays.stream(values())
      .collect(Collectors.toMap(ForceTrackEmitterState::getSerializedName, Function.identity()));

  private final String name;
  private final boolean visuallyPowered;
  private final Function<ForceTrackEmitterBlockEntity, Instance> factory;

  private ForceTrackEmitterState(String name, boolean visuallyPowered,
      Function<ForceTrackEmitterBlockEntity, Instance> factory) {
    this.name = name;
    this.visuallyPowered = visuallyPowered;
    this.factory = factory;
  }

  public boolean isVisuallyPowered() {
    return this.visuallyPowered;
  }

  public Instance load(ForceTrackEmitterBlockEntity emitter) {
    return this.factory.apply(emitter);
  }

  @Override
  public String getSerializedName() {
    return this.name;
  }

  public static Optional<ForceTrackEmitterState> getByName(String name) {
    return Optional.ofNullable(byName.get(name));
  }

  public interface Instance {

    /**
     * Determines what state the emitter will be after using charge.
     *
     * @param emitter The emitter
     * @return The new state
     */
    default Optional<ForceTrackEmitterState> charged() {
      return Optional.of(EXTENDING);
    }

    /**
     * Determines what state the emitter will be if there is no charge available.
     *
     * @return The new state
     */
    default Optional<ForceTrackEmitterState> uncharged() {
      return Optional.of(RETRACTING);
    }

    ForceTrackEmitterState getState();
  }
}

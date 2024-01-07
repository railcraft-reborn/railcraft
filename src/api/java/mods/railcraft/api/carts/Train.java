package mods.railcraft.api.carts;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

/**
 * @author Sm0keySa1m0n
 */
public interface Train {

  UUID id();

  UUID idMinecartFront();

  default RollingStock front(Level level) {
    var entity = level.getEntities().get(idMinecartFront());
    if (entity instanceof AbstractMinecart minecart) {
      return RollingStock.getOrThrow(minecart);
    }
    throw new IllegalStateException();
  }

  void copyTo(Train train);

  default Stream<RollingStock> stream(Level level) {
    return this.front(level).traverseTrainWithSelf(Side.BACK);
  }

  default Stream<? extends AbstractMinecart> entities(Level level) {
    return this.stream(level).map(RollingStock::entity);
  }

  default Stream<Entity> passengers(Level level) {
    return this.entities(level)
        .flatMap(minecart -> minecart.getPassengers().stream());
  }

  int getNumRunningLocomotives(Level level);

  default int size(Level level) {
    return (int) this.stream(level).count();
  }

  State state();

  void setState(State state);

  default void setStateIfHigherPriority(State state) {
    if (state.ordinal() < this.state().ordinal()) {
      this.setState(state);
    }
  }

  void addLock(UUID lockId);

  void removeLock(UUID lockId);

  boolean isLocked();

  default boolean isIdle() {
    return this.state() == State.IDLE || this.isLocked();
  }

  Optional<IItemHandler> itemHandler(Level level);

  Optional<IFluidHandler> fluidHandler(Level level);

  enum State implements StringRepresentable {

    STOPPED("stopped"), IDLE("idle"), NORMAL("normal");

    private static final Map<String, State> byName = Arrays.stream(values())
        .collect(Collectors.toUnmodifiableMap(State::getSerializedName, Function.identity()));

    private final String name;

    private State(String name) {
      this.name = name;
    }

    public boolean isStopped() {
      return this == STOPPED;
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }

    public static Optional<State> getByName(String name) {
      return Optional.ofNullable(byName.get(name));
    }
  }
}

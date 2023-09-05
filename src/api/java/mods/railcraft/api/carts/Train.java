package mods.railcraft.api.carts;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * @author Sm0keySa1m0n
 */
public interface Train extends Iterable<RollingStock> {

  UUID id();

  RollingStock front();

  void copyTo(Train train);

  default Stream<RollingStock> stream() {
    return this.front().traverseTrainWithSelf(Side.BACK);
  }

  default Stream<? extends AbstractMinecart> entities() {
    return this.stream().map(RollingStock::entity);
  }

  default Stream<Entity> passengers() {
    return this.entities()
        .flatMap(minecart -> minecart.getPassengers().stream());
  }

  @Override
  default Iterator<RollingStock> iterator() {
    return this.stream().iterator();
  }

  int getNumRunningLocomotives();

  default int size() {
    return (int) this.stream().count();
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

  Optional<IItemHandler> itemHandler();

  Optional<IFluidHandler> fluidHandler();

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

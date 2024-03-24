package mods.railcraft.api.carts;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

/**
 * @author Sm0keySa1m0n
 */
public interface Train {

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

    STOPPED("stopped"),
    IDLE("idle"),
    NORMAL("normal");

    private final String name;

    State(String name) {
      this.name = name;
    }

    public boolean isStopped() {
      return this == STOPPED;
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }

    public static Optional<State> fromName(String name) {
      return Arrays.stream(values()).filter(state -> state.name.equals(name)).findAny();
    }
  }
}

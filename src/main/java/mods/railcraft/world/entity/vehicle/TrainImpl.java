package mods.railcraft.world.entity.vehicle;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.carts.Train;
import mods.railcraft.util.fluids.CompositeFluidHandler;
import mods.railcraft.util.FunctionalUtil;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

/**
 * @author Sm0keySa1m0n
 */
final class TrainImpl implements Train {

  private final UUID id;
  private final RollingStockImpl front;
  private final Set<UUID> locks = new HashSet<>();
  private State state = State.NORMAL;

  static TrainImpl create(RollingStockImpl owner) {
    return new TrainImpl(UUID.randomUUID(), owner);
  }

  private TrainImpl(UUID id, RollingStockImpl front) {
    this.id = id;
    this.front = front;
  }

  @Override
  public UUID id() {
    return this.id;
  }

  @Override
  public State state() {
    return this.state;
  }

  @Override
  public void setState(State state) {
    if (this.state != state) {
      this.state = state;
    }
  }

  @Override
  public RollingStock front() {
    return this.front;
  }

  @Override
  public void copyTo(Train train) {
    this.locks.forEach(train::addLock);
    train.setStateIfHigherPriority(this.state);
  }

  @Override
  public int getNumRunningLocomotives() {
    return (int) this.entities()
        .flatMap(FunctionalUtil.ofType(Locomotive.class))
        .filter(Locomotive::isRunning)
        .count();
  }

  @Override
  public Optional<IItemHandler> itemHandler() {
    var cartHandlers = this.entities()
        .flatMap(cart -> cart.getCapability(ForgeCapabilities.ITEM_HANDLER)
            .map(Stream::of)
            .orElse(Stream.empty()))
        .flatMap(FunctionalUtil.ofType(IItemHandlerModifiable.class))
        .toArray(IItemHandlerModifiable[]::new);
    return cartHandlers.length == 0
        ? Optional.empty()
        : Optional.of(new CombinedInvWrapper(cartHandlers));
  }

  @Override
  public Optional<IFluidHandler> fluidHandler() {
    var cartHandlers = this.entities()
        .flatMap(cart -> cart.getCapability(ForgeCapabilities.FLUID_HANDLER)
            .map(Stream::of)
            .orElse(Stream.empty()))
        .toList();
    return cartHandlers.isEmpty()
        ? Optional.empty()
        : Optional.of(new CompositeFluidHandler(cartHandlers));
  }

  public void refreshMaxSpeed() {
    setMaxSpeed(calculateMaxSpeed());
  }

  private float calculateMaxSpeed() {
    double locoBoost = Math.max(0.0, this.getNumRunningLocomotives() - 1.0) * 0.075;
    return (float) this.entities()
        .mapToDouble(c -> Math.min(c.getMaxCartSpeedOnRail(), this.softMaxSpeed(c) + locoBoost))
        .min()
        .orElse(1.2F);
  }

  private float softMaxSpeed(AbstractMinecart cart) {
    return cart instanceof WeightedCart weighted
        ? weighted.softMaxSpeed()
        : cart.getMaxCartSpeedOnRail();
  }

  private void setMaxSpeed(float trainSpeed) {
    this.entities().forEach(c -> c.setCurrentCartSpeedCapOnRail(trainSpeed));
  }

  @Override
  public boolean isLocked() {
    return !this.locks.isEmpty();
  }

  @Override
  public void addLock(UUID lock) {
    this.locks.add(lock);
  }

  @Override
  public void removeLock(UUID lock) {
    this.locks.remove(lock);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof TrainImpl other)) {
      return false;
    }
    return id.equals(other.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return String.format("Train{id=%s,n=%d}", this.id, this.size());
  }

  static TrainImpl fromTag(CompoundTag tag, RollingStockImpl minecart) {
    var id = tag.getUUID("id");
    var train = new TrainImpl(id, minecart);
    State.fromName(tag.getString("state")).ifPresent(train::setState);
    tag.getList("locks", Tag.TAG_INT_ARRAY).stream()
        .map(NbtUtils::loadUUID)
        .forEach(train::addLock);
    return train;
  }

  CompoundTag toTag() {
    var tag = new CompoundTag();
    tag.putUUID("id", this.id);
    tag.putString("state", this.state.getSerializedName());
    var locksTag = new ListTag();
    for (var uuid : this.locks) {
      locksTag.add(NbtUtils.createUUID(uuid));
    }
    tag.put("locks", locksTag);
    return tag;
  }
}

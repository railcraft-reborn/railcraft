package mods.railcraft.world.entity.vehicle;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import mods.railcraft.api.carts.Train;
import mods.railcraft.util.FunctionalUtil;
import mods.railcraft.util.fluids.CompositeFluidHandler;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

/**
 * @author Sm0keySa1m0n
 */
public final class TrainImpl implements Train {


  private final UUID trainUUID;
  private final UUID minecartFrontUUID;
  private final Set<UUID> locks = new HashSet<>();
  private State state = State.NORMAL;

  public static TrainImpl create(UUID idMinecartFront) {
    return new TrainImpl(UUID.randomUUID(), idMinecartFront);
  }

  private TrainImpl(UUID trainUUID, UUID minecartFrontUUID) {
    this.trainUUID = trainUUID;
    this.minecartFrontUUID = minecartFrontUUID;
  }

  @Override
  public UUID id() {
    return this.trainUUID;
  }

  @Override
  public UUID idMinecartFront() {
    return this.minecartFrontUUID;
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
  public void copyTo(Train train) {
    this.locks.forEach(train::addLock);
    train.setStateIfHigherPriority(this.state);
  }

  @Override
  public int getNumRunningLocomotives(Level level) {
    return (int) this.entities(level)
        .flatMap(FunctionalUtil.ofType(Locomotive.class))
        .filter(Locomotive::isRunning)
        .count();
  }

  @Override
  public Optional<IItemHandler> itemHandler(Level level) {
    var cartHandlers = this.entities(level)
        .flatMap(cart ->
            Optional.ofNullable(cart.getCapability(Capabilities.ItemHandler.ENTITY)).stream())
        .flatMap(FunctionalUtil.ofType(IItemHandlerModifiable.class))
        .toArray(IItemHandlerModifiable[]::new);
    return cartHandlers.length == 0
        ? Optional.empty()
        : Optional.of(new CombinedInvWrapper(cartHandlers));
  }

  @Override
  public Optional<IFluidHandler> fluidHandler(Level level) {
    var cartHandlers = this.entities(level)
        .flatMap(cart -> Optional.ofNullable(
            cart.getCapability(Capabilities.FluidHandler.ENTITY, null)).stream())
        .toList();
    return cartHandlers.isEmpty()
        ? Optional.empty()
        : Optional.of(new CompositeFluidHandler(cartHandlers));
  }

  public void refreshMaxSpeed(Level level) {
    setMaxSpeed(level, calculateMaxSpeed(level));
  }

  private float calculateMaxSpeed(Level level) {
    double locoBoost = Math.max(0.0, this.getNumRunningLocomotives(level) - 1.0) * 0.075;
    return (float) this.entities(level)
        .mapToDouble(c -> Math.min(c.getMaxCartSpeedOnRail(), this.softMaxSpeed(c) + locoBoost))
        .min()
        .orElse(1.2F);
  }

  private float softMaxSpeed(AbstractMinecart cart) {
    return cart instanceof WeightedCart weighted
        ? weighted.softMaxSpeed()
        : cart.getMaxCartSpeedOnRail();
  }

  private void setMaxSpeed(Level level, float trainSpeed) {
    this.entities(level).forEach(c -> c.setCurrentCartSpeedCapOnRail(trainSpeed));
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
    return trainUUID.equals(other.trainUUID);
  }

  @Override
  public int hashCode() {
    return trainUUID.hashCode();
  }

  @Override
  public String toString() {
    return String.format("Train{id=%s}", this.trainUUID);
  }

  public static TrainImpl fromTag(CompoundTag tag) {
    var trainUUID = tag.getUUID("trainUUID");
    var idMinecartFront = tag.getUUID("idMinecartFront");
    var train = new TrainImpl(trainUUID, idMinecartFront);
    State.fromName(tag.getString("state")).ifPresent(train::setState);
    tag.getList("locks", Tag.TAG_INT_ARRAY).stream()
        .map(NbtUtils::loadUUID)
        .forEach(train::addLock);
    return train;
  }

  public CompoundTag toTag() {
    var tag = new CompoundTag();
    tag.putUUID("trainUUID", this.trainUUID);
    tag.putUUID("idMinecartFront", this.minecartFrontUUID);
    tag.putString("state", this.state.getSerializedName());
    var locksTag = new ListTag();
    for (var uuid : this.locks) {
      locksTag.add(NbtUtils.createUUID(uuid));
    }
    tag.put("locks", locksTag);
    return tag;
  }
}

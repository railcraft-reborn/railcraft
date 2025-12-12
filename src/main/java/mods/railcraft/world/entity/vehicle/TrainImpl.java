package mods.railcraft.world.entity.vehicle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.carts.Train;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.attachment.RailcraftAttachmentTypes;
import mods.railcraft.util.FunctionalUtil;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.CombinedResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;

/**
 * @author Sm0keySa1m0n
 */
public final class TrainImpl implements Train {

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
  public Optional<ResourceHandler<ItemResource>> itemHandler() {
    var cartHandlers = this.entities()
        .flatMap(cart -> Optional.ofNullable(cart.getCapability(Capabilities.Item.ENTITY))
            .stream())
        //.flatMap(FunctionalUtil.ofType(IndexModifier.class))
        .toList();
    return cartHandlers.isEmpty()
        ? Optional.empty()
        : Optional.of(new CombinedResourceHandler<>(cartHandlers));
  }

  @Override
  public Optional<ResourceHandler<FluidResource>> fluidHandler() {
    var cartHandlers = this.entities()
        .flatMap(cart -> Optional.ofNullable(
            cart.getCapability(Capabilities.Fluid.ENTITY, null)).stream())
        .toList();
    return cartHandlers.isEmpty()
        ? Optional.empty()
        : Optional.of(new CombinedResourceHandler<>(cartHandlers));
  }

  public void refreshMaxSpeed() {
    this.setMaxSpeed(this.calculateMaxSpeed());
  }

  private float calculateMaxSpeed() {
    double locoBoost = Math.max(0.0, this.getNumRunningLocomotives() - 1.0) * 0.075;
    return (float) this.entities()
        .mapToDouble(c -> {
          var maxCartSpeedOnRail = c.getData(RailcraftAttachmentTypes.MAX_CART_SPEED_ON_RAIL);
          return Math.min(maxCartSpeedOnRail, this.softMaxSpeed(c) + locoBoost);
        })
        .min()
        .orElse(1.2F);
  }

  private float softMaxSpeed(AbstractMinecart cart) {
    return cart instanceof WeightedCart weighted
        ? weighted.softMaxSpeed()
        : cart.getData(RailcraftAttachmentTypes.MAX_CART_SPEED_ON_RAIL);
  }

  private void setMaxSpeed(float trainSpeed) {
    this.entities().forEach(c ->
        c.setData(RailcraftAttachmentTypes.CURRENT_SPEED_CAP_ON_RAIL, trainSpeed));
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
    return this.id.equals(other.id);
  }

  @Override
  public int hashCode() {
    return this.id.hashCode();
  }

  @Override
  public String toString() {
    return String.format("Train{id=%s}", this.id);
  }

  static TrainImpl deserialize(ValueInput input, RollingStockImpl minecart) {
    var id = input.read(CompoundTagKeys.ID, UUIDUtil.CODEC).orElseThrow();
    var train = new TrainImpl(id, minecart);
    input.read(CompoundTagKeys.STATE, State.CODEC).ifPresent(train::setState);
    input.read(CompoundTagKeys.LOCKS, UUIDUtil.CODEC.listOf())
        .ifPresent(train.locks::addAll);
    return train;
  }

  void serialize(ValueOutput valueOutput) {
    valueOutput.store(CompoundTagKeys.ID, UUIDUtil.CODEC, this.id);
    valueOutput.store(CompoundTagKeys.STATE, State.CODEC, this.state);
    valueOutput.store(CompoundTagKeys.LOCKS, UUIDUtil.CODEC.listOf(), new ArrayList<>(this.locks));
  }
}

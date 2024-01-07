package mods.railcraft.charge;

import java.util.Optional;
import java.util.Random;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.ChargeCartStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;

public class ChargeCartStorageImpl extends EnergyStorage implements ChargeCartStorage {

  protected static final Random RANDOM = new Random();
  private static final int DRAW_INTERVAL = 8;
  protected final int lossPerTick;
  protected double draw, chargeDrawnThisTick;
  protected int drewFromTrack, clock = RANDOM.nextInt(0, DRAW_INTERVAL);

  public ChargeCartStorageImpl(int capacity) {
    this(capacity, 0);
  }

  public ChargeCartStorageImpl(int capacity, int lossPerTick) {
    super(capacity);
    this.lossPerTick = lossPerTick;
  }

  @Override
  public double getLosses() {
    return this.lossPerTick;
  }

  @Override
  public double getDraw() {
    return this.draw;
  }

  protected void removeLosses() {
    if (lossPerTick > 0) {
      if (energy >= lossPerTick) {
        energy -= lossPerTick;
      } else {
        energy = 0;
      }
    }
  }

  @Override
  public void tick(AbstractMinecart owner) {
    if (!(owner.level() instanceof ServerLevel serverLevel)) {
      return;
    }
    clock++;
    removeLosses();

    this.draw = (this.draw * 24.0 + this.chargeDrawnThisTick) / 25.0;
    this.chargeDrawnThisTick = 0;

    if (drewFromTrack > 0) {
      drewFromTrack--;
    } else if (energy < (capacity * 0.5) && clock % DRAW_INTERVAL == 0) {
      RollingStock.getOrThrow(owner).train().entities(serverLevel)
          .flatMap(c -> Optional.ofNullable(
              c.getCapability(Capabilities.EnergyStorage.ENTITY, null)).stream())
          .findAny()
          .ifPresent(
              energyStorage -> energy += energyStorage.extractEnergy(capacity - energy, false));
    }
  }

  @Override
  public void tickOnTrack(AbstractMinecart owner, BlockPos pos) {
    if (!owner.level().isClientSide() && needsCharging()) {
      int drawnFromTrack = Charge.distribution
          .network((ServerLevel) owner.level())
          .access(pos)
          .removeCharge(capacity - energy, false);
      if (drawnFromTrack > 0) {
        drewFromTrack = DRAW_INTERVAL * 4;
      }
      energy += drawnFromTrack;
    }
  }

  @Override
  public int extractEnergy(int maxExtract, boolean simulate) {
    int extracted = super.extractEnergy(maxExtract, simulate);
    if (!simulate) {
      this.chargeDrawnThisTick += extracted;
    }
    return extracted;
  }

  private boolean needsCharging() {
    return this.energy < this.capacity;
  }
}

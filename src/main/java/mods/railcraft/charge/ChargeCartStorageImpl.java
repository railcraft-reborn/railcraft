package mods.railcraft.charge;

import java.util.Random;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.ChargeCartStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.EnergyStorage;

public class ChargeCartStorageImpl extends EnergyStorage implements ChargeCartStorage {

  protected static final Random RANDOM = new Random();
  private static final int DRAW_INTERVAL = 8;
  protected final float lossPerTick;
  protected double draw;
  protected int clock = RANDOM.nextInt();
  protected int drewFromTrack;

  public ChargeCartStorageImpl() {
    this(5000, 0);
  }

  public ChargeCartStorageImpl(int capacity) {
    this(capacity, 0);
  }

  public ChargeCartStorageImpl(int capacity, float lossPerTick) {
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
    if (owner.level().isClientSide) {
      return;
    }
    clock++;
    removeLosses();

    draw = (draw * 24.0) / 25.0;

    if (drewFromTrack > 0) {
      drewFromTrack--;
    } else if (energy < (capacity * 0.5) && clock % DRAW_INTERVAL == 0) {
      RollingStock.getOrThrow(owner)
          .train()
          .stream()
          .map(RollingStock::entity)
          .map(c -> c.getCapability(ForgeCapabilities.ENERGY))
          .findAny()
          .ifPresent(c ->
              c.ifPresent(energyStorage ->
                  energy += energyStorage.extractEnergy(capacity - energy, false)
              ));
    }
  }

  @Override
  public void tickOnTrack(AbstractMinecart owner, BlockPos pos) {
    if (!owner.level().isClientSide && needsCharging()) {
      double drawnFromTrack = Charge.distribution
          .network((ServerLevel) owner.level())
          .access(pos)
          .removeCharge(capacity - energy, false);
      if (drawnFromTrack > 0.0) {
        drewFromTrack = DRAW_INTERVAL * 4;
      }
      energy += drawnFromTrack;
    }
  }

  private boolean needsCharging() {
    return this.energy < this.capacity;
  }
}

package mods.railcraft.battery;

import mods.railcraft.api.charge.ChargeStorage;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * Implementation of {@link mods.railcraft.api.charge.ChargeStorage}.
 *
 * <p>IMPORTANT CHANGE: WE DO NOT USE FLOATS ANYMORE FOR ENERGY UNITS!
 *
 * <p>Created by CovertJaguar on 1/15/2019 for Railcraft.
 *
 * @author CovertJaguar (https://www.railcraft.info)
 */
public class SimpleBattery implements ChargeStorage, IEnergyStorage {

  protected int charge;
  protected final int capacity;
  protected int chargeDrawnThisTick;

  public SimpleBattery(int capacity) {
    this(capacity, 0);
  }

  public SimpleBattery(int capacity, int charge) {
    this.capacity = capacity;
    this.charge = Math.max(0, Math.min(capacity, charge));
  }

  public int addCharge(int charge) {
    return this.addCharge(charge, false);
  }

  @Override
  public int addCharge(int charge, boolean simulate) {
    int chargedRecive = charge;
    if (!simulate) {
      this.charge += charge;
    }
    return chargedRecive;
  }

  @Override
  public void setCharge(int charge) {
    this.charge = charge;
  }

  public int removeCharge(int charge) {
    return this.removeCharge(charge, false);
  }

  @Override
  public int removeCharge(int charge, boolean simulate) {
    int amountToDraw = Math.min(charge, this.getCharge());
    if (!simulate) {
      this.charge -= amountToDraw / this.getEfficiency();
      this.chargeDrawnThisTick += amountToDraw;
    }

    return amountToDraw;
  }

  @Override
  public int getCharge() {
    return this.charge;
  }

  @Override
  public int getCapacity() {
    return this.capacity;
  }

  @Override
  public int receiveEnergy(int maxReceive, boolean simulate) {
    int energyReceived = Math.min(capacity - charge, maxReceive);
    if (!simulate) {
      charge += energyReceived;
    }
    return (energyReceived);
  }

  @Override
  public int extractEnergy(int maxExtract, boolean simulate) {
    int energyExtracted = Math.min(charge, maxExtract);
    if (!simulate) {
      charge -= energyExtracted;
    }
    return energyExtracted;
  }

  @Override
  public int getEnergyStored() {
    return charge;
  }

  @Override
  public int getMaxEnergyStored() {
    return capacity;
  }

  @Override
  public boolean canExtract() {
    return true;
  }

  @Override
  public boolean canReceive() {
    return true;
  }
}

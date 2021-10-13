package mods.railcraft.battery;

import net.minecraftforge.energy.IEnergyStorage;

/**
 * A semi-direct way of converting RF to charge. It acts as a battery block.
 */
public class ForgeChargeBattery extends SimpleBattery implements IEnergyStorage {

  private static final float EU_RF_RATIO = 0.25F; // 1/4.

  public ForgeChargeBattery(float capacity) {
    this(capacity, 0);
  }

  public ForgeChargeBattery(float capacity, float charge) {
    super(capacity, charge);
  }

  @Override
  public int receiveEnergy(int maxReceive, boolean simulate) {
    float energyReceived = Math.min(capacity - charge, maxReceive * 4F);
    if (!simulate) {
      charge += energyReceived;
    }
    return (int)(energyReceived * EU_RF_RATIO);
  }

  @Override
  public int extractEnergy(int maxExtract, boolean simulate) {
    float energyExtracted = Math.min(charge, (float)maxExtract * EU_RF_RATIO);
    if (!simulate) {
      charge -= energyExtracted;
    }
    return (int)energyExtracted * 4;
  }

  @Override
  public int getEnergyStored() {
    return (int)(charge * EU_RF_RATIO);
  }

  @Override
  public int getMaxEnergyStored() {
    return (int)(capacity * EU_RF_RATIO);
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

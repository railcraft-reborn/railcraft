package mods.railcraft.util;

import net.minecraftforge.energy.EnergyStorage;

public class MachineEnergyStorage extends EnergyStorage {

  public MachineEnergyStorage(int capacity, int maxReceive) {
    super(capacity, maxReceive, 0);
  }

  public int consumeEnergyInternally(int maxExtract) {
    int energyExtracted = Math.min(energy, maxExtract);
    energy -= energyExtracted;
    return energyExtracted;
  }
}

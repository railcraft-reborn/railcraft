package mods.railcraft.util;

import java.util.function.Supplier;
import net.minecraftforge.energy.IEnergyStorage;

public class ForwardingEnergyStorage implements IEnergyStorage {

  private final Supplier<IEnergyStorage> delegate;

  public ForwardingEnergyStorage(Supplier<IEnergyStorage> delegate) {
    this.delegate = delegate;
  }

  @Override
  public int receiveEnergy(int maxReceive, boolean simulate) {
    return this.delegate.get().receiveEnergy(maxReceive, simulate);
  }

  @Override
  public int extractEnergy(int maxExtract, boolean simulate) {
    return this.delegate.get().extractEnergy(maxExtract, simulate);
  }

  @Override
  public int getEnergyStored() {
    return this.delegate.get().getEnergyStored();
  }

  @Override
  public int getMaxEnergyStored() {
    return this.delegate.get().getMaxEnergyStored();
  }

  @Override
  public boolean canExtract() {
    return this.delegate.get().canExtract();
  }

  @Override
  public boolean canReceive() {
    return this.delegate.get().canReceive();
  }
}

package mods.railcraft.util;

import java.util.Optional;
import java.util.function.Supplier;
import mods.railcraft.api.charge.ChargeStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class ForwardingEnergyStorage implements IEnergyStorage {

  private final Supplier<Optional<? extends ChargeStorage>> delegate;

  public ForwardingEnergyStorage(Supplier<Optional<? extends ChargeStorage>> delegate) {
    this.delegate = delegate;
  }

  @Override
  public int receiveEnergy(int maxReceive, boolean simulate) {
    return this.delegate.get().map(x -> x.receiveEnergy(maxReceive, simulate)).orElse(0);
  }

  @Override
  public int extractEnergy(int maxExtract, boolean simulate) {
    return this.delegate.get().map(x -> x.extractEnergy(maxExtract, simulate)).orElse(0);
  }

  @Override
  public int getEnergyStored() {
    return this.delegate.get().map(IEnergyStorage::getEnergyStored).orElse(0);
  }

  @Override
  public int getMaxEnergyStored() {
    return this.delegate.get().map(IEnergyStorage::getMaxEnergyStored).orElse(0);
  }

  @Override
  public boolean canExtract() {
    return this.delegate.get().map(IEnergyStorage::canExtract).orElse(false);
  }

  @Override
  public boolean canReceive() {
    return this.delegate.get().map(IEnergyStorage::canReceive).orElse(false);
  }
}

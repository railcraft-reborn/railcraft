package mods.railcraft.util;

import java.util.Optional;
import java.util.function.Supplier;
import mods.railcraft.api.charge.ChargeStorage;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class ForwardingEnergyStorage implements EnergyHandler {

  private final Supplier<Optional<? extends ChargeStorage>> delegate;

  public ForwardingEnergyStorage(Supplier<Optional<? extends ChargeStorage>> delegate) {
    this.delegate = delegate;
  }

  @Override
  public int insert(int amount, TransactionContext transaction) {
    return this.delegate.get().map(x -> x.insert(amount, transaction)).orElse(0);
  }

  @Override
  public int extract(int amount, TransactionContext transaction) {
    return this.delegate.get().map(x -> x.extract(amount, transaction)).orElse(0);
  }

  @Override
  public long getAmountAsLong() {
    return this.delegate.get().map(EnergyHandler::getAmountAsLong).orElse(0L);
  }

  @Override
  public long getCapacityAsLong() {
    return this.delegate.get().map(EnergyHandler::getCapacityAsLong).orElse(0L);
  }
}

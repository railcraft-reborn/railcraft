package mods.railcraft.world.module;

import java.util.Optional;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.ChargeStorage;
import mods.railcraft.util.ForwardingEnergyStorage;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class ChargeModule<T extends BlockModuleProvider> extends BaseModule<T> {

  private final Charge network;

  private final IEnergyStorage energyStorage = new ForwardingEnergyStorage(this::storage);

  public ChargeModule(T provider, Charge network) {
    super(provider);
    this.network = network;
  }

  public IEnergyStorage getEnergyStorage() {
    return this.energyStorage;
  }

  public Optional<? extends ChargeStorage> storage() {
    return this.provider.level().isClientSide() ? Optional.empty() : this.access().storage();
  }

  public Charge.Access access() {
    return this.network
        .network((ServerLevel) this.provider.level())
        .access(this.provider.blockPos());
  }

  @Override
  public void serverTick() {}
}

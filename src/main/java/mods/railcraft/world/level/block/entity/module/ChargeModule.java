package mods.railcraft.world.level.block.entity.module;

import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.ChargeStorage;
import mods.railcraft.util.ForwardingEnergyStorage;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class ChargeModule extends BaseModule {

  private final Charge network;

  private final LazyOptional<IEnergyStorage> energyStorage =
      LazyOptional.of(() -> new ForwardingEnergyStorage(this::storage));

  public ChargeModule(ModuleProvider provider, Charge network) {
    super(provider);
    this.network = network;
  }

  public LazyOptional<IEnergyStorage> getEnergyStorage() {
    return this.energyStorage;
  }

  public ChargeStorage storage() {
    return this.access().storage().get();
  }

  public Charge.Access access() {
    return this.network
        .network((ServerLevel) this.provider.getLevel())
        .access(this.provider.getBlockPos());
  }

  @Override
  public void serverTick() {}
}

package mods.railcraft.world.level.block.entity.module;

import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.ChargeStorageBlock;
import mods.railcraft.util.ForwardingEnergyStorage;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class ChargeModule extends BaseModule implements ICapabilityProvider {

  private final Charge network;

  private final LazyOptional<IEnergyStorage> capability =
      LazyOptional.of(() -> new ForwardingEnergyStorage(this::storage));

  public ChargeModule(ModuleProvider provider, Charge network) {
    super(provider);
    this.network = network;
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    return cap == CapabilityEnergy.ENERGY ? this.capability.cast() : LazyOptional.empty();
  }

  public ChargeStorageBlock storage() {
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

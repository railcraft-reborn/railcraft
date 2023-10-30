package mods.railcraft.world.level.block.entity.charge;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.ChargeStorage;
import mods.railcraft.util.ForwardingEnergyStorage;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class BatteryBlockEntity extends RailcraftBlockEntity {

  private final LazyOptional<IEnergyStorage> energyHandler;

  public BatteryBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.BATTERY.get(), blockPos, blockState);
    this.energyHandler = LazyOptional.of(() -> new ForwardingEnergyStorage(this::storage));
  }

  private Optional<? extends ChargeStorage> storage() {
    if (this.level().isClientSide()) {
      return Optional.empty();
    }
    return Charge.distribution
        .network((ServerLevel) this.level)
        .access(this.blockPos())
        .storage();
  }

  @Override
  @NotNull
  public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    return cap == Capabilities.ENERGY
        ? this.energyHandler.cast()
        : super.getCapability(cap, side);
  }

  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    this.energyHandler.invalidate();
  }
}

package mods.railcraft.world.level.block.entity.charge;

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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class BatteryBlockEntity extends RailcraftBlockEntity {

  private final LazyOptional<IEnergyStorage> energyHandler;

  public BatteryBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.BATTERY.get(), blockPos, blockState);
    this.energyHandler = LazyOptional.of(() -> new ForwardingEnergyStorage(this::storage));
  }

  private ChargeStorage storage() {
    return Charge.distribution
        .network((ServerLevel) this.level)
        .access(this.blockPos())
        .storage()
        .get();
  }

  @Override
  @NotNull
  public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    return cap == ForgeCapabilities.ENERGY
        ? this.energyHandler.cast()
        : super.getCapability(cap, side);
  }

  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    this.energyHandler.invalidate();
  }
}

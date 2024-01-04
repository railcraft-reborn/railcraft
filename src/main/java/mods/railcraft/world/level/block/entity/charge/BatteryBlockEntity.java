package mods.railcraft.world.level.block.entity.charge;

import java.util.Optional;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.ChargeStorage;
import mods.railcraft.util.ForwardingEnergyStorage;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class BatteryBlockEntity extends RailcraftBlockEntity {

  public BatteryBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.BATTERY.get(), blockPos, blockState);
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

  public IEnergyStorage getEnergyCap(Direction side) {
    return new ForwardingEnergyStorage(this::storage);
  }
}

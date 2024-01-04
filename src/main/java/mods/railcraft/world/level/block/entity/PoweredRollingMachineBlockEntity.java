package mods.railcraft.world.level.block.entity;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.ChargeStorage;
import mods.railcraft.util.ForwardingEnergyStorage;
import mods.railcraft.util.container.CombinedInvWrapper;
import mods.railcraft.world.inventory.PoweredRollingMachineMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;

public class PoweredRollingMachineBlockEntity extends ManualRollingMachineBlockEntity {

  private static final int CHARGE_PER_TICK = 10;
  private final IItemHandler itemHandler;
  private final IEnergyStorage energyHandler;

  public PoweredRollingMachineBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.POWERED_ROLLING_MACHINE.get(), blockPos, blockState);
    this.itemHandler = new CombinedInvWrapper(this.craftMatrix, this.invResult);
    this.energyHandler = new ForwardingEnergyStorage(this::storage);
  }

  @Override
  protected void progress() {
    if (access().useCharge(CHARGE_PER_TICK, false)) {
      super.progress();
    }
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
    return new PoweredRollingMachineMenu(containerId, inventory, this);
  }

  private Optional<? extends ChargeStorage> storage() {
    return this.level().isClientSide() ? Optional.empty() : this.access().storage();
  }

  private Charge.Access access() {
    return Charge.distribution
        .network((ServerLevel) this.level)
        .access(this.blockPos());
  }

  public IItemHandler getItemCap(Direction side) {
    return this.itemHandler;
  }

  public IEnergyStorage getEnergyCap(Direction side) {
    return this.energyHandler;
  }
}

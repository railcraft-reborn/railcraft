package mods.railcraft.world.level.block.entity;

import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.ChargeStorage;
import mods.railcraft.util.ForwardingEnergyStorage;
import mods.railcraft.world.inventory.PoweredRollingMachineMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class PoweredRollingMachineBlockEntity extends ManualRollingMachineBlockEntity {

  private static final int CHARGE_PER_TICK = 10;
  private final LazyOptional<IEnergyStorage> energyHandler;
  public PoweredRollingMachineBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.POWERED_ROLLING_MACHINE.get(), blockPos, blockState);
    this.energyHandler = LazyOptional.of(() -> new ForwardingEnergyStorage(this::storage));
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

  private ChargeStorage storage() {
    return this.access().storage().get();
  }

  private Charge.Access access() {
    return Charge.distribution
        .network((ServerLevel) this.level)
        .access(this.blockPos());
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    return cap == ForgeCapabilities.ENERGY
        ? this.energyHandler.cast()
        : super.getCapability(cap, side);
  }
}

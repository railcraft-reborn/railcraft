package mods.railcraft.world.level.block.entity;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.ChargeStorage;
import mods.railcraft.util.ForwardingEnergyStorage;
import mods.railcraft.util.container.FilteredInvWrapper;
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
import net.minecraftforge.items.IItemHandler;

public class PoweredRollingMachineBlockEntity extends ManualRollingMachineBlockEntity {

  private static final int CHARGE_PER_TICK = 10;
  private final LazyOptional<IItemHandler> inputHandler, outputHandler;
  private final LazyOptional<IEnergyStorage> energyHandler;
  public PoweredRollingMachineBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.POWERED_ROLLING_MACHINE.get(), blockPos, blockState);
    inputHandler = LazyOptional.of(() -> new FilteredInvWrapper(this.craftMatrix, true, false));
    outputHandler = LazyOptional.of(() -> new FilteredInvWrapper(this.invResult, false, true));
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

  private Optional<? extends ChargeStorage> storage() {
    return this.level().isClientSide() ? Optional.empty() : this.access().storage();
  }

  private Charge.Access access() {
    return Charge.distribution
        .network((ServerLevel) this.level)
        .access(this.blockPos());
  }

  @Override
  @NotNull
  public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
    if (cap == ForgeCapabilities.ENERGY) {
      return this.energyHandler.cast();
    }
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
      return side.equals(Direction.UP)
          ? this.inputHandler.cast()
          : this.outputHandler.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    this.energyHandler.invalidate();
    this.inputHandler.invalidate();
    this.outputHandler.invalidate();
  }
}

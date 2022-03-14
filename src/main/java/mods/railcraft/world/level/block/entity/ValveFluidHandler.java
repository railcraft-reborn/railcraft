package mods.railcraft.world.level.block.entity;

import mods.railcraft.world.level.block.entity.multiblock.TankBlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class ValveFluidHandler implements IFluidHandler {

  private final TankBlockEntity blockEntity;
  private final TankBlockEntity master;

  public ValveFluidHandler(TankBlockEntity blockEntity, TankBlockEntity master) {
    this.blockEntity = blockEntity;
    this.master = master;
  }

  private IFluidHandler getDelegate() {
    return this.master.getModule().getTank();
  }

  private boolean isFillable() {
    return this.blockEntity.getBlockPos().getY() - this.master.getBlockPos().getY() > 0;
  }

  private boolean isDrainable() {
    return this.blockEntity.getBlockPos().getY() - this.master.getBlockPos().getY() <= 1;
  }

  @Override
  public int getTanks() {
    return this.getDelegate().getTanks();
  }

  @Override
  public FluidStack getFluidInTank(int tank) {
    return this.getDelegate().getFluidInTank(tank);
  }

  @Override
  public int getTankCapacity(int tank) {
    return this.getDelegate().getTankCapacity(tank);
  }

  @Override
  public boolean isFluidValid(int tank, FluidStack stack) {
    return this.getDelegate().isFluidValid(tank, stack);
  }

  @Override
  public int fill(FluidStack resource, FluidAction action) {
    if (!this.isFillable()) {
      return 0;
    }
    return this.getDelegate().fill(resource, action);
  }

  @Override
  public FluidStack drain(FluidStack resource, FluidAction action) {
    if (!this.isDrainable()) {
      return FluidStack.EMPTY;
    }
    return this.getDelegate().drain(resource, action);
  }

  @Override
  public FluidStack drain(int maxDrain, FluidAction action) {
    if (!this.isDrainable()) {
      return FluidStack.EMPTY;
    }
    return this.getDelegate().drain(maxDrain, action);
  }
}

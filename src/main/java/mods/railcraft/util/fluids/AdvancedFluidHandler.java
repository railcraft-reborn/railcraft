package mods.railcraft.util.fluids;

import java.util.stream.IntStream;
import org.jetbrains.annotations.NotNull;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class AdvancedFluidHandler implements IFluidHandler {

  private final IFluidHandler fluidHandler;

  public AdvancedFluidHandler(IFluidHandler fluidHandler) {
    this.fluidHandler = fluidHandler;
  }

  @Override
  public int getTanks() {
    return this.fluidHandler.getTanks();
  }

  @Override
  @NotNull
  public FluidStack getFluidInTank(int i) {
    return this.fluidHandler.getFluidInTank(i);
  }

  @Override
  public int getTankCapacity(int i) {
    return this.fluidHandler.getTankCapacity(i);
  }

  @Override
  public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
    return this.fluidHandler.isFluidValid(i, fluidStack);
  }

  @Override
  public int fill(FluidStack fluidStack, FluidAction fluidAction) {
    return this.fluidHandler.fill(fluidStack, fluidAction);
  }

  @Override
  @NotNull
  public FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
    return this.fluidHandler.drain(fluidStack, fluidAction);
  }

  @Override
  @NotNull
  public FluidStack drain(int i, FluidAction fluidAction) {
    return this.fluidHandler.drain(i, fluidAction);
  }

  public int getFluidQty(FluidStack fluid) {
    if (fluid.isEmpty()) {
      return 0;
    }
    return IntStream.range(0, this.getTanks())
        .mapToObj(this::getFluidInTank)
        .filter(fluidStack -> fluidStack.isFluidEqual(fluid))
        .mapToInt(FluidStack::getAmount)
        .sum();
  }

  public boolean isTankEmpty(FluidStack fluid) {
    if (fluid.isEmpty()) {
      return this.areTanksEmpty();
    }
    return this.getFluidQty(fluid) <= 0;
  }

  public boolean isTankFull(FluidStack fluid) {
    if (fluid.isEmpty()) {
      return this.areTanksFull();
    }
    int fill = this.fill(new FluidStack(fluid, 1), FluidAction.SIMULATE);
    return fill <= 0;
  }

  public boolean areTanksFull() {
    for (int i = 0; i < this.getTanks(); i++) {
      var tank = this.getFluidInTank(i);
      if (tank.isEmpty() || tank.getAmount() < this.getTankCapacity(i)) {
        return false;
      }
    }
    return true;
  }

  public boolean areTanksEmpty() {
    return !this.isFluidInTank();
  }

  public boolean isFluidInTank() {
    for (int i = 0; i < this.getTanks(); i++) {
      var tank = this.getFluidInTank(i);
      boolean empty = tank.isEmpty() || tank.getAmount() <= 0;
      if (!empty) {
        return true;
      }
    }
    return false;
  }

  public float getFluidLevel() {
    int amount = 0;
    int capacity = 0;
    for (int i = 0; i < this.getTanks(); i++) {
      var liquid = this.getFluidInTank(i);
      amount += liquid.isEmpty() ? 0 : liquid.getAmount();
      capacity += this.getTankCapacity(i);
    }
    return capacity == 0 ? 0 : ((float) amount) / capacity;
  }

  public float getFluidLevel(FluidStack fluid) {
    int amount = 0;
    int capacity = 0;
    for (int i = 0; i < this.getTanks(); i++) {
      var liquid = this.getFluidInTank(i);
      if (liquid.isEmpty() || !liquid.isFluidEqual(fluid))
        continue;
      amount += liquid.getAmount();
      capacity += this.getTankCapacity(i);
    }
    return capacity == 0 ? 0 : amount / (float) capacity;
  }

  public boolean canPutFluid(FluidStack fluid) {
    return !fluid.isEmpty() && fill(fluid, FluidAction.SIMULATE) > 0;
  }
}

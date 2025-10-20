package mods.railcraft.util.fluids;

import java.util.stream.IntStream;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class AdvancedFluidHandler implements ResourceHandler<FluidResource> {

  private final ResourceHandler<FluidResource> fluidHandler;

  public AdvancedFluidHandler(ResourceHandler<FluidResource> fluidHandler) {
    this.fluidHandler = fluidHandler;
  }

  @Override
  public int size() {
    return fluidHandler.size();
  }

  @Override
  public FluidResource getResource(int index) {
    return fluidHandler.getResource(index);
  }

  @Override
  public long getAmountAsLong(int index) {
    return fluidHandler.getAmountAsLong(index);
  }

  @Override
  public long getCapacityAsLong(int index, FluidResource resource) {
    return fluidHandler.getCapacityAsLong(index, resource);
  }

  @Override
  public boolean isValid(int index, FluidResource resource) {
    return fluidHandler.isValid(index, resource);
  }

  @Override
  public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
    return fluidHandler.insert(index, resource, amount, transaction);
  }

  @Override
  public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
    return fluidHandler.extract(index, resource, amount, transaction);
  }

  @Override
  public int extract(FluidResource resource, int amount, TransactionContext transaction) {
    return fluidHandler.extract(resource, amount, transaction);
  }

  public int getFluidQty(FluidStack fluid) {
    if (fluid.isEmpty()) {
      return 0;
    }
    return IntStream.range(0, this.size())
        .mapToObj(i -> FluidUtil.getStack(this, i))
        .filter(fluidStack -> FluidStack.isSameFluidSameComponents(fluidStack, fluid))
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

    try (var transaction = Transaction.openRoot()) {
      int filled = this.insert(FluidResource.of(fluid), 1, transaction);
      return filled <= 0;
    }
  }

  public boolean areTanksFull() {
    for (int i = 0; i < this.size(); i++) {
      var tank = FluidUtil.getStack(this, i);
      if (tank.isEmpty() || tank.getAmount() < this.getCapacityAsInt(i, FluidResource.EMPTY)) {
        return false;
      }
    }
    return true;
  }

  public boolean areTanksEmpty() {
    return !this.isFluidInTank();
  }

  public boolean isFluidInTank() {
    for (int i = 0; i < this.size(); i++) {
      var tank = FluidUtil.getStack(this, i);
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
    for (int i = 0; i < this.size(); i++) {
      var liquid = FluidUtil.getStack(this, i);
      amount += liquid.isEmpty() ? 0 : liquid.getAmount();
      capacity += this.getCapacityAsInt(i, FluidResource.EMPTY);
    }
    return capacity == 0 ? 0 : ((float) amount) / capacity;
  }

  public float getFluidLevel(FluidStack fluid) {
    int amount = 0;
    int capacity = 0;
    for (int i = 0; i < this.size(); i++) {
      var liquid = FluidUtil.getStack(this, i);
      if (liquid.isEmpty() || !FluidStack.isSameFluidSameComponents(liquid, fluid))
        continue;
      amount += liquid.getAmount();
      capacity += this.getCapacityAsInt(i, FluidResource.EMPTY);
    }
    return capacity == 0 ? 0 : amount / (float) capacity;
  }

  public boolean canPutFluid(FluidResource fluidResource, int amount) {
    if (amount <= 0) {
      return false;
    }
    try (var transaction = Transaction.openRoot()) {
      return this.insert(fluidResource, amount, transaction) > 0;
    }
  }
}

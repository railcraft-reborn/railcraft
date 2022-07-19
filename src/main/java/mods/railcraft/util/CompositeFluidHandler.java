package mods.railcraft.util;

import java.util.Arrays;
import java.util.Collection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;

import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class CompositeFluidHandler implements IFluidHandler {

  protected final IFluidHandler[] fluidHandlers;
  protected final int[] baseIndex; // index-offsets of the different handlers
  protected final int slotCount; // number of total slots

  public CompositeFluidHandler(IFluidHandler... subHandlers) {
    this.fluidHandlers = subHandlers;
    this.baseIndex = new int[subHandlers.length];
    int index = 0;
    for (int i = 0; i < subHandlers.length; i++) {
      index += subHandlers[i].getTanks();
      this.baseIndex[i] = index;
    }
    this.slotCount = index;
  }

  public CompositeFluidHandler(Collection<IFluidHandler> subHandlers) {
    this(subHandlers.toArray(new IFluidHandler[subHandlers.size()]));
  }

  // returns the handler index for the tank
  protected int getIndexForTank(int tank) {
    if (tank < 0) {
      return -1;
    }

    for (int i = 0; i < this.baseIndex.length; i++) {
      if (tank - this.baseIndex[i] < 0) {
        return i;
      }
    }
    return -1;
  }

  protected IFluidHandler getHandlerFromIndex(int index) {
    return index < 0 || index >= this.fluidHandlers.length
        ? EmptyFluidHandler.INSTANCE
        : this.fluidHandlers[index];
  }

  protected int getSlotFromIndex(int slot, int index) {
    return index <= 0 || index >= this.baseIndex.length ? slot : slot - this.baseIndex[index - 1];
  }

  @Override
  public int fill(FluidStack fluid, FluidAction action) {
    if (fluid.isEmpty() || fluid.getAmount() <= 0) {
      return 0;
    }

    fluid = fluid.copy();

    int totalFillAmount = 0;
    for (IFluidHandler handler : this.fluidHandlers) {
      int fillAmount = handler.fill(fluid, action);
      totalFillAmount += fillAmount;
      fluid.setAmount(fluid.getAmount() - fillAmount);
      if (fluid.getAmount() <= 0) {
        break;
      }
    }
    return totalFillAmount;
  }

  @Override
  public FluidStack drain(FluidStack fluid, FluidAction action) {
    if (fluid.isEmpty() || fluid.getAmount() <= 0) {
      return FluidStack.EMPTY;
    }

    fluid = fluid.copy();

    FluidStack totalDrained = FluidStack.EMPTY;
    for (IFluidHandler handler : this.fluidHandlers) {
      FluidStack drain = handler.drain(fluid, action);
      if (!drain.isEmpty()) {
        if (totalDrained.isEmpty()) {
          totalDrained = drain;
        } else {
          totalDrained.setAmount(totalDrained.getAmount() + drain.getAmount());
        }

        fluid.setAmount(fluid.getAmount() - drain.getAmount());
        if (fluid.getAmount() <= 0) {
          break;
        }
      }
    }
    return totalDrained;
  }

  @Override
  public FluidStack drain(int fluid, FluidAction action) {
    if (fluid == 0) {
      return FluidStack.EMPTY;
    }
    FluidStack totalDrained = FluidStack.EMPTY;
    for (IFluidHandler handler : this.fluidHandlers) {
      if (totalDrained.isEmpty()) {
        totalDrained = handler.drain(fluid, action);
        if (!totalDrained.isEmpty()) {
          fluid -= totalDrained.getAmount();
        }
      } else {
        FluidStack copy = totalDrained.copy();
        copy.setAmount(fluid);
        FluidStack drain = handler.drain(copy, action);
        if (!drain.isEmpty()) {
          totalDrained.setAmount(totalDrained.getAmount() + drain.getAmount());
          fluid -= drain.getAmount();
        }
      }

      if (fluid <= 0) {
        break;
      }
    }
    return totalDrained;
  }

  @Override
  public int getTanks() {
    return Arrays.stream(this.fluidHandlers)
        .mapToInt(IFluidHandler::getTanks)
        .sum();
  }

  @Override
  public FluidStack getFluidInTank(int tank) {
    int index = this.getIndexForTank(tank);
    return this.getHandlerFromIndex(index).getFluidInTank(tank);
  }

  @Override
  public int getTankCapacity(int tank) {
    int index = this.getIndexForTank(tank);
    return this.getHandlerFromIndex(index).getTankCapacity(tank);
  }

  @Override
  public boolean isFluidValid(int tank, FluidStack stack) {
    int index = this.getIndexForTank(tank);
    return this.getHandlerFromIndex(index).isFluidValid(tank, stack);
  }
}

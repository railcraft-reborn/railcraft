package mods.railcraft.util.capability;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.world.item.FluidBottleItem;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.material.RailcraftFluids;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class FluidBottleWrapper implements IFluidHandlerItem, ICapabilityProvider {

  private final LazyOptional<IFluidHandlerItem> holder = LazyOptional.of(() -> this);

  @NotNull
  protected ItemStack container;

  public FluidBottleWrapper(@NotNull ItemStack container) {
    this.container = container;
  }

  @NotNull
  @Override
  public ItemStack getContainer() {
    return this.container;
  }

  public boolean canFillFluidType(FluidStack fluid) {
    return fluid.getFluid().isSame(RailcraftFluids.CREOSOTE.get());
  }

  @NotNull
  public FluidStack getFluid() {
    if (this.container.getItem() instanceof FluidBottleItem bottle) {
      return new FluidStack(bottle.getFluid(), FluidBottleItem.QUANTITY);
    } else {
      return FluidStack.EMPTY;
    }
  }

  protected void setFluid(@NotNull FluidStack fluidStack) {
    if (fluidStack.isEmpty()) {
      this.container = new ItemStack(Items.GLASS_BOTTLE);
    } else {
      this.container = new ItemStack(RailcraftItems.CREOSOTE_BOTTLE.get());
    }
  }

  @Override
  public int getTanks() {
    return 1;
  }

  @NotNull
  @Override
  public FluidStack getFluidInTank(int tank) {
    return this.getFluid();
  }

  @Override
  public int getTankCapacity(int tank) {
    return FluidBottleItem.QUANTITY;
  }

  @Override
  public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
    return true;
  }

  @Override
  public int fill(FluidStack resource, FluidAction action) {
    if (this.container.getCount() != 1 || resource.getAmount() < FluidBottleItem.QUANTITY
        || !this.getFluid().isEmpty()
        || !this.canFillFluidType(resource)) {
      return 0;
    }

    if (action.execute()) {
      this.setFluid(resource);
    }

    return FluidBottleItem.QUANTITY;
  }

  @NotNull
  @Override
  public FluidStack drain(FluidStack resource, FluidAction action) {
    if (this.container.getCount() != 1 || resource.getAmount() < FluidBottleItem.QUANTITY) {
      return FluidStack.EMPTY;
    }

    var fluidStack = this.getFluid();
    if (!fluidStack.isEmpty() && fluidStack.isFluidEqual(resource)) {
      if (action.execute()) {
        this.setFluid(FluidStack.EMPTY);
      }
      return fluidStack;
    }

    return FluidStack.EMPTY;
  }

  @NotNull
  @Override
  public FluidStack drain(int maxDrain, FluidAction action) {
    if (this.container.getCount() != 1 || maxDrain < FluidBottleItem.QUANTITY) {
      return FluidStack.EMPTY;
    }

    var fluidStack = getFluid();
    if (!fluidStack.isEmpty()) {
      if (action.execute()) {
        this.setFluid(FluidStack.EMPTY);
      }
      return fluidStack;
    }

    return FluidStack.EMPTY;
  }

  @Override
  @NotNull
  public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability,
      @Nullable Direction facing) {
    return ForgeCapabilities.FLUID_HANDLER_ITEM.orEmpty(capability, this.holder);
  }
}


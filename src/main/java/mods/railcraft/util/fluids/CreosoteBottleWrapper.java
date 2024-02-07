package mods.railcraft.util.fluids;

import org.jetbrains.annotations.NotNull;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.item.FluidBottleItem;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public class CreosoteBottleWrapper implements IFluidHandlerItem {

  @NotNull
  protected ItemStack container;

  public CreosoteBottleWrapper(@NotNull ItemStack container) {
    this.container = container;
  }

  @NotNull
  @Override
  public ItemStack getContainer() {
    return this.container;
  }

  public boolean canFillFluidType(FluidStack fluid) {
    return fluid.is(RailcraftTags.Fluids.CREOSOTE);
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
}

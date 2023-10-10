package mods.railcraft.world.module;

import mods.railcraft.api.fuel.FuelUtil;
import mods.railcraft.util.FluidTools;
import mods.railcraft.util.FluidTools.ProcessType;
import mods.railcraft.world.level.block.entity.steamboiler.FluidFueledSteamBoilerBlockEntity;
import mods.railcraft.world.level.material.StandardTank;
import mods.railcraft.world.level.material.steam.FluidFuelProvider;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public class FluidFueledSteamBoilerModule
    extends SteamBoilerModule<FluidFueledSteamBoilerBlockEntity> {

  protected final StandardTank fuelTank = StandardTank.ofBuckets(16)
      .disableDrain()
      .setValidator(fluidStack -> !fluidStack.isEmpty()
          && FuelUtil.fuelManager().getFuelValue(fluidStack.getFluid()) > 0);

  private FluidTools.ProcessState fuelProcessState = FluidTools.ProcessState.RESET;

  public FluidFueledSteamBoilerModule(FluidFueledSteamBoilerBlockEntity provider) {
    super(provider, 3);
    this.tankManager.add(this.fuelTank);

    this.fuelTank.changeCallback(provider::setChanged);

    this.boiler.setFuelProvider(new FluidFuelProvider(this.fuelTank));
  }

  public StandardTank getFuelTank() {
    return this.fuelTank;
  }

  @Override
  protected void processFluidContainers() {
    super.processFluidContainers();
    this.fuelProcessState = FluidTools.processContainer(this.fluidContainer, this.fuelTank,
        ProcessType.DRAIN_ONLY, this.fuelProcessState);
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean canPlaceItem(int slot, ItemStack itemStack) {
    if (!this.provider.isFormed()) {
      return false;
    }

    if (slot == SLOT_LIQUID_INPUT) {
      var fluid = FluidUtil.getFluidContained(itemStack).orElse(FluidStack.EMPTY);
      if (fluid.isEmpty()) {
        return false;
      }
      return fluid.getFluid().is(FluidTags.WATER)
          || FuelUtil.fuelManager().getFuelValue(fluid.getFluid()) > 0;
    }

    return false;
  }
}

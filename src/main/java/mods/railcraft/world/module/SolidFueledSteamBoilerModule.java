package mods.railcraft.world.module;

import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.world.level.block.entity.steamboiler.SolidFueledSteamBoilerBlockEntity;
import mods.railcraft.world.level.material.steam.SolidFuelProvider;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.fluids.FluidUtil;

public class SolidFueledSteamBoilerModule extends SteamBoilerModule<SolidFueledSteamBoilerBlockEntity> {

  private static final int SLOT_BURN = 3;
  private static final int SLOT_FUEL = 4;

  private final ContainerMapper burnContainer = ContainerMapper.make(this.container, SLOT_BURN, 1);
  private final ContainerMapper stockContainer = ContainerMapper.make(this.container, SLOT_FUEL, 3);
  private final ContainerMapper fuelContainer = ContainerMapper.make(this.container, SLOT_BURN, 4);
  private boolean needsFuel;

  private int processTicks;

  private int fuelMoveTicks;

  public SolidFueledSteamBoilerModule(SolidFueledSteamBoilerBlockEntity provider) {
    super(provider, 7);
    this.boiler.setFuelProvider(new SolidFuelProvider(this.container, SLOT_BURN));
  }

  @Override
  public void serverTick() {
    super.serverTick();

    if (this.provider.isMaster() && this.processTicks++ >= 4) {
      this.processTicks = 0;
      this.stockContainer.moveOneItemTo(this.burnContainer);
      this.needsFuel = this.fuelContainer.countItems() < 64;
    }

    this.provider.getMembership().ifPresent(membership -> {
      var masterModule = membership.master().getModule(SolidFueledSteamBoilerModule.class).get();
      if (masterModule.needsFuel && this.fuelMoveTicks++ >= 128) {
        this.fuelMoveTicks = 0;
        this.provider.findAdjacentContainers()
            .moveOneItemTo(masterModule.fuelContainer, SolidFueledSteamBoilerModule::isFuel);
      }
    });
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean canPlaceItem(int slot, ItemStack itemStack) {
    if (!this.provider.isFormed()) {
      return false;
    }
    if (slot >= SLOT_BURN) {
      return isFuel(itemStack);
    } else if (slot == SLOT_LIQUID_INPUT) {
      return FluidUtil.getFluidContained(itemStack)
          .map(fluid -> fluid.is(FluidTags.WATER)).orElse(false);
    }
    return false;
  }

  private static boolean isFuel(ItemStack itemStack) {
    return CommonHooks.getBurnTime(itemStack, null) > 0;
  }
}

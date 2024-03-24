package mods.railcraft.world.level.material.steam;

import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.world.item.RefinedFirestoneItem;
import mods.railcraft.world.level.material.FuelProvider;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.CommonHooks;

public class SolidFuelProvider implements FuelProvider {

  private final Container container;
  private final int slot;
  private Item lastItem;

  public SolidFuelProvider(Container container, int slot) {
    this.container = container;
    this.slot = slot;
  }

  @Override
  public float getHeatStep() {
    return this.lastItem instanceof RefinedFirestoneItem
        ? SteamConstants.HEAT_STEP * 30
        : SteamConstants.HEAT_STEP;
  }

  @Override
  public float consumeFuel() {
    var fuel = this.container.getItem(this.slot);
    int burn = CommonHooks.getBurnTime(fuel, null);
    if (burn > 0) {
      this.lastItem = fuel.getItem();
      this.container.setItem(this.slot, ContainerTools.depleteItem(fuel));
    }
    return burn;
  }
}

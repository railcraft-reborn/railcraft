package mods.railcraft.world.level.material.fluid.steam;

import mods.railcraft.plugins.FuelPlugin;
import mods.railcraft.util.inventory.InvTools;
import mods.railcraft.world.item.RefinedFirestoneItem;
import mods.railcraft.world.level.material.fluid.IFuelProvider;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public class SolidFuelProvider implements IFuelProvider {

  private final IInventory inv;
  private final int slot;
  private Item lastItem;

  public SolidFuelProvider(IInventory inv, int slot) {
    this.inv = inv;
    this.slot = slot;
  }

  @Override
  public float getHeatStep() {
    if (this.lastItem instanceof RefinedFirestoneItem)
      return SteamConstants.HEAT_STEP * 30;
    return SteamConstants.HEAT_STEP;
  }

  @Override
  public float consumeFuel() {
    ItemStack fuel = this.inv.getItem(this.slot);
    int burn = FuelPlugin.getBurnTime(fuel);
    if (burn > 0) {
      this.lastItem = fuel.getItem();
      this.inv.setItem(this.slot, InvTools.depleteItem(fuel));
    }
    return burn;
  }
}

package mods.railcraft.world.level.material.steam;

import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.world.item.RefinedFirestoneItem;
import mods.railcraft.world.level.material.FuelProvider;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

/**
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public class SolidFuelProvider implements FuelProvider {

  private final Container inv;
  private final int slot;
  private Item lastItem;

  public SolidFuelProvider(Container inv, int slot) {
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
    int burn = ForgeHooks.getBurnTime(fuel, null);
    if (burn > 0) {
      this.lastItem = fuel.getItem();
      this.inv.setItem(this.slot, ContainerTools.depleteItem(fuel));
    }
    return burn;
  }
}

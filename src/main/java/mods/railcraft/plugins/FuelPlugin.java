package mods.railcraft.plugins;

import mods.railcraft.util.inventory.InvTools;
import mods.railcraft.world.level.material.fluid.FluidItemHelper;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class FuelPlugin {

  private static ItemStack lastFuel;
  private static int lastFuelValue;

  /**
   * Internal function that provides custom fuel values before requesting them from Minecraft. It
   * also caches the last fuel hit to reduce cpu cycles.
   *
   * @param stack The item to test
   * @return The fuel value
   */
  public static int getBurnTime(ItemStack stack) {
    if (InvTools.isEmpty(stack))
      return 0;

    if (InvTools.isItemEqualSemiStrict(stack, lastFuel))
      return lastFuelValue;

    lastFuel = stack;
    lastFuelValue = findFuelValue(stack);
    return lastFuelValue;
  }

  private static int findFuelValue(ItemStack stack) {

    Item item = stack.getItem();

    // if (itemID == Item.coal.itemID && stack.getItemDamage() == 0)
    // return 1600;

    if (item == Items.BLAZE_ROD)
      return 1000;

    return FluidItemHelper.getFluidStackInContainer(stack)
        .filter(fluidStack -> fluidStack.getFluid() == Fluids.LAVA)
        .map(FluidStack::getAmount)
        .orElseGet(() -> ForgeHooks.getBurnTime(stack));
  }
}

/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.crafting;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * This interface allows you to interact with the Rolling Machine Crafting Manager.
 *
 * Be aware that Rolling Machine won't accept any unstackable items or items which return containers
 * on crafting. So no fluid crafting.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface IRollingMachineCrafter<I extends IInventory> {

  int DEFAULT_PROCESS_TIME = 100;

  default IRollingMachineRecipeBuilder<I> newRecipe(ItemStack output) {
    return new IRollingMachineRecipeBuilder<I>() {};
  }

  /**
   * Given a specific combination of ItemStacks, this function will try to provide a matching
   * recipe.
   *
   * @param craftingInventory the crafting inventory
   * @param world the world
   * @return the matching recipe
   */
  default Optional<IRollingRecipe<I>> getRecipe(CraftingInventory craftingInventory, World world) {
    return Optional.empty();
  }

  /**
   * You can remove recipes from this list, but do not add them, it will throw an
   * UnsupportedOperationException.
   */
  default List<IRollingRecipe<I>> getRecipes() {
    return Collections.emptyList();
  }

  interface IRollingRecipe<I extends IInventory> extends IRecipe<I> {

    int getTickTime();
  }

  interface IRollingMachineRecipeBuilder<I extends IInventory> extends
      IRecipeBuilder<IRollingMachineRecipeBuilder<I>>,
      IRecipeBuilder.ISingleItemStackOutputFeature<IRollingMachineRecipeBuilder<I>>,
      IRecipeBuilder.ITimeFeature<IRollingMachineRecipeBuilder<I>> {

    default IRollingMachineRecipeBuilder<I> group(ResourceLocation group) {
      return this;
    }

    default void recipe(IRecipe<I> recipe) {}

    default void shaped(Object... components) {}

    default void shapeless(Object... components) {}
  }
}

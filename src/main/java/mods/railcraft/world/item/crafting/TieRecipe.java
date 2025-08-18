package mods.railcraft.world.item.crafting;

import java.util.Objects;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public abstract class TieRecipe extends CustomRecipe {

  private final TagKey<Fluid> fluidTag;
  private final Item fluidContainer;
  private final ItemStack result;
  private final Ingredient[] ingredients;

  public TieRecipe(CraftingBookCategory category, TagKey<Fluid> fluidTag,
      Item fluidContainer, ItemStack result, Ingredient... ingredients) {
    super(category);
    this.fluidTag = fluidTag;
    this.fluidContainer = fluidContainer;
    this.result = result;
    if (ingredients.length != 3) {
      throw new IllegalArgumentException("TieRecipe requires exactly 3 ingredients as base layer");
    }
    this.ingredients = ingredients;
  }

  @Override
  public final boolean matches(CraftingInput craftingInput, Level level) {
    if (!canCraftInDimensions(craftingInput.width(), craftingInput.height())) {
      return false;
    }

    if (!craftingInput.getItem(0).isEmpty() || !craftingInput.getItem(2).isEmpty()) {
      return false;
    }

    var item = craftingInput.getItem(1);
    if (item.isEmpty()) {
      return false;
    }
    var cap = item.getCapability(Capabilities.FluidHandler.ITEM);
    if (cap == null || !cap.getFluidInTank(0).is(this.fluidTag)) {
      return false;
    }

    return ingredients[0].test(craftingInput.getItem(3)) &&
           ingredients[1].test(craftingInput.getItem(4)) &&
           ingredients[2].test(craftingInput.getItem(5));
  }

  @Override
  public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
    var fluidHandler = Objects.requireNonNull(
        craftingInput.getItem(1).getCapability(Capabilities.FluidHandler.ITEM));

    if (fluidHandler.getFluidInTank(0).getAmount() >= 1000) {
      return result.copy();
    }
    return ItemStack.EMPTY;
  }

  @Override
  public final NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
    var remainingItems = NonNullList.withSize(input.size(), ItemStack.EMPTY);
    for(int i = 0; i < remainingItems.size(); ++i) {
      ItemStack item = input.getItem(i);
      if (item.hasCraftingRemainingItem()) {
        remainingItems.set(i, item.getCraftingRemainingItem());
      } else if (item.getCapability(Capabilities.FluidHandler.ITEM) != null) {
        var fluidHandler = Objects.requireNonNull(item.getCapability(Capabilities.FluidHandler.ITEM));
        if (fluidHandler.drain(1000, IFluidHandler.FluidAction.SIMULATE).getAmount() == 1000) {
          fluidHandler.drain(1000, IFluidHandler.FluidAction.EXECUTE);
        }
        remainingItems.set(i, item.copy());
      }
    }
    return remainingItems;
  }

  @Override
  public final ItemStack getResultItem(HolderLookup.Provider registries) {
    return result.copy();
  }

  @Override
  public final NonNullList<Ingredient> getIngredients() {
    var ingredients = NonNullList.withSize(9, Ingredient.EMPTY);
    ingredients.set(1, Ingredient.of(fluidContainer));
    ingredients.set(3, this.ingredients[0]);
    ingredients.set(4, this.ingredients[1]);
    ingredients.set(5, this.ingredients[2]);
    return ingredients;
  }

  @Override
  public final boolean canCraftInDimensions(int width, int height) {
    return width == 3 && height == 2;
  }
}

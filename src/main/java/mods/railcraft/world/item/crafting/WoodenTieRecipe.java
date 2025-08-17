package mods.railcraft.world.item.crafting;

import java.util.Objects;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class WoodenTieRecipe extends CustomRecipe {

  public WoodenTieRecipe(CraftingBookCategory category) {
    super(category);
  }

  @Override
  public boolean matches(CraftingInput craftingInput, Level level) {
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
    if (cap == null || !cap.getFluidInTank(0).is(RailcraftTags.Fluids.CREOSOTE)) {
      return false;
    }

    return craftingInput.getItem(3).is(ItemTags.WOODEN_SLABS) &&
        craftingInput.getItem(4).is(ItemTags.WOODEN_SLABS) &&
        craftingInput.getItem(5).is(ItemTags.WOODEN_SLABS);
  }

  @Override
  public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
    var fluidHandler = Objects.requireNonNull(
        craftingInput.getItem(1).getCapability(Capabilities.FluidHandler.ITEM));

    if (fluidHandler.getFluidInTank(0).getAmount() >= 1000) {
      return RailcraftItems.WOODEN_TIE.toStack(3);
    }
    return ItemStack.EMPTY;
  }

  @Override
  public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
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
  public ItemStack getResultItem(HolderLookup.Provider registries) {
    return RailcraftItems.WOODEN_TIE.toStack(3);
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    var ingredients = NonNullList.withSize(9, Ingredient.EMPTY);
    ingredients.set(1, Ingredient.of(RailcraftItems.CREOSOTE_BUCKET.get()));
    ingredients.set(3, Ingredient.of(ItemTags.WOODEN_SLABS));
    ingredients.set(4, Ingredient.of(ItemTags.WOODEN_SLABS));
    ingredients.set(5, Ingredient.of(ItemTags.WOODEN_SLABS));
    return ingredients;
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width == 3 && height == 2;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return RailcraftRecipeSerializers.WOODEN_TIE.get();
  }
}

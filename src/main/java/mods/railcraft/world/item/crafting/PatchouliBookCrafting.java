package mods.railcraft.world.item.crafting;

import org.apache.commons.lang3.NotImplementedException;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

public class PatchouliBookCrafting extends ShapelessRecipe {

  public PatchouliBookCrafting(CraftingBookCategory category) {
    super("", category, makeGuideBook(), NonNullList.of(Ingredient.of(),
        Ingredient.of(Items.BOOK), Ingredient.of(RailcraftItems.IRON_CROWBAR.get())));
  }

  private static ItemStack makeGuideBook() {
    return ItemStack.EMPTY;
    //return PatchouliAPI.get().getBookStack(RailcraftConstants.rl("guide_book"));
  }

  @Override
  public RecipeSerializer<ShapelessRecipe> getSerializer() {
    throw new NotImplementedException();
    //return RailcraftRecipeSerializers.PATCHOULI_BOOK_CRAFTING.get();
  }
}

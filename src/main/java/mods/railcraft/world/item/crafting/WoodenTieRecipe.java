package mods.railcraft.world.item.crafting;

import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class WoodenTieRecipe extends TieRecipe {

  public WoodenTieRecipe(CraftingBookCategory category) {
    super(category, RailcraftTags.Fluids.CREOSOTE,
        RailcraftItems.WOODEN_TIE.toStack(3));
  }

  @Override
  protected boolean testIngredient(ItemStack itemPresent, int index) {
    return itemPresent.is(ItemTags.WOODEN_SLABS);
  }

  @Override
  public RecipeSerializer<WoodenTieRecipe> getSerializer() {
    return RailcraftRecipeSerializers.WOODEN_TIE.get();
  }
}

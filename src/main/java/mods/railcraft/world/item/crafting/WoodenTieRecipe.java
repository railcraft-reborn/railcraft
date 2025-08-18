package mods.railcraft.world.item.crafting;

import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class WoodenTieRecipe extends TieRecipe {

  public WoodenTieRecipe(CraftingBookCategory category) {
    super(category, RailcraftTags.Fluids.CREOSOTE,
        RailcraftItems.CREOSOTE_BUCKET.get(),
        RailcraftItems.WOODEN_TIE.toStack(3),
        Ingredient.of(ItemTags.WOODEN_SLABS),
        Ingredient.of(ItemTags.WOODEN_SLABS),
        Ingredient.of(ItemTags.WOODEN_SLABS));
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return RailcraftRecipeSerializers.WOODEN_TIE.get();
  }
}

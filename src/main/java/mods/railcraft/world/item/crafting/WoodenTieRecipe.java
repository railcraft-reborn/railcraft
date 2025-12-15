package mods.railcraft.world.item.crafting;

import java.util.Optional;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class WoodenTieRecipe extends TieRecipe {

  public WoodenTieRecipe(CraftingBookCategory category) {
    super(category, RailcraftTags.Fluids.CREOSOTE, RailcraftItems.WOODEN_TIE.toStack(3));
    this.ingredients.set(1, Optional.of(Ingredient.of(RailcraftItems.CREOSOTE_BUCKET.get())));
    this.ingredients.set(3, Optional.of(Ingredient.of(Items.OAK_SLAB)));
    this.ingredients.set(4, Optional.of(Ingredient.of(Items.OAK_SLAB)));
    this.ingredients.set(5, Optional.of(Ingredient.of(Items.OAK_SLAB)));
  }

  @Override
  protected boolean testBottomIngredients(CraftingInput craftingInput, int index) {
    return craftingInput.getItem(index).is(ItemTags.WOODEN_SLABS);
  }

  @Override
  public RecipeSerializer<WoodenTieRecipe> getSerializer() {
    return RailcraftRecipeSerializers.WOODEN_TIE.get();
  }
}

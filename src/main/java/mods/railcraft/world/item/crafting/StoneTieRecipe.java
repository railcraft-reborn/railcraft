package mods.railcraft.world.item.crafting;

import java.util.Optional;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.common.Tags;

public class StoneTieRecipe extends TieRecipe {

  public StoneTieRecipe(CraftingBookCategory category) {
    super(category, Tags.Fluids.WATER, RailcraftItems.STONE_TIE.toStack());
    this.ingredients.set(1, Optional.of(Ingredient.of(Items.WATER_BUCKET)));
    this.ingredients.set(3, Optional.of(Ingredient.of(RailcraftItems.BAG_OF_CEMENT.get())));
    this.ingredients.set(4, Optional.of(Ingredient.of(RailcraftItems.REBAR.get())));
    this.ingredients.set(5, Optional.of(Ingredient.of(RailcraftItems.BAG_OF_CEMENT.get())));
  }

  @Override
  public RecipeSerializer<StoneTieRecipe> getSerializer() {
    return RailcraftRecipeSerializers.STONE_TIE.get();
  }
}

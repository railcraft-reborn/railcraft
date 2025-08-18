package mods.railcraft.world.item.crafting;

import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.common.Tags;

public class StoneTieRecipe extends TieRecipe {

  public StoneTieRecipe(CraftingBookCategory category) {
    super(category, Tags.Fluids.WATER,
        Items.WATER_BUCKET,
        RailcraftItems.STONE_TIE.toStack(),
        Ingredient.of(RailcraftItems.BAG_OF_CEMENT.get()),
        Ingredient.of(RailcraftItems.REBAR.get()),
        Ingredient.of(RailcraftItems.BAG_OF_CEMENT.get()));
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return RailcraftRecipeSerializers.STONE_TIE.get();
  }
}

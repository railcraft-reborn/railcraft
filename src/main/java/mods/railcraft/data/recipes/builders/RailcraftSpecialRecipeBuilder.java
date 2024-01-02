package mods.railcraft.data.recipes.builders;

import javax.annotation.Nullable;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.CraftingRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class RailcraftSpecialRecipeBuilder extends SpecialRecipeBuilder {

  private final RecipeSerializer<?> serializer;

  public RailcraftSpecialRecipeBuilder(RecipeSerializer<?> serializer) {
    super(serializer);
    this.serializer = serializer;
  }

  public static RailcraftSpecialRecipeBuilder special(RecipeSerializer<? extends CraftingRecipe> serializer) {
    return new RailcraftSpecialRecipeBuilder(serializer);
  }

  @Override
  public void save(RecipeOutput recipeOutput, String id) {
    recipeOutput.accept(new CraftingRecipeBuilder.CraftingResult(CraftingBookCategory.MISC) {
      public RecipeSerializer<?> type() {
        return serializer;
      }

      public ResourceLocation id() {
        return RailcraftConstants.rl(id);
      }

      @Nullable
      public AdvancementHolder advancement() {
        return null;
      }
    });
  }
}

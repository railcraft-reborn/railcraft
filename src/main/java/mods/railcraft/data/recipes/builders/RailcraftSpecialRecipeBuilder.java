package mods.railcraft.data.recipes.builders;

import java.util.function.Consumer;
import javax.annotation.Nullable;
import com.google.gson.JsonObject;
import mods.railcraft.Railcraft;
import net.minecraft.data.recipes.CraftingRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
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
  public void save(Consumer<FinishedRecipe> finishedRecipeConsumer, String id) {
    finishedRecipeConsumer.accept(new CraftingRecipeBuilder.CraftingResult(CraftingBookCategory.MISC) {
      public RecipeSerializer<?> getType() {
        return serializer;
      }

      public ResourceLocation getId() {
        return Railcraft.rl(id);
      }

      @Nullable
      public JsonObject serializeAdvancement() {
        return null;
      }

      public ResourceLocation getAdvancementId() {
        return Railcraft.rl("");
      }
    });
  }
}

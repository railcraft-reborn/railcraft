package mods.railcraft.data.recipes.builders;

import java.util.function.Consumer;
import javax.annotation.Nullable;
import com.google.gson.JsonObject;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;

public class RailcraftSpecialRecipeBuilder extends SpecialRecipeBuilder {

  private final RecipeSerializer<?> serializer;

  public RailcraftSpecialRecipeBuilder(SimpleRecipeSerializer<?> serializer) {
    super(serializer);
    this.serializer = serializer;
  }

  public static RailcraftSpecialRecipeBuilder special(SimpleRecipeSerializer<?> serializer) {
    return new RailcraftSpecialRecipeBuilder(serializer);
  }

  @Override
  public void save(Consumer<FinishedRecipe> finishedRecipeConsumer, String id) {
    finishedRecipeConsumer.accept(new FinishedRecipe() {
      public RecipeSerializer<?> getType() {
        return serializer;
      }

      @Override
      public void serializeRecipeData(JsonObject pJson) {

      }

      public ResourceLocation getId() {
        return RailcraftConstants.rl(id);
      }

      @Nullable
      public JsonObject serializeAdvancement() {
        return null;
      }

      public ResourceLocation getAdvancementId() {
        return RailcraftConstants.rl("");
      }
    });
  }
}

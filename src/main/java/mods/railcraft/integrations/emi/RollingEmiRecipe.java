package mods.railcraft.integrations.emi;

import java.util.ArrayList;
import java.util.List;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import mods.railcraft.world.item.crafting.RollingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.RecipeHolder;

public class RollingEmiRecipe extends EmiCraftingRecipe {

  public RollingEmiRecipe(RecipeHolder<RollingRecipe> recipe) {
    super(padIngredients(recipe.value()),
        EmiStack.of(recipe.value().getResultItem(Minecraft.getInstance().level.registryAccess())),
        recipe.id(), false);
  }

  @Override
  public EmiRecipeCategory getCategory() {
    return RailcraftEmiPlugin.ROLLING_CATEGORY;
  }

  private static List<EmiIngredient> padIngredients(RollingRecipe recipe) {
    var list = new ArrayList<EmiIngredient>();
    int i = 0;
    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        if (x >= recipe.getWidth() || y >= recipe.getHeight() || i >= recipe.getIngredients().size()) {
          list.add(EmiStack.EMPTY);
        } else {
          list.add(EmiIngredient.of(recipe.getIngredients().get(i++)));
        }
      }
    }
    return list;
  }
}

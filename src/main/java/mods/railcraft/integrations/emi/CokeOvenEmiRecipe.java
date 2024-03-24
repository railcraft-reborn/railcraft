package mods.railcraft.integrations.emi;

import java.util.List;
import dev.emi.emi.api.neoforge.NeoForgeEmiStack;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import mods.railcraft.integrations.jei.category.CokeOvenRecipeCategory;
import mods.railcraft.world.item.crafting.CokeOvenRecipe;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;

public class CokeOvenEmiRecipe extends BasicEmiRecipe {

  private final CokeOvenRecipe recipe;

  public CokeOvenEmiRecipe(RecipeHolder<CokeOvenRecipe> recipe) {
    super(RailcraftEmiPlugin.COKING_CATEGORY, recipe.id(),
        CokeOvenRecipeCategory.WIDTH, CokeOvenRecipeCategory.HEIGHT);
    this.recipe = recipe.value();
    this.inputs.add(EmiIngredient.of(this.recipe.getIngredients().get(0)));
    var level = Minecraft.getInstance().level;
    this.outputs.add(EmiStack.of(this.recipe.getResultItem(level.registryAccess())));
  }

  @Override
  public void addWidgets(WidgetHolder widgets) {
    widgets.addTexture(CokeOvenRecipeCategory.BACKGROUND, 0, 0, width, height, 15, 23);
    widgets.addFillingArrow(18, 20, 10_000).tooltip((x, y) -> {
      int cookTime = recipe.getCookingTime();
      if (cookTime > 0) {
        int cookTimeSeconds = cookTime / SharedConstants.TICKS_PER_SECOND;
        var timeString = Component.translatable("gui.jei.category.smelting.time.seconds",
            cookTimeSeconds);
        return List.of(EmiTooltipComponents.of(timeString));
      }
      return List.of(EmiTooltipComponents.of(Component.empty()));
    });
    widgets.addAnimatedTexture(EmiTexture.FULL_FLAME, 1, 3, 10_000, false, true, true);

    widgets.addSlot(this.inputs.get(0), 0, 19);
    widgets.addSlot(this.outputs.get(0), 42, 15).large(true).recipeContext(this);

    var fluid = NeoForgeEmiStack.of(recipe.getCreosote());
    widgets.addTank(fluid, 74, 0, 50, 49, 10_000)
        .drawBack(false)
        .recipeContext(this);
  }
}

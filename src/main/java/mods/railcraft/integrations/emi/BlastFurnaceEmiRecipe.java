package mods.railcraft.integrations.emi;

import java.util.List;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import mods.railcraft.integrations.jei.category.BlastFurnaceRecipeCategory;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.crafting.BlastFurnaceRecipe;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

public class BlastFurnaceEmiRecipe extends BasicEmiRecipe {

  private final BlastFurnaceRecipe recipe;

  public BlastFurnaceEmiRecipe(RecipeHolder<BlastFurnaceRecipe> recipe) {
    super(RailcraftEmiPlugin.BLASTING_CATEGORY, recipe.id(),
        BlastFurnaceRecipeCategory.WIDTH, BlastFurnaceRecipeCategory.HEIGHT);
    this.recipe = recipe.value();
    this.inputs.add(EmiIngredient.of(this.recipe.getIngredients().get(0)));
    var level = Minecraft.getInstance().level;
    this.outputs.add(EmiStack.of(this.recipe.getResultItem(level.registryAccess())));
    if (this.recipe.getSlagOutput() > 0) {
      this.outputs.add(EmiStack.of(
          new ItemStack(RailcraftItems.SLAG.get(), this.recipe.getSlagOutput())));
    }
  }

  @Override
  public void addWidgets(WidgetHolder widgets) {
    widgets.addTexture(BlastFurnaceRecipeCategory.BACKGROUND,
        0, 0, width, height, 55, 16);
    widgets.addFillingArrow(24, 18, 10_000).tooltip((x, y) -> {
      int cookTime = recipe.getCookingTime();
      if (cookTime > 0) {
        int cookTimeSeconds = cookTime / SharedConstants.TICKS_PER_SECOND;
        var timeString = Component.translatable("gui.jei.category.smelting.time.seconds",
            cookTimeSeconds);
        return List.of(EmiTooltipComponents.of(timeString));
      }
      return List.of(EmiTooltipComponents.of(Component.empty()));
    });
    widgets.addAnimatedTexture(EmiTexture.FULL_FLAME, 1, 20, 10_000, false, true, true);

    widgets.addSlot(this.inputs.get(0), 0, 0);
    widgets.addSlot(this.outputs.get(0), 56, 0).large(true).recipeContext(this);
    if (this.outputs.size() > 1) {
      widgets.addSlot(this.outputs.get(1), 60, 36).recipeContext(this);
    }
  }
}

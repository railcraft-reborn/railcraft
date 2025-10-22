package mods.railcraft.integrations.emi;

import java.util.List;
import dev.emi.emi.api.forge.ForgeEmiStack;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.crafting.CokeOvenRecipe;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CokeOvenEmiRecipe extends BasicEmiRecipe {

  private static final int WIDTH = 124;
  private static final int HEIGHT = 49;
  private static final ResourceLocation BACKGROUND =
      RailcraftConstants.rl("textures/gui/container/coke_oven.png");

  private final CokeOvenRecipe recipe;

  public CokeOvenEmiRecipe(CokeOvenRecipe recipe) {
    super(RailcraftEmiPlugin.COKING_CATEGORY, recipe.getId(), WIDTH, HEIGHT);
    this.recipe = recipe;
    this.inputs.add(EmiIngredient.of(recipe.getIngredients().get(0)));
    this.outputs.add(EmiStack.of(recipe.getResultItem()));
  }

  @Override
  public void addWidgets(WidgetHolder widgets) {
    widgets.addTexture(BACKGROUND, 0, 0, width, height, 15, 23);
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

    var fluid = ForgeEmiStack.of(recipe.getCreosote());
    widgets.addTank(fluid, 74, 0, 50, 49, 10_000)
        .drawBack(false)
        .recipeContext(this);
  }
}

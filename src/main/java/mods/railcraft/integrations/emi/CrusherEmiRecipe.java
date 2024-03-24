package mods.railcraft.integrations.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import mods.railcraft.Translations;
import mods.railcraft.integrations.jei.category.CrusherRecipeCategory;
import mods.railcraft.world.item.crafting.CrusherRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

public class CrusherEmiRecipe extends BasicEmiRecipe {

  private final CrusherRecipe recipe;

  public CrusherEmiRecipe(RecipeHolder<CrusherRecipe> recipe) {
    super(RailcraftEmiPlugin.CRUSHING_CATEGORY, recipe.id(),
        CrusherRecipeCategory.WIDTH, CrusherRecipeCategory.HEIGHT);
    this.recipe = recipe.value();
    this.inputs.add(EmiIngredient.of(this.recipe.getIngredients().get(0)));
    this.recipe.getProbabilityOutputs().stream()
        .map(CrusherRecipe.CrusherOutput::getOutput)
        .map(EmiStack::of)
        .forEach(this.outputs::add);
  }

  @Override
  public void addWidgets(WidgetHolder widgets) {
    widgets.addTexture(CrusherRecipeCategory.BACKGROUND, 0, 0, width, height, 0, 171);
    var crushingTexture = new EmiTexture(CrusherRecipeCategory.BACKGROUND,
        144, 171, 29, 53);
    widgets.addAnimatedTexture(crushingTexture, 58, 0,
        1000 * this.recipe.getProcessTime() / SharedConstants.TICKS_PER_SECOND,
        true, false, false);

    widgets.addSlot(this.inputs.get(0), 18, 18);

    var outputs = recipe.getProbabilityOutputs();
    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        int index = 1 + x + (y * 3);
        var itemStack = ItemStack.EMPTY;
        if (outputs.size() > index - 1) {
          itemStack = outputs.get(index - 1).getOutput();
        }
        if (itemStack.isEmpty()) {
          widgets.addSlot(90 + x * 18, y * 18);
        } else {
          widgets
              .addSlot(EmiStack.of(itemStack), 90 + x * 18, y * 18)
              .appendTooltip(() -> {
                double probability = outputs.get(index - 1).probability() * 100;
                var probText = Component.translatable(Translations.Jei.CRUSHER_TIP, probability)
                    .withStyle(ChatFormatting.GRAY);
                return EmiTooltipComponents.of(probText);
              })
              .recipeContext(this);
        }
      }
    }
  }
}

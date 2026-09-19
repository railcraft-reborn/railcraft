package mods.railcraft.integrations.jei.category;

import java.util.List;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mods.railcraft.Translations;
import mods.railcraft.integrations.jei.RecipeTypes;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.crafting.RollingRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

public class RollingRecipeCategory extends AbstractRecipeCategory<RecipeHolder<RollingRecipe>> {

  private static final int WIDTH = 116;
  private static final int HEIGHT = 54;

  private final ICraftingGridHelper craftingGridHelper;

  public RollingRecipeCategory(IGuiHelper guiHelper) {
    super(
        RecipeTypes.ROLLING_MACHINE,
        Component.translatable(Translations.Jei.METAL_ROLLING),
        guiHelper.createDrawableItemLike(RailcraftItems.MANUAL_ROLLING_MACHINE.get()),
        WIDTH,
        HEIGHT
    );
    this.craftingGridHelper = guiHelper.createCraftingGridHelper();
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<RollingRecipe> recipeHolder,
      IFocusGroup focuses) {
    var recipe = recipeHolder.value();
    var display = recipe.display().getFirst();
    var resultItem = display.result();
    this.craftingGridHelper.createAndSetOutputs(builder, resultItem);

    int width = recipe.getWidth();
    int height = recipe.getHeight();
    if (recipe.display().isEmpty()) {
      craftingGridHelper.createAndSetIngredientsFromDisplays(builder, List.of(), width, height);
    } else {
      List<SlotDisplay> ingredients = switch (display) {
        case ShapedCraftingRecipeDisplay shapedDisplay -> shapedDisplay.ingredients();
        case ShapelessCraftingRecipeDisplay shapelessDisplay -> shapelessDisplay.ingredients();
        default -> List.of();
      };
      craftingGridHelper.createAndSetIngredientsFromDisplays(builder, ingredients, width, height);
    }
  }
}

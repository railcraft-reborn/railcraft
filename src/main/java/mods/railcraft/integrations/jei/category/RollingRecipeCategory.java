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
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;

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
    var registryAccess = Minecraft.getInstance().level.registryAccess();
    this.craftingGridHelper.createAndSetOutputs(builder, List.of(recipe.getResultItem(registryAccess)));
    int width = recipe.getWidth();
    int height = recipe.getHeight();
    var inputs = recipe.getIngredients().stream()
        .map(ingredient -> List.of(ingredient.getItems()))
        .toList();
    this.craftingGridHelper.createAndSetInputs(builder, inputs, width, height);
  }
}

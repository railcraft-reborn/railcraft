package mods.railcraft.integrations.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mods.railcraft.Translations;
import mods.railcraft.integrations.jei.RecipeTypes;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.crafting.RollingRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class RollingRecipeCategory implements IRecipeCategory<RollingRecipe> {

  private static final int WIDTH = 116;
  private static final int HEIGHT = 54;

  private static final ResourceLocation CRAFTING_TABLE =
      new ResourceLocation("textures/gui/container/crafting_table.png");

  private final IDrawable background, icon;

  public RollingRecipeCategory(IGuiHelper guiHelper) {
    this.background = guiHelper.createDrawable(CRAFTING_TABLE, 29, 16, WIDTH, HEIGHT);
    var itemStack = new ItemStack(RailcraftItems.MANUAL_ROLLING_MACHINE.get());
    this.icon = guiHelper.createDrawableItemStack(itemStack);
  }

  @Override
  public RecipeType<RollingRecipe> getRecipeType() {
    return RecipeTypes.ROLLING_MACHINE;
  }

  @Override
  public Component getTitle() {
    return Component.translatable(Translations.Jei.METAL_ROLLING);
  }

  @Override
  public IDrawable getBackground() {
    return this.background;
  }

  @Override
  public IDrawable getIcon() {
    return this.icon;
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, RollingRecipe recipe, IFocusGroup focuses) {
    builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 19).addItemStack(recipe.getResultItem());
    var ingredients = recipe.getIngredients();

    int heightOffset = Math.floorDiv(3 - recipe.getHeight(), 2);
    int widthOffset = Math.floorDiv(3 - recipe.getWidth(), 2);
    int stackIndex = 0;

    for (int y = heightOffset; y < recipe.getHeight() + heightOffset; y++) {
      for (int x = widthOffset; x < recipe.getWidth() + widthOffset; x++) {
        var ingredient = ingredients.get(stackIndex);
        builder
            .addSlot(RecipeIngredientRole.INPUT, x * 18 + 1, y * 18 + 1)
            .addIngredients(ingredient);
        stackIndex++;
      }
    }
  }
}

package mods.railcraft.integrations.jei.category;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mods.railcraft.integrations.jei.RecipeTypes;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.crafting.RollingRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class RollingRecipeCategory implements IRecipeCategory<RollingRecipe> {

  private static final int WIDTH = 116;
  private static final int HEIGHT = 54;
  private final IDrawable background, icon;
  private final Component localizedName;

  public RollingRecipeCategory(IGuiHelper guiHelper) {
    var location = new ResourceLocation("minecraft", "textures/gui/container/crafting_table.png");
    background = guiHelper.createDrawable(location, 29, 16, WIDTH, HEIGHT);
    icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RailcraftItems.MANUAL_ROLLING_MACHINE.get()));
    localizedName = Component.translatable("jei.railcraft.category.rolling");
  }

  @Override
  public RecipeType<RollingRecipe> getRecipeType() {
    return RecipeTypes.ROLLING_MACHINE;
  }

  @Override
  public Component getTitle() {
    return localizedName;
  }

  @Override
  public IDrawable getBackground() {
    return background;
  }

  @Override
  public IDrawable getIcon() {
    return icon;
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, RollingRecipe recipe, IFocusGroup focuses) {
    builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 19).addItemStack(recipe.getResultItem());

    var ingredients = recipe.getIngredients();
    for(int y = 0; y < 3; y++) {
      for(int x = 0; x < 3; x++) {
        var ingredient = ingredients.get(x + (y * 3));
        builder
          .addSlot(RecipeIngredientRole.INPUT, x * 18 + 1, y * 18 + 1)
          .addIngredients(ingredient);
      }
    }
  }
}

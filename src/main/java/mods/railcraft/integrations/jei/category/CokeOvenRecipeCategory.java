package mods.railcraft.integrations.jei.category;

import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mods.railcraft.Railcraft;
import mods.railcraft.Translations;
import mods.railcraft.integrations.jei.RecipeTypes;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.crafting.CokeOvenRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CokeOvenRecipeCategory implements IRecipeCategory<CokeOvenRecipe> {

  private static final int WIDTH = 124;
  private static final int HEIGHT = 49;

  private static final ResourceLocation COKE_OVEN_BACKGROUND =
      Railcraft.rl("textures/gui/container/coke_oven.png");

  private final IDrawable background, icon, flame, arrow;

  public CokeOvenRecipeCategory(IGuiHelper guiHelper) {
    this.background = guiHelper.createDrawable(COKE_OVEN_BACKGROUND, 15, 23, WIDTH, HEIGHT);
    var itemStack = new ItemStack(RailcraftItems.COKE_OVEN_BRICKS.get());
    this.icon = guiHelper.createDrawableItemStack(itemStack);

    this.flame = guiHelper.createAnimatedDrawable(
        guiHelper.createDrawable(COKE_OVEN_BACKGROUND, 176, 47, 14, 14),
        200, IDrawableAnimated.StartDirection.TOP, true);
    this.arrow = guiHelper.createAnimatedDrawable(
        guiHelper.createDrawable(COKE_OVEN_BACKGROUND, 176, 61, 22, 15),
        200, IDrawableAnimated.StartDirection.LEFT, false);
  }

  @Override
  public RecipeType<CokeOvenRecipe> getRecipeType() {
    return RecipeTypes.COKE_OVEN;
  }

  @Override
  public Component getTitle() {
    return Component.translatable(Translations.Jei.COKE_OVEN);
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
  public void draw(CokeOvenRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics,
      double mouseX, double mouseY) {
    flame.draw(guiGraphics, 1, 3);
    arrow.draw(guiGraphics, 19, 21);

    int cookTime = recipe.getCookingTime();
    if (cookTime > 0) {
      int cookTimeSeconds = cookTime / 20;
      var timeString = Component.translatable("gui.jei.category.smelting.time.seconds",
          cookTimeSeconds);
      var minecraft = Minecraft.getInstance();
      var font = minecraft.font;
      int stringWidth = font.width(timeString);
      guiGraphics.drawString(font, timeString, getBackground().getWidth() - stringWidth - 80, 43,
          0xFF808080, false);
    }
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, CokeOvenRecipe recipe, IFocusGroup focuses) {
    var ingredients = recipe.getIngredients();
    builder
        .addSlot(RecipeIngredientRole.INPUT, 1, 20)
        .addIngredients(ingredients.get(0));
    builder
        .addSlot(RecipeIngredientRole.OUTPUT, 46, 20)
        .addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
    // Not the actual capacity, but is 10000 for a better visibility
    // TODO: FIXMEEEEE
    /*builder.addSlot(RecipeIngredientRole.OUTPUT, 75, 1)
        .addIngredient(ForgeTypes.FLUID_STACK, recipe.getCreosote())
        .setFluidRenderer(10_000, true, 48, 47);*/
  }
}

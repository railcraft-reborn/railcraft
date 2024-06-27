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
import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.integrations.jei.RailcraftJeiPlugin;
import mods.railcraft.integrations.jei.RecipeTypes;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.crafting.CokeOvenRecipe;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CokeOvenRecipeCategory implements IRecipeCategory<CokeOvenRecipe> {

  public static final int WIDTH = 124;
  public static final int HEIGHT = 49;

  public static final ResourceLocation BACKGROUND =
      RailcraftConstants.rl("textures/gui/container/coke_oven.png");

  private final IDrawable background, icon, flame, arrow;

  public CokeOvenRecipeCategory(IGuiHelper guiHelper) {
    this.background = guiHelper.createDrawable(BACKGROUND, 15, 23, WIDTH, HEIGHT);
    var itemStack = new ItemStack(RailcraftItems.COKE_OVEN_BRICKS.get());
    this.icon = guiHelper.createDrawableItemStack(itemStack);

    this.flame = guiHelper.createAnimatedDrawable(
        guiHelper.createDrawable(BACKGROUND, 176, 47, 14, 14),
        200, IDrawableAnimated.StartDirection.TOP, true);
    this.arrow = guiHelper.createAnimatedDrawable(
        guiHelper.createDrawable(BACKGROUND, 176, 61, 22, 15),
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
    return this.background;
  }

  @Override
  public IDrawable getIcon() {
    return this.icon;
  }

  @Override
  public void draw(CokeOvenRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics,
      double mouseX, double mouseY) {
    this.flame.draw(guiGraphics, 1, 3);
    this.arrow.draw(guiGraphics, 19, 21);

    int cookTime = recipe.getCookingTime();
    if (cookTime > 0) {
      int cookTimeSeconds = cookTime / SharedConstants.TICKS_PER_SECOND;
      var timeString = Component.translatable("gui.jei.category.smelting.time.seconds",
          cookTimeSeconds);
      var font = Minecraft.getInstance().font;
      int stringWidth = font.width(timeString);
      guiGraphics.drawString(font, timeString, getBackground().getWidth() - stringWidth - 80, 43,
          RailcraftJeiPlugin.TEXT_COLOR, false);
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
    builder.addSlot(RecipeIngredientRole.OUTPUT, 75, 1)
        .addIngredient(ForgeTypes.FLUID_STACK, recipe.getCreosote())
        .setFluidRenderer(10_000, true, 48, 47);
  }
}

package mods.railcraft.integrations.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
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
import net.minecraft.world.item.crafting.RecipeHolder;

public class CokeOvenRecipeCategory extends AbstractRecipeCategory<RecipeHolder<CokeOvenRecipe>> {

  private static final int WIDTH = 127;
  private static final int HEIGHT = 49;

  private static final ResourceLocation BACKGROUND =
      RailcraftConstants.rl("textures/gui/container/coke_oven.png");

  private final IDrawable tankBackground, tankOverlay, flame, arrow;

  public CokeOvenRecipeCategory(IGuiHelper guiHelper) {
    super(
        RecipeTypes.COKE_OVEN,
        Component.translatable(Translations.Jei.COKE_OVEN),
        guiHelper.createDrawableItemLike(RailcraftItems.COKE_OVEN_BRICKS.get()),
        WIDTH,
        HEIGHT
    );

    this.tankBackground = guiHelper.createDrawable(BACKGROUND, 89, 23, 50, 49);
    this.tankOverlay = guiHelper.createDrawable(BACKGROUND, 176, 0, 48, 47);
    this.flame = guiHelper.createAnimatedRecipeFlame(200);
    this.arrow = guiHelper.createAnimatedRecipeArrow(200);
  }

  @Override
  public void draw(RecipeHolder<CokeOvenRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView,
      GuiGraphics guiGraphics, double mouseX, double mouseY) {
    this.flame.draw(guiGraphics, 1, 3);
    this.arrow.draw(guiGraphics, 20, 21);
  }

  @Override
  public void createRecipeExtras(IRecipeExtrasBuilder builder,
      RecipeHolder<CokeOvenRecipe> recipeHolder, IFocusGroup focuses) {
    var recipe = recipeHolder.value();
    int cookTime = recipe.getCookingTime();
    if (cookTime > 0) {
      int cookTimeSeconds = cookTime / SharedConstants.TICKS_PER_SECOND;
      var timeString =
          Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
      builder.addText(timeString, WIDTH - 70, 50)
          .setTextAlignment(VerticalAlignment.BOTTOM)
          .setTextAlignment(HorizontalAlignment.CENTER)
          .setColor(RailcraftJeiPlugin.TEXT_COLOR);
    }
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<CokeOvenRecipe> recipeHolder,
      IFocusGroup focuses) {
    var recipe = recipeHolder.value();
    var ingredients = recipe.getIngredients();
    builder
        .addInputSlot(1, 20)
        .setStandardSlotBackground()
        .addIngredients(ingredients.getFirst());
    builder
        .addOutputSlot(49, 20)
        .setOutputSlotBackground()
        .addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
    // Not the actual capacity, but is 10000 for a better visibility
    builder.addOutputSlot(78, 1)
        .addIngredient(NeoForgeTypes.FLUID_STACK, recipe.getCreosote())
        .setFluidRenderer(10_000, true, 48, 47)
        .setOverlay(tankOverlay, 0, 0)
        .setBackground(tankBackground, -1, -1);
  }
}

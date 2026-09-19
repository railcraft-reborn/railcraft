package mods.railcraft.integrations.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mods.railcraft.Translations;
import mods.railcraft.integrations.jei.RailcraftJeiPlugin;
import mods.railcraft.integrations.jei.RecipeTypes;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.crafting.BlastFurnaceRecipe;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay;

public class BlastFurnaceRecipeCategory extends
    AbstractRecipeCategory<RecipeHolder<BlastFurnaceRecipe>> {

  private static final int WIDTH = 82;
  private static final int HEIGHT = 54;

  private final IDrawable flame, arrow;

  public BlastFurnaceRecipeCategory(IGuiHelper guiHelper) {
    super(
        RecipeTypes.BLAST_FURNACE,
        Component.translatable(Translations.Jei.BLAST_FURNACE),
        guiHelper.createDrawableItemLike(RailcraftItems.BLAST_FURNACE_BRICKS.get()),
        WIDTH,
        HEIGHT
    );

    this.flame = guiHelper.createAnimatedRecipeFlame(200);
    this.arrow = guiHelper.createAnimatedRecipeArrow(200);
  }

  @Override
  public void draw(RecipeHolder<BlastFurnaceRecipe> recipe, IRecipeSlotsView recipeSlotsView,
      GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
    this.flame.draw(guiGraphics, 1, 20);
    this.arrow.draw(guiGraphics, 25, 19);
  }

  @Override
  public void createRecipeExtras(IRecipeExtrasBuilder builder,
      RecipeHolder<BlastFurnaceRecipe> recipeHolder, IFocusGroup focuses) {
    var recipe = recipeHolder.value();

    if (recipe.display().getFirst() instanceof FurnaceRecipeDisplay furnaceRecipeDisplay) {
      int cookTime = furnaceRecipeDisplay.duration();
      if (cookTime > 0) {
        int cookTimeSeconds = cookTime / SharedConstants.TICKS_PER_SECOND;
        var timeString =
            Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
        builder.addText(timeString, WIDTH, 45)
            .setTextAlignment(VerticalAlignment.BOTTOM)
            .setTextAlignment(HorizontalAlignment.CENTER)
            .setColor(RailcraftJeiPlugin.TEXT_COLOR);
      }
    }
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<BlastFurnaceRecipe> recipeHolder,
      IFocusGroup focuses) {
    var recipe = recipeHolder.value();
    var ingredients = recipe.placementInfo().ingredients();
    builder
        .addInputSlot(1, 1)
        .setStandardSlotBackground()
        .add(ingredients.getFirst());
    builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 1, 37)
        .setStandardSlotBackground();
    builder
        .addOutputSlot(61, 5)
        .setOutputSlotBackground()
        .add(recipe.assemble(null));
    builder.addOutputSlot(61, 37)
        .setStandardSlotBackground()
        .add(new ItemStack(RailcraftItems.SLAG.get(), recipe.getSlagOutput()));
  }
}

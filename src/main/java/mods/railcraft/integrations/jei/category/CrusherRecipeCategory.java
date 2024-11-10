package mods.railcraft.integrations.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.integrations.jei.RecipeTypes;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.crafting.CrusherRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

public class CrusherRecipeCategory extends AbstractRecipeCategory<RecipeHolder<CrusherRecipe>> {

  private static final int WIDTH = 144;
  private static final int HEIGHT = 54;

  private static final ResourceLocation BACKGROUND =
      RailcraftConstants.rl("textures/gui/container/crusher.png");

  private final IDrawable background, arrow;

  public CrusherRecipeCategory(IGuiHelper guiHelper) {
    super(
        RecipeTypes.CRUSHER,
        Component.translatable(Translations.Jei.CRUSHER),
        guiHelper.createDrawableItemLike(RailcraftItems.CRUSHER.get()),
        WIDTH,
        HEIGHT
    );

    this.background = guiHelper.createDrawable(BACKGROUND, 0, 171, WIDTH, HEIGHT);
    this.arrow = guiHelper.createAnimatedDrawable(
        guiHelper.createDrawable(BACKGROUND, 144, 171, 29, 53),
        200, IDrawableAnimated.StartDirection.LEFT, false);
  }

  @Override
  public void draw(RecipeHolder<CrusherRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView,
      GuiGraphics guiGraphics, double mouseX, double mouseY) {
    this.background.draw(guiGraphics);
    this.arrow.draw(guiGraphics, 58, 0);
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<CrusherRecipe> recipeHolder,
      IFocusGroup focuses) {
    var recipe = recipeHolder.value();
    var ingredients = recipe.getIngredients();

    builder
        .addInputSlot(19, 19)
        .addIngredients(ingredients.getFirst());

    var outputs = recipe.getProbabilityOutputs();
    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        int index = 1 + x + (y * 3);
        var itemStack = ItemStack.EMPTY;
        if (outputs.size() > index - 1) {
          itemStack = outputs.get(index - 1).getOutput();
        }
        var recipeLayout = builder
            .addOutputSlot(91 + x * 18, y * 18 + 1)
            .addItemStack(itemStack);
        if (!itemStack.isEmpty()) {
          recipeLayout.addRichTooltipCallback((recipeSlotView, tooltip) -> {
            double probability = outputs.get(index - 1).probability() * 100;
            var probText = Component.translatable(Translations.Jei.CRUSHER_TIP, probability)
                .withStyle(ChatFormatting.GRAY);
            tooltip.add(probText);
          });
        }
      }
    }
  }
}

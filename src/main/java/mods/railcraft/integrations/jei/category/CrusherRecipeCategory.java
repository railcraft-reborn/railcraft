package mods.railcraft.integrations.jei.category;

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
import mods.railcraft.integrations.jei.RecipeTypes;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.crafting.CrusherRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CrusherRecipeCategory implements IRecipeCategory<CrusherRecipe> {

  public static final int WIDTH = 144;
  public static final int HEIGHT = 54;

  public static final ResourceLocation BACKGROUND =
      RailcraftConstants.rl("textures/gui/container/crusher.png");

  private final IDrawable background, icon, arrow;

  public CrusherRecipeCategory(IGuiHelper guiHelper) {
    this.background = guiHelper.createDrawable(BACKGROUND, 0, 171, WIDTH, HEIGHT);
    var itemStack = new ItemStack(RailcraftItems.CRUSHER.get());
    this.icon = guiHelper.createDrawableItemStack(itemStack);

    this.arrow = guiHelper.createAnimatedDrawable(
        guiHelper.createDrawable(BACKGROUND, 144, 171, 29, 53),
        500, IDrawableAnimated.StartDirection.LEFT, false);
  }

  @Override
  public RecipeType<CrusherRecipe> getRecipeType() {
    return RecipeTypes.CRUSHER;
  }

  @Override
  public Component getTitle() {
    return Component.translatable(Translations.Jei.CRUSHER);
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
  public void draw(CrusherRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics,
      double mouseX, double mouseY) {
    this.arrow.draw(guiGraphics, 58, 0);
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, CrusherRecipe recipe, IFocusGroup focuses) {
    var ingredients = recipe.getIngredients();

    builder
        .addSlot(RecipeIngredientRole.INPUT, 19, 19)
        .addIngredients(ingredients.get(0));

    var outputs = recipe.getProbabilityOutputs();
    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        int index = 1 + x + (y * 3);
        var itemStack = ItemStack.EMPTY;
        if (outputs.size() > index - 1) {
          itemStack = outputs.get(index - 1).getOutput();
        }
        var recipeLayout = builder
            .addSlot(RecipeIngredientRole.OUTPUT, 91 + x * 18, y * 18 + 1)
            .addItemStack(itemStack);
        if (!itemStack.isEmpty()) {
          recipeLayout.addTooltipCallback((recipeSlotView, tooltip) -> {
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

package mods.railcraft.integrations.jei;

import java.util.List;
import java.util.function.Consumer;
import org.jspecify.annotations.Nullable;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;

public class DefaultRecipeWrapper <T extends CraftingRecipe> implements ICraftingCategoryExtension<T> {

  private final boolean isShapeless;
  private final Component info;
  @Nullable
  private Consumer<ItemStack> stackModifier;

  DefaultRecipeWrapper(boolean isShapeless) {
    this(isShapeless, Component.empty());
  }

  DefaultRecipeWrapper(boolean isShapeless, Component info) {
    this.isShapeless = isShapeless;
    this.info = info;
  }

  DefaultRecipeWrapper<T> modifyInputs(Consumer<ItemStack> stackModifier) {
    this.stackModifier = stackModifier;
    return this;
  }

  @Override
  public void drawInfo(RecipeHolder<T> recipe, int recipeWidth, int recipeHeight,
      GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
    var font = Minecraft.getInstance().font;
    int stringWidth = font.width(this.info) / 2;
    guiGraphics.text(font, this.info, 82 - stringWidth, 0,
        RailcraftJeiPlugin.TEXT_COLOR, false);
  }

  @Override
  public void setRecipe(RecipeHolder<T> recipeHolder, IRecipeLayoutBuilder builder,
      ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
    if (this.stackModifier == null) {
      ICraftingCategoryExtension.super.setRecipe(recipeHolder, builder, craftingGridHelper,
          focuses);
      return;
    }
    var context = SlotDisplayContext.fromLevel(Minecraft.getInstance().level);
    // Modify copies only, so the stacks backing the recipe are never touched
    var inputs = this.getIngredients(recipeHolder).stream()
        .map(display -> display.resolveForStacks(context).stream()
            .map(stack -> {
              var copy = stack.copy();
              this.stackModifier.accept(copy);
              return copy;
            })
            .toList())
        .toList();
    craftingGridHelper.createAndSetOutputs(builder,
        recipeHolder.value().display().getFirst().result());
    craftingGridHelper.createAndSetInputs(builder, inputs,
        this.getWidth(recipeHolder), this.getHeight(recipeHolder));
  }

  @Override
  public List<SlotDisplay> getIngredients(RecipeHolder<T> recipeHolder) {
    var displays = recipeHolder.value().display();
    if (displays.isEmpty()) {
      return List.of();
    }
    return switch (displays.getFirst()) {
      case ShapedCraftingRecipeDisplay shapedDisplay -> shapedDisplay.ingredients();
      case ShapelessCraftingRecipeDisplay shapelessDisplay -> shapelessDisplay.ingredients();
      default -> List.of();
    };
  }

  @Override
  public int getWidth(RecipeHolder<T> recipeHolder) {
    return this.isShapeless ? 0 : 3;
  }

  @Override
  public int getHeight(RecipeHolder<T> recipeHolder) {
    return this.isShapeless ? 0 : 3;
  }
}

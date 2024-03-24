package mods.railcraft.integrations.jei;

import java.util.List;
import java.util.function.Consumer;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;

public class DefaultRecipeWrapper <T extends CraftingRecipe> implements ICraftingCategoryExtension<T> {

  private final boolean isShapeless;
  private final Component info;
  private Consumer<ItemStack> stackModifier;

  DefaultRecipeWrapper(boolean isShapeless, Component info) {
    this.isShapeless = isShapeless;
    this.info = info;
    this.stackModifier = stack -> {};
  }

  DefaultRecipeWrapper<T> modifyInputs(Consumer<ItemStack> stackModifier) {
    this.stackModifier = stackModifier;
    return this;
  }

  @Override
  public void drawInfo(RecipeHolder<T> recipe, int recipeWidth, int recipeHeight,
      GuiGraphics guiGraphics, double mouseX, double mouseY) {
    var font = Minecraft.getInstance().font;
    int stringWidth = font.width(this.info) / 2;
    guiGraphics.drawString(font, this.info, 82 - stringWidth, 0,
        RailcraftJeiPlugin.TEXT_COLOR, false);
  }

  @Override
  public void setRecipe(RecipeHolder<T> recipeHolder, IRecipeLayoutBuilder builder,
      ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
    var inputs = recipeHolder.value().getIngredients().stream()
        .map(ingredient -> List.of(ingredient.getItems()))
        .toList();
    inputs.forEach(l -> l.forEach(stackModifier));
    var registryAccess = Minecraft.getInstance().level.registryAccess();
    craftingGridHelper.createAndSetOutputs(builder,
        List.of(recipeHolder.value().getResultItem(registryAccess)));
    var width = getWidth(recipeHolder);
    var height = getHeight(recipeHolder);
    craftingGridHelper.createAndSetInputs(builder, inputs, width, height);
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

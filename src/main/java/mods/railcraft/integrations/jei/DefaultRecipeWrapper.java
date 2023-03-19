package mods.railcraft.integrations.jei;

import java.util.List;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;

public class DefaultRecipeWrapper implements ICraftingCategoryExtension {

  private final CustomRecipe recipe;
  private final boolean isShapeless;
  private final Component info;
  private Consumer<ItemStack> stackModifier;

  DefaultRecipeWrapper(CustomRecipe recipe, boolean isShapeless, Component info) {
    this.recipe = recipe;
    this.isShapeless = isShapeless;
    this.info = info;
    this.stackModifier = stack -> {};
  }

  DefaultRecipeWrapper modifyInputs(Consumer<ItemStack> stackModifier) {
    this.stackModifier = stackModifier;
    return this;
  }

  @Nullable
  @Override
  public ResourceLocation getRegistryName() {
    return this.recipe.getId();
  }

  @Override
  public void drawInfo(int recipeWidth, int recipeHeight, PoseStack stack, double mouseX,
      double mouseY) {
    var font = Minecraft.getInstance().font;
    float stringWidth = font.width(this.info) / 2.0f;
    font.draw(stack, this.info, 82 - stringWidth, 0, 0xFF808080);
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper,
      IFocusGroup focuses) {
    var inputs = recipe.getIngredients().stream()
        .map(ingredient -> List.of(ingredient.getItems()))
        .toList();
    inputs.forEach(l -> l.forEach(stackModifier));

    craftingGridHelper.createAndSetOutputs(builder, List.of(recipe.getResultItem()));
    craftingGridHelper.createAndSetInputs(builder, inputs, getWidth(), getHeight());
  }

  @Override
  public int getWidth() {
    return this.isShapeless ? 0 : 3;
  }

  @Override
  public int getHeight() {
    return this.isShapeless ? 0 : 3;
  }
}

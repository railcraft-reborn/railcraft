package mods.railcraft.integrations.emi;

import java.util.List;
import java.util.function.Consumer;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import mods.railcraft.integrations.jei.RailcraftJeiPlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;

public class DefaultRecipeWrapper extends EmiCraftingRecipe {

  private final Component info;
  private Consumer<ItemStack> stackModifier;

  public DefaultRecipeWrapper(CustomRecipe customRecipe, ResourceLocation id, boolean shapeless) {
    this(getInputs(customRecipe), getOutput(customRecipe), id, shapeless, Component.empty());
  }

  public DefaultRecipeWrapper(CustomRecipe customRecipe, ResourceLocation id, boolean shapeless,
      Component info) {
    this(getInputs(customRecipe), getOutput(customRecipe), id, shapeless, info);
  }

  public DefaultRecipeWrapper(List<EmiIngredient> input, EmiStack output, ResourceLocation id,
      boolean shapeless, Component info) {
    super(input, output, id, shapeless);
    this.info = info;
  }

  DefaultRecipeWrapper modifyInputs(Consumer<ItemStack> stackModifier) {
    this.stackModifier = stackModifier;
    return this;
  }

  @Override
  public void addWidgets(WidgetHolder widgets) {
    super.addWidgets(widgets);
    var font = Minecraft.getInstance().font;
    int stringWidth = font.width(this.info) / 2;
    widgets.addText(this.info, 82 - stringWidth, 0, RailcraftJeiPlugin.TEXT_COLOR, false);
  }

  private static List<EmiIngredient> getInputs(CustomRecipe recipe) {
    return recipe.getIngredients().stream()
        .map(EmiIngredient::of)
        .toList();
  }

  private static EmiStack getOutput(CustomRecipe recipe) {
    return EmiStack.of(recipe.getResultItem(null));
  }
}

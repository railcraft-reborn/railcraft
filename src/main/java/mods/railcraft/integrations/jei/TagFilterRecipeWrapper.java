package mods.railcraft.integrations.jei;

import java.util.List;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mods.railcraft.Translations;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.crafting.TagFilterRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

class TagFilterRecipeWrapper extends DefaultRecipeWrapper<TagFilterRecipe> {

  TagFilterRecipeWrapper() {
    super(true, Component.translatable(Translations.Jei.SET_PROTOTYPE));
  }

  @Override
  public void setRecipe(RecipeHolder<TagFilterRecipe> recipeHolder, IRecipeLayoutBuilder builder,
      ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
    var output = craftingGridHelper.createAndSetOutputs(builder,
        TagFilterRecipe.getExampleResults());
    var inputs = craftingGridHelper.createAndSetInputs(builder,
        List.of(List.of(new ItemStack(RailcraftItems.TAG_FILTER.get())),
            TagFilterRecipe.getExamplePrototypes()),
        0, 0);
    builder.createFocusLink(inputs.get(1), output);
  }
}

package mods.railcraft.integrations.jei.category;

import java.util.List;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mods.railcraft.Translations;
import mods.railcraft.integrations.jei.RecipeTypes;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.crafting.RollingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class RollingRecipeCategory implements IRecipeCategory<RollingRecipe> {

  private static final int WIDTH = 116;
  private static final int HEIGHT = 54;

  private static final ResourceLocation CRAFTING_TABLE =
      new ResourceLocation("textures/gui/container/crafting_table.png");

  private final IDrawable background, icon;
  private final ICraftingGridHelper craftingGridHelper;

  public RollingRecipeCategory(IGuiHelper guiHelper) {
    this.background = guiHelper.createDrawable(CRAFTING_TABLE, 29, 16, WIDTH, HEIGHT);
    var itemStack = new ItemStack(RailcraftItems.MANUAL_ROLLING_MACHINE.get());
    this.icon = guiHelper.createDrawableItemStack(itemStack);
    this.craftingGridHelper = guiHelper.createCraftingGridHelper();
  }

  @Override
  public RecipeType<RollingRecipe> getRecipeType() {
    return RecipeTypes.ROLLING_MACHINE;
  }

  @Override
  public Component getTitle() {
    return Component.translatable(Translations.Jei.METAL_ROLLING);
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
  public void setRecipe(IRecipeLayoutBuilder builder, RollingRecipe recipe, IFocusGroup focuses) {
    var registryAccess = Minecraft.getInstance().level.registryAccess();
    this.craftingGridHelper.createAndSetOutputs(builder, List.of(recipe.getResultItem(registryAccess)));
    int width = recipe.getWidth();
    int height = recipe.getHeight();
    var inputs = recipe.getIngredients().stream()
        .map(ingredient -> List.of(ingredient.getItems()))
        .toList();
    this.craftingGridHelper.createAndSetInputs(builder, inputs, width, height);
  }
}

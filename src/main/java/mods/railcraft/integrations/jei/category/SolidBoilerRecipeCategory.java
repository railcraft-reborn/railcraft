package mods.railcraft.integrations.jei.category;

import java.util.List;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.integrations.jei.RailcraftJeiPlugin;
import mods.railcraft.integrations.jei.RecipeTypes;
import mods.railcraft.integrations.jei.recipe.SolidBoilerJEIRecipe;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.material.RailcraftFluids;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

public class SolidBoilerRecipeCategory extends AbstractRecipeCategory<SolidBoilerJEIRecipe> {

  public static final int WIDTH = 117;
  public static final int HEIGHT = 54;

  private static final ResourceLocation BACKGROUND =
      RailcraftConstants.rl("textures/gui/container/solid_fueled_steam_boiler.png");

  private final IDrawable tankBackground, tankOverlay, heatBackground, heatOverlay, flame;

  public SolidBoilerRecipeCategory(IGuiHelper guiHelper) {
    super(
        RecipeTypes.SOLID_BOILER,
        Component.translatable(Translations.Jei.SOLID_BOILER),
        guiHelper.createDrawableItemLike(RailcraftItems.SOLID_FUELED_FIREBOX.get()),
        WIDTH,
        HEIGHT
    );

    this.tankBackground = guiHelper.createDrawable(BACKGROUND, 16, 22, 18, 49);
    this.tankOverlay = guiHelper.createDrawable(BACKGROUND, 176, 0, 16, 47);
    this.heatBackground = guiHelper.createDrawable(BACKGROUND, 39, 24, 8, 45);
    this.heatOverlay = guiHelper.createDrawable(BACKGROUND, 176, 61, 6, 43);
    this.flame = guiHelper.createAnimatedRecipeFlame(200);
  }

  @Override
  public void draw(SolidBoilerJEIRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics,
      double mouseX, double mouseY) {
    this.heatBackground.draw(guiGraphics, 23, 5);
    this.heatOverlay.draw(guiGraphics, 23 + 1, 5 + 1);
    this.flame.draw(guiGraphics, 46, 3);
  }

  @Override
  public void createRecipeExtras(IRecipeExtrasBuilder builder, SolidBoilerJEIRecipe recipe,
      IFocusGroup focuses) {
    var text = recipe.temperature() + "°C";
    builder.addText(Component.literal(text), WIDTH - 10, 50)
        .setTextAlignment(VerticalAlignment.BOTTOM)
        .setTextAlignment(HorizontalAlignment.CENTER)
        .setColor(RailcraftJeiPlugin.TEXT_COLOR);
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, SolidBoilerJEIRecipe recipe, IFocusGroup focuses) {
    builder.addOutputSlot(1, 4)
        .addIngredient(NeoForgeTypes.FLUID_STACK, recipe.steam())
        .setFluidRenderer(10_000, true, 16, 47)
        .setOverlay(tankOverlay, 0, 0)
        .setBackground(tankBackground, -1, -1);
    builder.addInputSlot(100, 4)
        .addIngredient(NeoForgeTypes.FLUID_STACK, recipe.water())
        .setFluidRenderer(10_000, true, 16, 47)
        .setOverlay(tankOverlay, 0, 0)
        .setBackground(tankBackground, -1, -1);

    builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 46, 20)
        .setStandardSlotBackground();
  }

  public static List<SolidBoilerJEIRecipe> getBoilerRecipes() {
    // Not the actual capacity, but is 10000 for a better visibility
    return List.of(
        new SolidBoilerJEIRecipe(new FluidStack(Fluids.WATER, 10_000),
            new FluidStack(RailcraftFluids.STEAM.get(), 10_000), 100)
    );
  }
}

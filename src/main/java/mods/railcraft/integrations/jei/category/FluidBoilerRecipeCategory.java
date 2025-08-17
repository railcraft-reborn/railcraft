package mods.railcraft.integrations.jei.category;

import java.util.List;
import java.util.stream.Stream;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.integrations.jei.RailcraftJeiPlugin;
import mods.railcraft.integrations.jei.RecipeTypes;
import mods.railcraft.integrations.jei.recipe.FluidBoilerJEIRecipe;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.material.RailcraftFluids;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

public class FluidBoilerRecipeCategory extends AbstractRecipeCategory<FluidBoilerJEIRecipe> {

  public static final int WIDTH = 117;
  public static final int HEIGHT = 54;

  public static final ResourceLocation BACKGROUND =
      RailcraftConstants.rl("textures/gui/container/fluid_fueled_steam_boiler.png");

  private final IDrawable tankBackground, tankOverlay, heatBackground, heatOverlay, flame;

  public FluidBoilerRecipeCategory(IGuiHelper guiHelper) {
    super(
        RecipeTypes.FLUID_BOILER,
        Component.translatable(Translations.Jei.FLUID_BOILER),
        guiHelper.createDrawableItemLike(RailcraftItems.FLUID_FUELED_FIREBOX.get()),
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
  public void draw(FluidBoilerJEIRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics,
      double mouseX, double mouseY) {
    this.heatBackground.draw(guiGraphics, 23, 5);
    this.heatOverlay.draw(guiGraphics, 23 + 1, 5 + 1);
    this.flame.draw(guiGraphics, 46, 19);
  }

  @Override
  public void createRecipeExtras(IRecipeExtrasBuilder builder, FluidBoilerJEIRecipe recipe,
      IFocusGroup focuses) {
    var text = recipe.temperature() + "°C";
    builder.addText(Component.literal(text), WIDTH - 10, 50)
        .setTextAlignment(VerticalAlignment.BOTTOM)
        .setTextAlignment(HorizontalAlignment.CENTER)
        .setColor(RailcraftJeiPlugin.TEXT_COLOR);
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, FluidBoilerJEIRecipe recipe, IFocusGroup focuses) {
    builder.addOutputSlot(1, 4)
        .addFluidStack(recipe.steam(), 1000)
        .setFluidRenderer(1000, true, 16, 47)
        .setOverlay(tankOverlay, 0, 0)
        .setBackground(tankBackground, -1, -1);
    builder.addInputSlot(73, 4)
        .addIngredients(NeoForgeTypes.FLUID_STACK,
            Stream.of(recipe.fuel().getStacks())
                .map(x -> x.copyWithAmount(1000))
                .toList())
        .setFluidRenderer(1000, true, 16, 47)
        .setOverlay(tankOverlay, 0, 0)
        .setBackground(tankBackground, -1, -1);
    builder.addInputSlot(100, 4)
        .addFluidStack(recipe.water(), 1000)
        .setFluidRenderer(1000, true, 16, 47)
        .setOverlay(tankOverlay, 0, 0)
        .setBackground(tankBackground, -1, -1);
  }

  public static List<FluidBoilerJEIRecipe> getBoilerRecipes() {
    return List.of(
        new FluidBoilerJEIRecipe(FluidIngredient.tag(RailcraftTags.Fluids.CREOSOTE),
            Fluids.WATER,
            RailcraftFluids.STEAM.get(), 100)
    );
  }
}

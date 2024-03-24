package mods.railcraft.integrations.jei.category;

import java.util.List;
import mezz.jei.api.forge.ForgeTypes;
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
import mods.railcraft.integrations.jei.RailcraftJeiPlugin;
import mods.railcraft.integrations.jei.RecipeTypes;
import mods.railcraft.integrations.jei.recipe.SolidBoilerJEIRecipe;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.material.RailcraftFluids;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

public class SolidBoilerRecipeCategory implements IRecipeCategory<SolidBoilerJEIRecipe> {

  public static final int WIDTH = 117;
  public static final int HEIGHT = 54;

  public static final ResourceLocation BACKGROUND =
      RailcraftConstants.rl("textures/gui/container/solid_fueled_steam_boiler.png");

  private final IDrawable background, icon, flame, bar;

  public SolidBoilerRecipeCategory(IGuiHelper guiHelper) {
    this.background = guiHelper.createDrawable(BACKGROUND, 16, 19, WIDTH, HEIGHT);
    var itemStack = new ItemStack(RailcraftItems.SOLID_FUELED_FIREBOX.get());
    this.icon = guiHelper.createDrawableItemStack(itemStack);

    this.flame = guiHelper.createAnimatedDrawable(
        guiHelper.createDrawable(BACKGROUND, 176, 47, 14, 14),
        200, IDrawableAnimated.StartDirection.TOP, true);
    this.bar = guiHelper.createDrawable(BACKGROUND, 176, 61, 6, 43);
  }

  @Override
  public RecipeType<SolidBoilerJEIRecipe> getRecipeType() {
    return RecipeTypes.SOLID_BOILER;
  }

  @Override
  public Component getTitle() {
    return Component.translatable(Translations.Jei.SOLID_BOILER);
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
  public void draw(SolidBoilerJEIRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics,
      double mouseX, double mouseY) {
    this.flame.draw(guiGraphics, 46, 3);
    this.bar.draw(guiGraphics, 24, 6);

    var font = Minecraft.getInstance().font;
    var temp = recipe.temperature() + "°C";
    guiGraphics.drawString(font, temp, getBackground().getWidth() - font.width(temp) - 50, 43,
        RailcraftJeiPlugin.TEXT_COLOR, false);
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, SolidBoilerJEIRecipe recipe, IFocusGroup focuses) {
    builder.addSlot(RecipeIngredientRole.OUTPUT, 1, 4)
        .addIngredient(ForgeTypes.FLUID_STACK, recipe.steam())
        .setFluidRenderer(10_000, true, 16, 47);
    builder.addSlot(RecipeIngredientRole.INPUT, 100, 4)
        .addIngredient(ForgeTypes.FLUID_STACK, recipe.water())
        .setFluidRenderer(10_000, true, 16, 47);
  }

  public static List<SolidBoilerJEIRecipe> getBoilerRecipes() {
    // Not the actual capacity, but is 10000 for a better visibility
    return List.of(
        new SolidBoilerJEIRecipe(new FluidStack(Fluids.WATER, 10_000),
            new FluidStack(RailcraftFluids.STEAM.get(), 10_000), 100)
    );
  }
}

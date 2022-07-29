package mods.railcraft.integrations.jei;

import java.util.Objects;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mods.railcraft.Railcraft;
import mods.railcraft.client.gui.screen.inventory.CokeOvenScreen;
import mods.railcraft.client.gui.screen.inventory.ManualRollingMachineScreen;
import mods.railcraft.integrations.jei.category.CokeOvenRecipeCategory;
import mods.railcraft.integrations.jei.category.RollingRecipeCategory;
import mods.railcraft.world.inventory.CokeOvenMenu;
import mods.railcraft.world.inventory.ManualRollingMachineMenu;
import mods.railcraft.world.inventory.RailcraftMenuTypes;
import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class RailcraftJeiPlugin implements IModPlugin {

  @Override
  public ResourceLocation getPluginUid() {
    return new ResourceLocation(Railcraft.ID, "jei_plugin");
  }

  @Override
  public void registerCategories(IRecipeCategoryRegistration registration) {
    var guiHelper = registration.getJeiHelpers().getGuiHelper();
    registration.addRecipeCategories(new RollingRecipeCategory(guiHelper));
    registration.addRecipeCategories(new CokeOvenRecipeCategory(guiHelper));
  }

  @Override
  public void registerGuiHandlers(IGuiHandlerRegistration registration) {
    registration.addRecipeClickArea(ManualRollingMachineScreen.class, 90, 45, 23, 9,
        RecipeTypes.ROLLING_MACHINE);
    registration.addRecipeClickArea(CokeOvenScreen.class, 34, 43, 20, 16, RecipeTypes.COKE_OVEN);
  }

  @Override
  public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
    registration.addRecipeTransferHandler(ManualRollingMachineMenu.class,
        RailcraftMenuTypes.MANUAL_ROLLING_MACHINE.get(),
        RecipeTypes.ROLLING_MACHINE, 2, 9, 11, 36);
    registration.addRecipeTransferHandler(CokeOvenMenu.class, RailcraftMenuTypes.COKE_OVEN.get(),
        RecipeTypes.COKE_OVEN, 0, 1, 4, 36);
  }

  @Override
  public void registerRecipes(IRecipeRegistration registration) {
    var recipeManager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
    registration.addRecipes(RecipeTypes.ROLLING_MACHINE,
        recipeManager.getAllRecipesFor(RailcraftRecipeTypes.ROLLING.get()));
    registration.addRecipes(RecipeTypes.COKE_OVEN,
        recipeManager.getAllRecipesFor(RailcraftRecipeTypes.COKING.get()));
  }
}

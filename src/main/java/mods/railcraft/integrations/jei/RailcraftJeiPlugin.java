package mods.railcraft.integrations.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.screen.inventory.BlastFurnaceScreen;
import mods.railcraft.client.gui.screen.inventory.CokeOvenScreen;
import mods.railcraft.client.gui.screen.inventory.CrusherScreen;
import mods.railcraft.client.gui.screen.inventory.ManualRollingMachineScreen;
import mods.railcraft.client.gui.screen.inventory.PoweredRollingMachineScreen;
import mods.railcraft.client.gui.screen.inventory.RailcraftMenuScreen;
import mods.railcraft.client.gui.screen.inventory.SteamOvenScreen;
import mods.railcraft.integrations.jei.category.BlastFurnaceRecipeCategory;
import mods.railcraft.integrations.jei.category.CokeOvenRecipeCategory;
import mods.railcraft.integrations.jei.category.CrusherRecipeCategory;
import mods.railcraft.integrations.jei.category.FluidBoilerRecipeCategory;
import mods.railcraft.integrations.jei.category.RollingRecipeCategory;
import mods.railcraft.integrations.jei.category.SolidBoilerRecipeCategory;
import mods.railcraft.world.inventory.BlastFurnaceMenu;
import mods.railcraft.world.inventory.CokeOvenMenu;
import mods.railcraft.world.inventory.CrusherMenu;
import mods.railcraft.world.inventory.ManualRollingMachineMenu;
import mods.railcraft.world.inventory.PoweredRollingMachineMenu;
import mods.railcraft.world.inventory.RailcraftMenuTypes;
import mods.railcraft.world.inventory.SteamOvenMenu;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.crafting.CartDisassemblyRecipe;
import mods.railcraft.world.item.crafting.LocomotivePaintingRecipe;
import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import mods.railcraft.world.item.crafting.RotorRepairRecipe;
import mods.railcraft.world.item.crafting.TicketDuplicateRecipe;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.registries.DeferredHolder;

@JeiPlugin
public class RailcraftJeiPlugin implements IModPlugin {

  public static final int TEXT_COLOR = 0xFF808080;

  @Override
  public ResourceLocation getPluginUid() {
    return RailcraftConstants.rl("jei_plugin");
  }

  @Override
  public void registerCategories(IRecipeCategoryRegistration registration) {
    var guiHelper = registration.getJeiHelpers().getGuiHelper();
    registration.addRecipeCategories(new RollingRecipeCategory(guiHelper));
    registration.addRecipeCategories(new CokeOvenRecipeCategory(guiHelper));
    registration.addRecipeCategories(new BlastFurnaceRecipeCategory(guiHelper));
    registration.addRecipeCategories(new CrusherRecipeCategory(guiHelper));
    registration.addRecipeCategories(new SolidBoilerRecipeCategory(guiHelper));
    registration.addRecipeCategories(new FluidBoilerRecipeCategory(guiHelper));
  }

  @Override
  public void registerGuiHandlers(IGuiHandlerRegistration registration) {
    registration.addRecipeClickArea(ManualRollingMachineScreen.class, 90, 45, 23, 9,
        RecipeTypes.ROLLING_MACHINE);
    registration.addRecipeClickArea(PoweredRollingMachineScreen.class, 90, 36, 23, 9,
        RecipeTypes.ROLLING_MACHINE);
    registration.addRecipeClickArea(CokeOvenScreen.class, 34, 43, 20, 16, RecipeTypes.COKE_OVEN);
    registration.addRecipeClickArea(BlastFurnaceScreen.class, 80, 36, 22, 15,
        RecipeTypes.BLAST_FURNACE);
    registration.addRecipeClickArea(CrusherScreen.class, 73, 20, 30, 38, RecipeTypes.CRUSHER);
    registration.addRecipeClickArea(SteamOvenScreen.class, 65, 18, 23, 50,
        mezz.jei.api.constants.RecipeTypes.SMELTING);
    registration.addGhostIngredientHandler(RailcraftMenuScreen.class, new GhostIngredientHandler<>());
  }

  @Override
  public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
    registration.addRecipeTransferHandler(ManualRollingMachineMenu.class,
        RailcraftMenuTypes.MANUAL_ROLLING_MACHINE.get(),
        RecipeTypes.ROLLING_MACHINE, 2, 9, 11, 36);
    registration.addRecipeTransferHandler(PoweredRollingMachineMenu.class,
        RailcraftMenuTypes.POWERED_ROLLING_MACHINE.get(),
        RecipeTypes.ROLLING_MACHINE, 2, 9, 11, 36);
    registration.addRecipeTransferHandler(CokeOvenMenu.class, RailcraftMenuTypes.COKE_OVEN.get(),
        RecipeTypes.COKE_OVEN, 0, 1, 4, 36);
    registration.addRecipeTransferHandler(BlastFurnaceMenu.class,
        RailcraftMenuTypes.BLAST_FURNACE.get(), RecipeTypes.BLAST_FURNACE, 0, 1, 4, 36);
    registration.addRecipeTransferHandler(CrusherMenu.class, RailcraftMenuTypes.CRUSHER.get(),
        RecipeTypes.CRUSHER, 0, 9, 17, 36);
    registration.addRecipeTransferHandler(SteamOvenMenu.class,
        RailcraftMenuTypes.STEAM_OVEN.get(),
        mezz.jei.api.constants.RecipeTypes.SMELTING, 0, 9, 9, 36);
  }

  @Override
  public void registerRecipes(IRecipeRegistration registration) {
    var recipeManager = Minecraft.getInstance().level.getRecipeManager();
    registration.addRecipes(RecipeTypes.ROLLING_MACHINE,
        recipeManager.getAllRecipesFor(RailcraftRecipeTypes.ROLLING.get()).stream()
            .map(RecipeHolder::value).toList());
    registration.addRecipes(RecipeTypes.COKE_OVEN,
        recipeManager.getAllRecipesFor(RailcraftRecipeTypes.COKING.get()).stream()
            .map(RecipeHolder::value).toList());
    registration.addRecipes(RecipeTypes.BLAST_FURNACE,
        recipeManager.getAllRecipesFor(RailcraftRecipeTypes.BLASTING.get()).stream()
            .map(RecipeHolder::value).toList());
    registration.addRecipes(RecipeTypes.CRUSHER,
        recipeManager.getAllRecipesFor(RailcraftRecipeTypes.CRUSHING.get()).stream()
            .map(RecipeHolder::value).toList());
    registration.addRecipes(RecipeTypes.SOLID_BOILER, SolidBoilerRecipeCategory.getBoilerRecipes());
    registration.addRecipes(RecipeTypes.FLUID_BOILER, FluidBoilerRecipeCategory.getBoilerRecipes());

    RailcraftBlocks.entries()
        .stream()
        .map(DeferredHolder::get)
        .filter(JeiSearchable.class::isInstance)
        .forEach(x ->
            registration.addItemStackInfo(new ItemStack(x), ((JeiSearchable)x).jeiDescription()));
    RailcraftItems.entries()
        .stream()
        .map(DeferredHolder::get)
        .filter(JeiSearchable.class::isInstance)
        .forEach(x ->
            registration.addItemStackInfo(new ItemStack(x), ((JeiSearchable)x).jeiDescription()));
  }

  @Override
  public void registerVanillaCategoryExtensions(
      IVanillaCategoryExtensionRegistration registration) {
    var craftingCategory = registration.getCraftingCategory();
    craftingCategory.addExtension(LocomotivePaintingRecipe.class,
        new DefaultRecipeWrapper<>(false, Component.translatable(Translations.Jei.PAINT)));
    craftingCategory.addExtension(TicketDuplicateRecipe.class,
        new DefaultRecipeWrapper<>(true, Component.translatable(Translations.Jei.COPY_TAG)));
    craftingCategory.addExtension(RotorRepairRecipe.class,
        new DefaultRecipeWrapper<>(true, Component.translatable(Translations.Jei.REPAIR))
            .modifyInputs(stack -> {
              if (stack.is(RailcraftItems.TURBINE_ROTOR.get())) {
                stack.setDamageValue(RotorRepairRecipe.REPAIR_PER_BLADE);
              }
            }));
    craftingCategory.addExtension(CartDisassemblyRecipe.class,
        new DefaultRecipeWrapper<>(true, Component.translatable(Translations.Jei.SPLIT)) {
          @Override
          public void drawInfo(RecipeHolder<CartDisassemblyRecipe> recipe, int recipeWidth,
              int recipeHeight, GuiGraphics guiGraphics, double mouseX, double mouseY) {
            super.drawInfo(recipe, recipeWidth, recipeHeight, guiGraphics, mouseX, mouseY);
            var drawable = registration.getJeiHelpers().getGuiHelper()
                .createDrawableItemStack(new ItemStack(Items.MINECART));
            drawable.draw(guiGraphics, 65, 35);
          }
        });
  }

  @Override
  public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
    registration.addRecipeCatalyst(new ItemStack(RailcraftItems.MANUAL_ROLLING_MACHINE.get()),
        RecipeTypes.ROLLING_MACHINE);
    registration.addRecipeCatalyst(new ItemStack(RailcraftItems.POWERED_ROLLING_MACHINE.get()),
        RecipeTypes.ROLLING_MACHINE);
    registration.addRecipeCatalyst(new ItemStack(RailcraftItems.COKE_OVEN_BRICKS.get()),
        RecipeTypes.COKE_OVEN);
    registration.addRecipeCatalyst(new ItemStack(RailcraftItems.BLAST_FURNACE_BRICKS.get()),
        RecipeTypes.BLAST_FURNACE);
    registration.addRecipeCatalyst(new ItemStack(RailcraftItems.CRUSHER.get()),
        RecipeTypes.CRUSHER);
    registration.addRecipeCatalyst(new ItemStack(RailcraftItems.STEAM_OVEN.get()),
        mezz.jei.api.constants.RecipeTypes.SMELTING);
    registration.addRecipeCatalyst(new ItemStack(RailcraftItems.SOLID_FUELED_FIREBOX.get()),
        RecipeTypes.SOLID_BOILER);
    registration.addRecipeCatalyst(new ItemStack(RailcraftItems.HIGH_PRESSURE_STEAM_BOILER_TANK.get()),
        RecipeTypes.SOLID_BOILER);
    registration.addRecipeCatalyst(new ItemStack(RailcraftItems.LOW_PRESSURE_STEAM_BOILER_TANK.get()),
        RecipeTypes.SOLID_BOILER);
    registration.addRecipeCatalyst(new ItemStack(RailcraftItems.FLUID_FUELED_FIREBOX.get()),
        RecipeTypes.FLUID_BOILER);
    registration.addRecipeCatalyst(new ItemStack(RailcraftItems.HIGH_PRESSURE_STEAM_BOILER_TANK.get()),
        RecipeTypes.FLUID_BOILER);
    registration.addRecipeCatalyst(new ItemStack(RailcraftItems.LOW_PRESSURE_STEAM_BOILER_TANK.get()),
        RecipeTypes.FLUID_BOILER);
  }
}

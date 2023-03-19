package mods.railcraft.integrations.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import mods.railcraft.Railcraft;
import mods.railcraft.Translations;
import mods.railcraft.client.gui.screen.inventory.BlastFurnaceScreen;
import mods.railcraft.client.gui.screen.inventory.CokeOvenScreen;
import mods.railcraft.client.gui.screen.inventory.CrusherScreen;
import mods.railcraft.client.gui.screen.inventory.ManualRollingMachineScreen;
import mods.railcraft.client.gui.screen.inventory.PoweredRollingMachineScreen;
import mods.railcraft.integrations.jei.category.BlastFurnaceRecipeCategory;
import mods.railcraft.integrations.jei.category.CokeOvenRecipeCategory;
import mods.railcraft.integrations.jei.category.CrusherRecipeCategory;
import mods.railcraft.integrations.jei.category.RollingRecipeCategory;
import mods.railcraft.world.inventory.BlastFurnaceMenu;
import mods.railcraft.world.inventory.CokeOvenMenu;
import mods.railcraft.world.inventory.CrusherMenu;
import mods.railcraft.world.inventory.ManualRollingMachineMenu;
import mods.railcraft.world.inventory.PoweredRollingMachineMenu;
import mods.railcraft.world.inventory.RailcraftMenuTypes;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.crafting.LocomotivePaintingRecipe;
import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import mods.railcraft.world.item.crafting.RotorRepairRecipe;
import mods.railcraft.world.item.crafting.TicketDuplicateRecipe;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

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
    registration.addRecipeCategories(new BlastFurnaceRecipeCategory(guiHelper));
    registration.addRecipeCategories(new CrusherRecipeCategory(guiHelper));
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
  }

  @Override
  public void registerRecipes(IRecipeRegistration registration) {
    var minecraft = Minecraft.getInstance();
    var recipeManager = minecraft.level.getRecipeManager();
    registration.addRecipes(RecipeTypes.ROLLING_MACHINE,
        recipeManager.getAllRecipesFor(RailcraftRecipeTypes.ROLLING.get()));
    registration.addRecipes(RecipeTypes.COKE_OVEN,
        recipeManager.getAllRecipesFor(RailcraftRecipeTypes.COKING.get()));
    registration.addRecipes(RecipeTypes.BLAST_FURNACE,
        recipeManager.getAllRecipesFor(RailcraftRecipeTypes.BLASTING.get()));
    registration.addRecipes(RecipeTypes.CRUSHER,
        recipeManager.getAllRecipesFor(RailcraftRecipeTypes.CRUSHING.get()));

    RailcraftBlocks.entries()
        .stream()
        .filter(x -> x.get() instanceof JeiSearchable)
        .map(RegistryObject::get)
        .forEach(x ->
            registration.addItemStackInfo(new ItemStack(x), ((JeiSearchable)x).addJeiInfo()));
    RailcraftItems.entries()
        .stream()
        .filter(x -> x.get() instanceof JeiSearchable)
        .map(RegistryObject::get)
        .forEach(x ->
            registration.addItemStackInfo(new ItemStack(x), ((JeiSearchable)x).addJeiInfo()));
  }


  @Override
  public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
    var craftingCategory = registration.getCraftingCategory();
    craftingCategory.addCategoryExtension(LocomotivePaintingRecipe.class,
        r -> new DefaultRecipeWrapper(r, false, Component.translatable(Translations.Jei.PAINT)));
    craftingCategory.addCategoryExtension(TicketDuplicateRecipe.class,
        r -> new DefaultRecipeWrapper(r, true, Component.translatable(Translations.Jei.COPY_TAG)));
    craftingCategory.addCategoryExtension(RotorRepairRecipe.class,
        r -> new DefaultRecipeWrapper(r, true, Component.translatable(Translations.Jei.REPAIR))
            .modifyInputs(stack -> {
              if (stack.is(RailcraftItems.TURBINE_ROTOR.get())) {
                stack.setDamageValue(RotorRepairRecipe.REPAIR_PER_BLADE);
              }
            }));
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
  }
}

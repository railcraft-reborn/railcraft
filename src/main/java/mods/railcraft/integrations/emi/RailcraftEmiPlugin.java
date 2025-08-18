package mods.railcraft.integrations.emi;

import java.util.function.Function;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiStack;
import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.crafting.CartDisassemblyRecipe;
import mods.railcraft.world.item.crafting.LocomotivePaintingRecipe;
import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import mods.railcraft.world.item.crafting.RotorRepairRecipe;
import mods.railcraft.world.item.crafting.TicketDuplicateRecipe;
import mods.railcraft.world.item.crafting.TieRecipe;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;

@EmiEntrypoint
public class RailcraftEmiPlugin implements EmiPlugin {

  private static final EmiStack MANUAL_ROLLING_MACHINE =
      EmiStack.of(RailcraftBlocks.MANUAL_ROLLING_MACHINE.get());
  public static final EmiRecipeCategory ROLLING_CATEGORY =
      new EmiRecipeCategory(RailcraftConstants.rl("rolling_category"), MANUAL_ROLLING_MACHINE);

  private static final EmiStack CRUSHER =
      EmiStack.of(RailcraftBlocks.CRUSHER.get());
  public static final EmiRecipeCategory CRUSHING_CATEGORY =
      new EmiRecipeCategory(RailcraftConstants.rl("crushing_category"), CRUSHER);

  private static final EmiStack COKE_OVEN =
      EmiStack.of(RailcraftBlocks.COKE_OVEN_BRICKS.get());
  public static final EmiRecipeCategory COKING_CATEGORY =
      new EmiRecipeCategory(RailcraftConstants.rl("coking_category"), COKE_OVEN);

  private static final EmiStack BLAST_FURNACE =
      EmiStack.of(RailcraftBlocks.BLAST_FURNACE_BRICKS.get());
  public static final EmiRecipeCategory BLASTING_CATEGORY =
      new EmiRecipeCategory(RailcraftConstants.rl("blasting_category"), BLAST_FURNACE);

  @Override
  public void register(EmiRegistry registry) {
    // Tell EMI to add a tab for your category
    registry.addCategory(ROLLING_CATEGORY);
    registry.addCategory(CRUSHING_CATEGORY);
    registry.addCategory(COKING_CATEGORY);
    registry.addCategory(BLASTING_CATEGORY);

    // Add all the workstations your category uses
    registry.addWorkstation(ROLLING_CATEGORY, MANUAL_ROLLING_MACHINE);
    registry.addWorkstation(ROLLING_CATEGORY,
        EmiStack.of(RailcraftBlocks.POWERED_ROLLING_MACHINE.get()));
    registry.addWorkstation(CRUSHING_CATEGORY, CRUSHER);
    registry.addWorkstation(COKING_CATEGORY, COKE_OVEN);
    registry.addWorkstation(BLASTING_CATEGORY, BLAST_FURNACE);
    registry.addWorkstation(VanillaEmiRecipeCategories.SMELTING,
        EmiStack.of(RailcraftBlocks.STEAM_OVEN.get()));

    registerRecipe(registry, RailcraftRecipeTypes.ROLLING.get(), RollingEmiRecipe::new);
    registerRecipe(registry, RailcraftRecipeTypes.CRUSHING.get(), CrusherEmiRecipe::new);
    registerRecipe(registry, RailcraftRecipeTypes.COKING.get(), CokeOvenEmiRecipe::new);
    registerRecipe(registry, RailcraftRecipeTypes.BLASTING.get(), BlastFurnaceEmiRecipe::new);

    for (var recipe : registry.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING)) {
      var id = recipe.id();
      switch (recipe.value()) {
        case TieRecipe r -> registry.addRecipe(new DefaultRecipeWrapper(r, id, false));
        case LocomotivePaintingRecipe r -> registry.addRecipe(new DefaultRecipeWrapper(r, id,
            false, Component.translatable(Translations.Jei.PAINT)));
        case TicketDuplicateRecipe r -> registry.addRecipe(new DefaultRecipeWrapper(r, id, true,
            Component.translatable(Translations.Jei.COPY_TAG)));
        case RotorRepairRecipe r -> registry.addRecipe(
            new DefaultRecipeWrapper(r, id, true, Component.translatable(Translations.Jei.REPAIR))
                .modifyInputs(stack -> {
                  if (stack.is(RailcraftItems.TURBINE_ROTOR.get())) {
                    stack.setDamageValue(RotorRepairRecipe.REPAIR_PER_BLADE);
                  }
                }));
        case CartDisassemblyRecipe r -> registry.addRecipe(new DefaultRecipeWrapper(r, id, true,
            Component.translatable(Translations.Jei.SPLIT)));
        default -> {}
      }
    }
  }

  private <C extends RecipeInput, T extends Recipe<C>> void registerRecipe(
      EmiRegistry registry, RecipeType<T> recipeType,
      Function<RecipeHolder<T>, EmiRecipe> emiRecipeFunction) {
    var manager = registry.getRecipeManager();
    for (var x : manager.getAllRecipesFor(recipeType)) {
      registry.addRecipe(emiRecipeFunction.apply(x));
    }
  }
}

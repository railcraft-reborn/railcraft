package mods.railcraft.world.item.crafting;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RailcraftRecipeTypes {

  private static final DeferredRegister<RecipeType<?>> deferredRegister =
      DeferredRegister.create(Registries.RECIPE_TYPE, RailcraftConstants.ID);

  public static final DeferredHolder<RecipeType<?>, RecipeType<RollingRecipe>> ROLLING =
      register("rolling");

  public static final DeferredHolder<RecipeType<?>, RecipeType<CokeOvenRecipe>> COKING =
      register("coking");

  public static final DeferredHolder<RecipeType<?>, RecipeType<BlastFurnaceRecipe>> BLASTING =
      register("blasting");

  public static final DeferredHolder<RecipeType<?>, RecipeType<CrusherRecipe>> CRUSHING =
      register("crushing");

  public static final DeferredHolder<RecipeType<?>, RecipeType<TicketDuplicateRecipe>> TICKET_DUPLICATE =
      register("ticket_duplicate");

  public static final DeferredHolder<RecipeType<?>, RecipeType<LocomotivePaintingRecipe>> LOCOMOTIVE_PAINTING =
      register("locomotive_painting");

  public static final DeferredHolder<RecipeType<?>, RecipeType<RotorRepairRecipe>> ROTOR_REPAIR =
      register("rotor_repair");

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  private static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> register(String name) {
    return deferredRegister.register(name, () -> new RecipeType<T>() {
      @Override
      public String toString() {
        return RailcraftConstants.rl(name).toString();
      }
    });
  }
}

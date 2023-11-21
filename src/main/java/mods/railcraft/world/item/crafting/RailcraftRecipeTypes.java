package mods.railcraft.world.item.crafting;

import java.util.function.Supplier;
import mods.railcraft.Railcraft;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RailcraftRecipeTypes {

  private static final DeferredRegister<RecipeType<?>> deferredRegister =
      DeferredRegister.create(Registries.RECIPE_TYPE, RailcraftConstants.ID);

  public static final Supplier<RecipeType<RollingRecipe>> ROLLING =
      register("rolling");

  public static final Supplier<RecipeType<CokeOvenRecipe>> COKING =
      register("coking");

  public static final Supplier<RecipeType<BlastFurnaceRecipe>> BLASTING =
      register("blasting");

  public static final Supplier<RecipeType<CrusherRecipe>> CRUSHING =
      register("crushing");

  public static final Supplier<RecipeType<TicketDuplicateRecipe>> TICKET_DUPLICATE =
      register("ticket_duplicate");

  public static final Supplier<RecipeType<LocomotivePaintingRecipe>> LOCOMOTIVE_PAINTING =
      register("locomotive_painting");

  public static final Supplier<RecipeType<RotorRepairRecipe>> ROTOR_REPAIR =
      register("rotor_repair");

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  private static <T extends Recipe<?>> Supplier<RecipeType<T>> register(String name) {
    return deferredRegister.register(name, () -> new RecipeType<T>() {
      @Override
      public String toString() {
        return Railcraft.rl(name).toString();
      }
    });
  }
}

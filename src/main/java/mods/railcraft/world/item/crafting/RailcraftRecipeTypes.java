package mods.railcraft.world.item.crafting;

import mods.railcraft.Railcraft;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftRecipeTypes {

  private static final DeferredRegister<RecipeType<?>> deferredRegister =
      DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, Railcraft.ID);

  public static final RegistryObject<RecipeType<RollingRecipe>> ROLLING =
      register("rolling");

  public static final RegistryObject<RecipeType<CokeOvenRecipe>> COKING =
      register("coking");

  public static final RegistryObject<RecipeType<BlastFurnaceRecipe>> BLASTING =
      register("blasting");

  public static final RegistryObject<RecipeType<CrusherRecipe>> CRUSHING =
      register("crushing");

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  private static <T extends Recipe<?>> RegistryObject<RecipeType<T>> register(String name) {
    return deferredRegister.register(name, () -> new RecipeType<T>() {
      @Override
      public String toString() {
        return Railcraft.ID + ResourceLocation.NAMESPACE_SEPARATOR + name;
      }
    });
  }
}

package mods.railcraft.world.item.crafting;

import mods.railcraft.Railcraft;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftRecipeSerializers {

  private static final DeferredRegister<RecipeSerializer<?>> deferredRegister =
      DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Railcraft.ID);

  public static final RegistryObject<RecipeSerializer<?>> ROLLING =
      deferredRegister.register("rolling", RollingRecipe.Serializer::new);

  public static final RegistryObject<RecipeSerializer<?>> COKING =
      deferredRegister.register("coking", CokeOvenRecipe.Serializer::new);

  public static final RegistryObject<RecipeSerializer<?>> BLASTING =
      deferredRegister.register("blasting", BlastFurnaceRecipe.Serializer::new);

  public static final RegistryObject<RecipeSerializer<?>> CRUSHER =
      deferredRegister.register("crusher", CrusherRecipe.Serializer::new);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }
}

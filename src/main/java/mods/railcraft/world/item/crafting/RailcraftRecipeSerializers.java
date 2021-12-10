package mods.railcraft.world.item.crafting;

import mods.railcraft.Railcraft;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Railcraft {@link IRecipeSerializer} registrar.
 *
 * @author LetterN (https://github.com/LetterN)
 */
public class RailcraftRecipeSerializers {

  public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
      DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Railcraft.ID);

  public static final RegistryObject<RecipeSerializer<?>> ROLLING =
      RECIPE_SERIALIZERS.register("rolling", RollingRecipe.RollingRecipeSerializer::new);

  public static final RegistryObject<RecipeSerializer<?>> COKE_OVEN_COOKING =
      RECIPE_SERIALIZERS.register("coke_oven_cooking",
          CokeOvenRecipe.CokeOvenRecipeSerializer::new);
}

package mods.railcraft.world.item.crafting;

import mods.railcraft.Railcraft;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Railcraft {@link IRecipeSerializer} registrar.
 *
 * @author LetterN (https://github.com/LetterN)
 */
public class RailcraftRecipeSerializers {

  public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS =
      DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Railcraft.ID);

  public static final RegistryObject<IRecipeSerializer<?>> ROLLING =
      RECIPE_SERIALIZERS.register("rolling", RollingRecipe.RollingRecipeSerializer::new);
}

package mods.railcraft.crafting;

import mods.railcraft.Railcraft;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Railcraft Recipie registrar
 *
 * @author LetterN (https://github.com/LetterN)
 */
public class RailcraftRecipies {

  public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS =
    DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Railcraft.ID);

  public static final RegistryObject<IRecipeSerializer<?>> ROLLING =
    RECIPE_SERIALIZERS.register("rolling", RollingRecipe.RollingRecipeSerializer::new);

  // i hate it but you have to do it.
  public static final IRecipeType<RollingRecipe> ROLLING_RECIPIE = IRecipeType.register("rolling");
}

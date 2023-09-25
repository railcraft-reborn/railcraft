package mods.railcraft.world.item.crafting;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftRecipeSerializers {

  private static final DeferredRegister<RecipeSerializer<?>> deferredRegister =
      DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, RailcraftConstants.ID);

  public static final RegistryObject<RecipeSerializer<?>> ROLLING =
      deferredRegister.register("rolling", RollingRecipe.Serializer::new);

  public static final RegistryObject<RecipeSerializer<?>> COKING =
      deferredRegister.register("coking", CokeOvenRecipe.Serializer::new);

  public static final RegistryObject<RecipeSerializer<?>> BLASTING =
      deferredRegister.register("blasting", BlastFurnaceRecipe.Serializer::new);

  public static final RegistryObject<RecipeSerializer<?>> CRUSHER =
      deferredRegister.register("crusher", CrusherRecipe.Serializer::new);

  public static final RegistryObject<RecipeSerializer<TicketDuplicateRecipe>> TICKET_DUPLICATE =
      deferredRegister.register("ticket_duplicate",
          () -> new SimpleCraftingRecipeSerializer<>(TicketDuplicateRecipe::new));

  public static final RegistryObject<RecipeSerializer<LocomotivePaintingRecipe>> LOCOMOTIVE_PAINTING =
      deferredRegister.register("locomotive_painting",
          () -> new SimpleCraftingRecipeSerializer<>(LocomotivePaintingRecipe::new));

  public static final RegistryObject<RecipeSerializer<RotorRepairRecipe>> ROTOR_REPAIR =
      deferredRegister.register("rotor_repair",
          () -> new SimpleCraftingRecipeSerializer<>(RotorRepairRecipe::new));

  public static final RegistryObject<RecipeSerializer<CartDisassemblyRecipe>> CART_DISASSEMBLY =
      deferredRegister.register("cart_disassembly",
          () -> new SimpleCraftingRecipeSerializer<>(CartDisassemblyRecipe::new));

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }
}

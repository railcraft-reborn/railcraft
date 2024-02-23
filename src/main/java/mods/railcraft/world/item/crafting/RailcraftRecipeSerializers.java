package mods.railcraft.world.item.crafting;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.world.item.Items;
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

  public static final RegistryObject<RecipeSerializer<CartDisassemblyRecipe>> CHEST_MINECART_DISASSEMBLY =
      deferredRegister.register("chest_minecart_disassembly",
          () -> new SimpleCraftingRecipeSerializer<>((resourceLocation, craftingBookCategory) ->
              new CartDisassemblyRecipe(resourceLocation, Items.CHEST_MINECART, Items.CHEST,
                  craftingBookCategory) {
                @Override
                public RecipeSerializer<?> getSerializer() {
                  return CHEST_MINECART_DISASSEMBLY.get();
                }
              }));

  public static final RegistryObject<RecipeSerializer<CartDisassemblyRecipe>> WORLDSPIKE_MINECART_DISASSEMBLY =
      deferredRegister.register("worldspike_minecart_disassembly",
          () -> new SimpleCraftingRecipeSerializer<>((resourceLocation, craftingBookCategory) ->
              new CartDisassemblyRecipe(resourceLocation, RailcraftItems.WORLD_SPIKE_MINECART.get(),
                  RailcraftItems.WORLD_SPIKE.get(), craftingBookCategory) {
                @Override
                public RecipeSerializer<?> getSerializer() {
                  return WORLDSPIKE_MINECART_DISASSEMBLY.get();
                }
              }));

  public static final RegistryObject<SimpleCraftingRecipeSerializer<PatchouliBookCrafting>> PATCHOULI_BOOK_CRAFTING =
      deferredRegister.register("patchouli_book_crafting",
          () -> new SimpleCraftingRecipeSerializer<>(PatchouliBookCrafting::new));

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }
}

package mods.railcraft.world.item.crafting;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RailcraftRecipeSerializers {

  private static final DeferredRegister<RecipeSerializer<?>> deferredRegister =
      DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, RailcraftConstants.ID);

  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RollingRecipe>> ROLLING =
      deferredRegister.register("rolling", RollingRecipe.Serializer::new);

  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CokeOvenRecipe>> COKING =
      deferredRegister.register("coking", CokeOvenRecipe.Serializer::new);

  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BlastFurnaceRecipe>> BLASTING =
      deferredRegister.register("blasting", BlastFurnaceRecipe.Serializer::new);

  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CrusherRecipe>> CRUSHER =
      deferredRegister.register("crusher", CrusherRecipe.Serializer::new);

  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TicketDuplicateRecipe>> TICKET_DUPLICATE =
      deferredRegister.register("ticket_duplicate",
          () -> new CustomRecipe.Serializer<>(TicketDuplicateRecipe::new));

  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<LocomotivePaintingRecipe>> LOCOMOTIVE_PAINTING =
      deferredRegister.register("locomotive_painting",
          () -> new CustomRecipe.Serializer<>(LocomotivePaintingRecipe::new));

  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RotorRepairRecipe>> ROTOR_REPAIR =
      deferredRegister.register("rotor_repair",
          () -> new CustomRecipe.Serializer<>(RotorRepairRecipe::new));

  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ChestMinecartDisassemblyRecipe>> CHEST_MINECART_DISASSEMBLY =
      deferredRegister.register("chest_minecart_disassembly",
          () -> new CustomRecipe.Serializer<>(ChestMinecartDisassemblyRecipe::new));

  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<WorldSpikeMinecartDisassemblyRecipe>> WORLDSPIKE_MINECART_DISASSEMBLY =
      deferredRegister.register("worldspike_minecart_disassembly",
          () -> new CustomRecipe.Serializer<>(WorldSpikeMinecartDisassemblyRecipe::new));

  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ShapelessRecipe>> PATCHOULI_BOOK_CRAFTING =
      deferredRegister.register("patchouli_book_crafting",
          () -> new CustomRecipe.Serializer<>(PatchouliBookCrafting::new));

  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<WoodenTieRecipe>> WOODEN_TIE =
      deferredRegister.register("wooden_tie",
          () -> new CustomRecipe.Serializer<>(WoodenTieRecipe::new));

  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<StoneTieRecipe>> STONE_TIE =
      deferredRegister.register("stone_tie",
          () -> new CustomRecipe.Serializer<>(StoneTieRecipe::new));

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }
}

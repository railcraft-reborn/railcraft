package mods.railcraft.world.item.crafting;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RailcraftRecipeSerializers {

  private static final DeferredRegister<RecipeSerializer<?>> deferredRegister =
      DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, RailcraftConstants.ID);

//  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ShapelessRecipe>> PATCHOULI_BOOK_CRAFTING =
//      deferredRegister.register("patchouli_book_crafting",
//          () -> new CustomRecipe.Serializer<>(PatchouliBookCrafting::new));

  public static void register(IEventBus modEventBus) {
    deferredRegister.register("rolling", () -> RollingRecipe.SERIALIZER);
    deferredRegister.register("coking", () -> CokeOvenRecipe.SERIALIZER);
    deferredRegister.register("blasting", () -> BlastFurnaceRecipe.SERIALIZER);
    deferredRegister.register("crusher", () -> CrusherRecipe.SERIALIZER);
    deferredRegister.register("ticket_duplicate", () -> TicketDuplicateRecipe.SERIALIZER);
    deferredRegister.register("locomotive_painting", () -> LocomotivePaintingRecipe.SERIALIZER);
    deferredRegister.register("rotor_repair", () -> RotorRepairRecipe.SERIALIZER);
    deferredRegister.register("chest_minecart_disassembly", () -> ChestMinecartDisassemblyRecipe.SERIALIZER);
    deferredRegister.register("worldspike_minecart_disassembly", () -> WorldSpikeMinecartDisassemblyRecipe.SERIALIZER);
    deferredRegister.register("void_chest_minecart_disassembly", () -> VoidChestMinecartDisassemblyRecipe.SERIALIZER);
    deferredRegister.register("wooden_tie", () -> WoodenTieRecipe.SERIALIZER);
    deferredRegister.register("stone_tie", () -> StoneTieRecipe.SERIALIZER);
    deferredRegister.register(modEventBus);
  }
}

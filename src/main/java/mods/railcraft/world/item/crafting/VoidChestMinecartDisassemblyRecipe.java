package mods.railcraft.world.item.crafting;

import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class VoidChestMinecartDisassemblyRecipe extends CartDisassemblyRecipe {

  public VoidChestMinecartDisassemblyRecipe(CraftingBookCategory category) {
    super(RailcraftItems.VOID_CHEST_MINECART.get(), RailcraftItems.VOID_CHEST.get(), category);
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return RailcraftRecipeSerializers.VOID_CHEST_MINECART_DISASSEMBLY.get();
  }
}

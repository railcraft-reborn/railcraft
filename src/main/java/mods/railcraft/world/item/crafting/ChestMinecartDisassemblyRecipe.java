package mods.railcraft.world.item.crafting;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ChestMinecartDisassemblyRecipe extends CartDisassemblyRecipe {

  public ChestMinecartDisassemblyRecipe(CraftingBookCategory category) {
    super(Items.CHEST_MINECART, Items.CHEST, category);
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return RailcraftRecipeSerializers.CHEST_MINECART_DISASSEMBLY.get();
  }
}

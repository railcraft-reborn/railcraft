package mods.railcraft.world.item.crafting;

import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class BedMinecartDisassemblyRecipe extends CartDisassemblyRecipe {

  public BedMinecartDisassemblyRecipe(CraftingBookCategory category) {
    super(RailcraftItems.BED_MINECART.get(), Items.WHITE_BED, category);
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return RailcraftRecipeSerializers.BED_MINECART_DISASSEMBLY.get();
  }
}

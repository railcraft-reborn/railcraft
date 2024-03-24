package mods.railcraft.world.item.crafting;

import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class WorldSpikeMinecartDisassemblyRecipe extends CartDisassemblyRecipe {

  public WorldSpikeMinecartDisassemblyRecipe(CraftingBookCategory category) {
    super(RailcraftItems.WORLD_SPIKE_MINECART.get(), RailcraftItems.WORLD_SPIKE.get(), category);
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return RailcraftRecipeSerializers.WORLDSPIKE_MINECART_DISASSEMBLY.get();
  }
}

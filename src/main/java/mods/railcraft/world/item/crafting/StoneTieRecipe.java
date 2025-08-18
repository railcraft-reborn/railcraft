package mods.railcraft.world.item.crafting;

import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.common.Tags;

public class StoneTieRecipe extends TieRecipe {

  public StoneTieRecipe(CraftingBookCategory category) {
    super(category, Tags.Fluids.WATER,
        RailcraftItems.STONE_TIE.toStack());
  }

  @Override
  protected boolean testIngredient(ItemStack itemPresent, int index) {
    if (index == 0 || index == 2) {
      return itemPresent.is(RailcraftItems.BAG_OF_CEMENT.get());
    } else if (index == 1) {
      return itemPresent.is(RailcraftItems.REBAR.get());
    }
    return false;
  }

  @Override
  public RecipeSerializer<StoneTieRecipe> getSerializer() {
    return RailcraftRecipeSerializers.STONE_TIE.get();
  }
}

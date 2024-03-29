package mods.railcraft.world.item;

import org.jetbrains.annotations.Nullable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

public class CoalCokeItem extends Item {

  public static final int BURN_TIME = 3200;

  public CoalCokeItem(Properties properties) {
    super(properties);
  }

  @Override
  public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
    return BURN_TIME;
  }
}

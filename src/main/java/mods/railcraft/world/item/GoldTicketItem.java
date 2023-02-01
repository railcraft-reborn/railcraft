package mods.railcraft.world.item;

import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;

public class GoldTicketItem extends TicketItem {

  public static final Predicate<ItemStack> FILTER =
      stack -> stack != null && stack.getItem() instanceof GoldTicketItem;

  public GoldTicketItem(Properties properties) {
    super(properties);
  }

  @Override
  public boolean hasCraftingRemainingItem(ItemStack stack) {
    return true;
  }

  @Override
  public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
    return new ItemStack(this);
  }
}

package mods.railcraft.world.item;

import java.util.function.Predicate;
import mods.railcraft.client.ScreenFactories;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GoldenTicketItem extends TicketItem {

  public static final Predicate<ItemStack> FILTER =
      stack -> stack != null && stack.getItem() instanceof GoldenTicketItem;

  public GoldenTicketItem(Properties properties) {
    super(properties);
  }

  @Override
  public boolean hasCraftingRemainingItem(ItemStack stack) {
    return true;
  }

  @Override
  public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
    var newItemStack = new ItemStack(this);
    var tag = itemStack.getTag();
    if (tag != null) {
      newItemStack.setTag(tag);
    }
    return newItemStack;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player,
      InteractionHand usedHand) {
    var itemStack = player.getItemInHand(usedHand);
    if (level.isClientSide()) {
      ScreenFactories.openGoldenTicketScreen(itemStack, usedHand);
    }
    return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
  }
}

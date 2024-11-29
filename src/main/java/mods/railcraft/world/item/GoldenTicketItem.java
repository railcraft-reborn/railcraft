package mods.railcraft.world.item;

import java.util.function.Predicate;
import mods.railcraft.client.ScreenFactories;
import mods.railcraft.world.item.component.RailcraftDataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
  public ItemStack getCraftingRemainder(ItemStack itemStack) {
    var newItemStack = new ItemStack(this);
    if (itemStack.has(RailcraftDataComponents.TICKET)) {
      newItemStack.set(RailcraftDataComponents.TICKET,
          itemStack.get(RailcraftDataComponents.TICKET));
    }
    return newItemStack;
  }

  @Override
  public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
    var itemStack = player.getItemInHand(usedHand);
    if (level.isClientSide()) {
      ScreenFactories.openGoldenTicketScreen(itemStack, usedHand);
    }
    return InteractionResult.SUCCESS;
  }
}

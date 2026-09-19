package mods.railcraft.world.item;

import java.util.function.Consumer;
import java.util.function.Predicate;
import mods.railcraft.client.ScreenFactories;
import mods.railcraft.world.item.component.RailcraftDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

public class RoutingTableBookItem extends Item {

  public static final Predicate<ItemStack> FILTER =
      stack -> stack != null && stack.getItem() instanceof RoutingTableBookItem;

  public RoutingTableBookItem(Properties properties) {
    super(properties);
  }

  @Override
  public Component getName(ItemStack stack) {
    var name = super.getName(stack);
    var content = stack.get(RailcraftDataComponents.ROUTING_TABLE_BOOK);
    if (content != null) {
      var title = content.title();
      if (title.isPresent() && !title.get().isEmpty()) {
        name = name.copy().append(" - " + title.get());
      }
    }
    return name;
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context,
      TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
    var content = stack.get(RailcraftDataComponents.ROUTING_TABLE_BOOK);
    if (content != null) {
      content.addToTooltip(context, tooltipAdder, flag, this.components());
    }
  }

  @Override
  public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
    var itemStack = player.getItemInHand(usedHand);
    if (level.isClientSide()) {
      ScreenFactories.openRoutingTableBookScreen(player, itemStack, usedHand);
    }
    return InteractionResult.SUCCESS;
  }
}

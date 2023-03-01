package mods.railcraft.world.item;

import java.util.List;
import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.client.ScreenFactories;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
    var tag = stack.getTag();
    if (tag != null && tag.contains("title")) {
      var title = tag.getString("title");
      if (!title.isEmpty()) {
        name = name.copy().append(" - " + title);
      }
    }
    return name;
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list,
      TooltipFlag flag) {
    var tag = stack.getTag();
    if (tag != null && tag.contains("author")) {
      var author = tag.getString("author");
      if (!author.isEmpty()) {
        list.add(Component.translatable(Translations.Tips.ROUTING_TABLE_BOOK_LAST_EDIT, author)
            .withStyle(ChatFormatting.GRAY));
      }
    }
  }

  @Override
  public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
    return false;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player,
      InteractionHand usedHand) {
    var itemStack = player.getItemInHand(usedHand);
    if (level.isClientSide()) {
      ScreenFactories.openRoutingTableBookScreen(player, itemStack, usedHand);
    }
    return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
  }
}

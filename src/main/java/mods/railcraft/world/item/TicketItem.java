package mods.railcraft.world.item;

import java.util.List;
import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;
import com.mojang.authlib.GameProfile;
import mods.railcraft.Translations;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.PlayerUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class TicketItem extends Item {

  public static final Predicate<ItemStack> FILTER =
      stack -> stack != null && stack.getItem() instanceof TicketItem;
  public static final int LINE_LENGTH = 32;

  public TicketItem(Properties properties) {
    super(properties);
  }

  @Override
  public Component getName(ItemStack stack) {
    var name = super.getName(stack);
    var dest = getDestination(stack);
    if (!dest.isEmpty()) {
      var pretty_dest = dest.substring(dest.lastIndexOf("/") + 1);
      name = name.copy().append(" - " + pretty_dest);
    }
    return name;
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list,
      TooltipFlag flag) {
    if (!stack.hasTag()) {
      list.add(Component.translatable(Translations.Tips.ROUTING_TICKET_BLANK)
          .withStyle(ChatFormatting.GRAY));
      return;
    }
    var owner = getOwner(stack);
    if (owner != null) {
      list.add(Component.translatable(Translations.Tips.ROUTING_TICKET_ISSUER)
          .withStyle(ChatFormatting.AQUA)
          .append(" ")
          .append(PlayerUtil.getUsername(level, owner).copy().withStyle(ChatFormatting.GRAY)));
    }

    String dest = getDestination(stack);
    list.add(Component.translatable(Translations.Tips.ROUTING_TICKET_DEST)
        .withStyle(ChatFormatting.AQUA)
        .append(" ")
        .append((dest.isEmpty()
            ? Component.translatable(Translations.Tips.NONE)
            : Component.literal(dest)).withStyle(ChatFormatting.GRAY)));
  }

  @Override
  public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
    return false;
  }

  public static ItemStack copyTicket(ItemStack source) {
    if (source.isEmpty())
      return ItemStack.EMPTY;
    if (source.getItem() instanceof TicketItem) {
      ItemStack ticket = RailcraftItems.TICKET.get().getDefaultInstance();
      if (ticket.isEmpty())
        return ItemStack.EMPTY;
      var tag = source.getTag();
      if (tag != null)
        ticket.setTag(tag.copy());
      return ticket;
    }
    return ItemStack.EMPTY;
  }

  public static boolean setTicketData(ItemStack ticket, String dest, @Nullable GameProfile owner) {
    if (ticket.isEmpty() || !(ticket.getItem() instanceof TicketItem))
      return false;
    if (dest.length() > LINE_LENGTH)
      return false;
    if (owner == null)
      return false;
    var tag = ticket.getOrCreateTag();
    tag.putString(CompoundTagKeys.DEST, dest);
    NbtUtils.writeGameProfile(tag, owner);
    return true;
  }

  public static String getDestination(ItemStack ticket) {
    if (ticket.isEmpty() || !(ticket.getItem() instanceof TicketItem))
      return "";
    var tag = ticket.getTag();
    if (tag == null)
      return "";
    return tag.getString(CompoundTagKeys.DEST);
  }

  @Nullable
  public static GameProfile getOwner(ItemStack ticket) {
    if (ticket.isEmpty() || !(ticket.getItem() instanceof TicketItem))
      return null;
    var tag = ticket.getTag();
    if (tag == null)
      return null;
    return NbtUtils.readGameProfile(tag);
  }
}

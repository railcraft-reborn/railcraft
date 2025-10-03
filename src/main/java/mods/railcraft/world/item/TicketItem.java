package mods.railcraft.world.item;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.world.item.component.RailcraftDataComponents;
import mods.railcraft.world.item.component.TicketComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.players.NameAndId;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

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
  public void appendHoverText(ItemStack stack, TooltipContext context,
      TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
    if (!stack.has(RailcraftDataComponents.TICKET)) {
      tooltipAdder.accept(Component.translatable(Translations.Tips.ROUTING_TICKET_BLANK)
          .withStyle(ChatFormatting.GRAY));
      return;
    }
    var owner = getOwner(stack);
    if (owner != null) {
      tooltipAdder.accept(Component.translatable(Translations.Tips.ROUTING_TICKET_ISSUER)
          .withStyle(ChatFormatting.AQUA)
          .append(CommonComponents.SPACE)
          .append(Component.literal(owner.name()).withStyle(ChatFormatting.GRAY)));
    }

    String dest = getDestination(stack);
    tooltipAdder.accept(Component.translatable(Translations.Tips.ROUTING_TICKET_DEST)
        .withStyle(ChatFormatting.AQUA)
        .append(CommonComponents.SPACE)
        .append((dest.isEmpty()
            ? Component.translatable(Translations.Tips.NONE)
            : Component.literal(dest)).withStyle(ChatFormatting.GRAY)));
  }

  public static ItemStack copyTicket(ItemStack source) {
    if (source.isEmpty())
      return ItemStack.EMPTY;
    if (source.getItem() instanceof TicketItem) {
      ItemStack ticket = RailcraftItems.TICKET.get().getDefaultInstance();
      if (ticket.isEmpty())
        return ItemStack.EMPTY;
      var ticketData = source.get(RailcraftDataComponents.TICKET);
      if (ticketData != null)
        ticket.set(RailcraftDataComponents.TICKET, ticketData);
      return ticket;
    }
    return ItemStack.EMPTY;
  }

  public static boolean setTicketData(ItemStack ticket, String dest, @Nullable NameAndId owner) {
    if (ticket.isEmpty() || !(ticket.getItem() instanceof TicketItem))
      return false;
    if (dest.length() > LINE_LENGTH)
      return false;
    if (owner == null)
      return false;
    ticket.set(RailcraftDataComponents.TICKET, new TicketComponent(dest, owner));
    return true;
  }

  public static String getDestination(ItemStack ticket) {
    if (ticket.isEmpty() || !(ticket.getItem() instanceof TicketItem))
      return "";
    if (!ticket.has(RailcraftDataComponents.TICKET))
      return "";
    var ticketData = ticket.get(RailcraftDataComponents.TICKET);
    return Objects.requireNonNull(ticketData).destination();
  }

  @Nullable
  public static NameAndId getOwner(ItemStack ticket) {
    if (ticket.isEmpty() || !(ticket.getItem() instanceof TicketItem))
      return null;
    if (!ticket.has(RailcraftDataComponents.TICKET))
      return null;
    var ticketData = ticket.get(RailcraftDataComponents.TICKET);
    return Objects.requireNonNull(ticketData).owner().orElse(null);
  }
}

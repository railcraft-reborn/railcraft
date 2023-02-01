package mods.railcraft.world.item;

import java.util.List;
import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;
import com.mojang.authlib.GameProfile;
import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstantsAPI;
import mods.railcraft.util.PlayerUtil;
import mods.railcraft.util.container.ContainerTools;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
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

  public boolean validateNBT(CompoundTag nbt) {
    String dest = nbt.getString("dest");
    return dest.length() < LINE_LENGTH;
  }
  @Override
  public Component getName(ItemStack stack) {
    var name = super.getName(stack);
    var dest = getDestination(stack);
    if (!dest.isEmpty()) {
      var pretty_dest = dest.substring(dest.lastIndexOf("/") + 1);
      name = name.copy().append(" - " + pretty_dest).withStyle(ChatFormatting.YELLOW);
    }
    return name;
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list,
      TooltipFlag flag) {
    if (stack.hasTag()) {
      GameProfile owner = getOwner(stack);
      list.add(Component.translatable(Translations.Tips.ROUTING_TICKET_ISSUER));
      list.add(PlayerUtil.getUsername(level, owner).copy().withStyle(ChatFormatting.GRAY));

      String dest = getDestination(stack);
      if (!dest.isEmpty()) {
        list.add(Component.translatable(Translations.Tips.ROUTING_TICKET_DEST));
        list.add(Component.literal(dest).withStyle(ChatFormatting.GRAY));
      }
    } else
      list.add(Component.translatable(Translations.Tips.ROUTING_TICKET_BLANK)
          .withStyle(ChatFormatting.GRAY));
  }

  public static boolean isNBTValid(@Nullable CompoundTag nbt) {
    if (nbt == null)
      return false;
    else if (!nbt.contains("dest", Tag.TAG_STRING))
      return false;

    String dest = nbt.getString("dest");
    return !dest.isEmpty() && dest.length() <= LINE_LENGTH;
  }

  public static ItemStack copyTicket(ItemStack source) {
    if (source.isEmpty())
      return ItemStack.EMPTY;
    if (source.getItem() instanceof TicketItem) {
      ItemStack ticket = RailcraftItems.TICKET.get().getDefaultInstance();
      if (ticket.isEmpty())
        return ItemStack.EMPTY;
      CompoundTag nbt = source.getTag();
      if (nbt != null)
        ticket.setTag(nbt.copy());
      return ticket;
    }
    return ItemStack.EMPTY;
  }

  public static boolean setTicketData(ItemStack ticket, String dest, String title,
      @Nullable GameProfile owner) {
    if (ticket.isEmpty() || !(ticket.getItem() instanceof TicketItem))
      return false;
    if (dest.length() > LINE_LENGTH)
      return false;
    if (owner == null)
      return false;
    CompoundTag data = ContainerTools.getItemData(ticket);
    data.putString("dest", dest);
    data.putString("title", title);
    PlayerUtil.writeOwnerToNBT(data, owner);
    return true;
  }

  public static String getDestination(ItemStack ticket) {
    if (ticket.isEmpty() || !(ticket.getItem() instanceof TicketItem))
      return "";
    CompoundTag nbt = ticket.getTag();
    if (nbt == null)
      return "";
    return nbt.getString("dest");
  }

  public static GameProfile getOwner(ItemStack ticket) {
    if (ticket.isEmpty() || !(ticket.getItem() instanceof TicketItem))
      return new GameProfile(null, RailcraftConstantsAPI.UNKNOWN_PLAYER);
    CompoundTag nbt = ticket.getTag();
    if (nbt == null)
      return new GameProfile(null, RailcraftConstantsAPI.UNKNOWN_PLAYER);
    return PlayerUtil.readOwnerFromNBT(nbt);
  }
}

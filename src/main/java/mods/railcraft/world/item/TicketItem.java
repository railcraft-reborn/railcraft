package mods.railcraft.world.item;

import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import com.mojang.authlib.GameProfile;
import mods.railcraft.api.core.RailcraftConstantsAPI;
import mods.railcraft.util.PlayerUtil;
import mods.railcraft.util.container.ContainerTools;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
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

  // @Override
  // public String getItemDisplayName(ItemStack stack) {
  // String dest = getDestination(stack);
  //
  // if (!dest.equals("")) {
  // return super.getItemDisplayName(stack) + " - " + dest.substring(dest.lastIndexOf("/") + 1);
  // }
  //
  // return super.getItemDisplayName(stack);
  // }

  /**
   * allows items to add custom lines of information to the mouse over description
   */
  @Override
  public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list,
      TooltipFlag par4) {
    if (stack.hasTag()) {
      GameProfile owner = getOwner(stack);
      if (owner.getId() != null) {
        list.add(new TranslatableComponent("gui.railcraft.routing.ticket.tips.issuer"));
        list.add(PlayerUtil.getUsername(world, owner).copy().withStyle(ChatFormatting.GRAY));
      }

      String dest = getDestination(stack);
      if (!"".equals(dest)) {
        list.add(new TranslatableComponent("gui.railcraft.routing.ticket.tips.dest"));
        list.add(new TextComponent(dest).withStyle(ChatFormatting.GRAY));
      }
    } else
      list.add(new TranslatableComponent("gui.railcraft.routing.ticket.tips.blank"));
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

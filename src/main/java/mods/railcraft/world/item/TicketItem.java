package mods.railcraft.world.item;

import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import com.mojang.authlib.GameProfile;
import mods.railcraft.api.core.RailcraftConstantsAPI;
import mods.railcraft.util.PlayerUtil;
import mods.railcraft.util.inventory.InvTools;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

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

  public boolean validateNBT(CompoundNBT nbt) {
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
  public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> list,
      ITooltipFlag par4) {
    if (stack.hasTag()) {
      GameProfile owner = getOwner(stack);
      if (owner.getId() != null) {
        list.add(new TranslationTextComponent("gui.railcraft.routing.ticket.tips.issuer"));
        list.add(PlayerUtil.getUsername(world, owner).copy().withStyle(TextFormatting.GRAY));
      }

      String dest = getDestination(stack);
      if (!"".equals(dest)) {
        list.add(new TranslationTextComponent("gui.railcraft.routing.ticket.tips.dest"));
        list.add(new StringTextComponent(dest).withStyle(TextFormatting.GRAY));
      }
    } else
      list.add(new TranslationTextComponent("gui.railcraft.routing.ticket.tips.blank"));
  }

  public static boolean isNBTValid(@Nullable CompoundNBT nbt) {
    if (nbt == null)
      return false;
    else if (!nbt.contains("dest", Constants.NBT.TAG_STRING))
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
      CompoundNBT nbt = source.getTag();
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
    CompoundNBT data = InvTools.getItemData(ticket);
    data.putString("dest", dest);
    data.putString("title", title);
    PlayerUtil.writeOwnerToNBT(data, owner);
    return true;
  }

  public static String getDestination(ItemStack ticket) {
    if (ticket.isEmpty() || !(ticket.getItem() instanceof TicketItem))
      return "";
    CompoundNBT nbt = ticket.getTag();
    if (nbt == null)
      return "";
    return nbt.getString("dest");
  }

  public static boolean matchesOwnerOrOp(ItemStack ticket, GameProfile player) {
    return ticket.getItem() instanceof TicketItem
        && PlayerUtil.isOwnerOrOp(getOwner(ticket), player);
  }

  public static GameProfile getOwner(ItemStack ticket) {
    if (ticket.isEmpty() || !(ticket.getItem() instanceof TicketItem))
      return new GameProfile(null, RailcraftConstantsAPI.UNKNOWN_PLAYER);
    CompoundNBT nbt = ticket.getTag();
    if (nbt == null)
      return new GameProfile(null, RailcraftConstantsAPI.UNKNOWN_PLAYER);
    return PlayerUtil.readOwnerFromNBT(nbt);
  }
}

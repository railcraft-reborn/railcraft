package mods.railcraft.world.item;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations.Tips;
import mods.railcraft.season.Season;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class SeasonsCrowbarItem extends CrowbarItem {

  public SeasonsCrowbarItem(Properties properties) {
    super(2.5F, -2.4F, Tiers.DIAMOND, properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack itemStack = player.getItemInHand(hand);
    if (!level.isClientSide()) {
      incrementSeason(itemStack);
      var season = getSeason(itemStack);
      player.displayClientMessage(getDescriptionText(season, false), true);
    }
    return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
  }

  @Override
  public boolean isValidRepairItem(ItemStack itemToRepair, ItemStack stack) {
    return false;
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list,
      TooltipFlag adv) {
    var season = getSeason(stack);
    list.add(getDescriptionText(season, true));
  }

  public static Season getSeason(ItemStack itemStack) {
    var tag = itemStack.getOrCreateTag();
    return Season.fromName(tag.getString("season"));
  }

  private static void incrementSeason(ItemStack itemStack) {
    var season = getSeason(itemStack).getNext();
    itemStack.getOrCreateTag().putString("season", season.getSerializedName());
  }

  private static Component getDescriptionText(Season value, boolean tooltip) {
    var title = Component.translatable(Tips.CROWBAR_SEASON_DESC);
    if (tooltip) {
      title.withStyle(ChatFormatting.GRAY);
    }
    return title.append(" ")
        .append(value.getDisplayName().copy().withStyle(ChatFormatting.DARK_PURPLE));
  }
}

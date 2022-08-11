package mods.railcraft.world.item;

import java.util.List;
import javax.annotation.Nullable;
import mods.railcraft.Translations.Tips;
import mods.railcraft.season.Season;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class SeasonsCrowbarItem extends CrowbarItem {

  public SeasonsCrowbarItem(Properties properties) {
    super(2.5F, -2.4F, Tiers.DIAMOND, properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack itemStack = player.getItemInHand(hand);
    if (!level.isClientSide()) {
      incrementSeason(itemStack);
      Season season = getSeason(itemStack);
      player.displayClientMessage(getDescriptionText(season.getTranslation(), false), true);
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
    Season season = getSeason(stack);
    list.add(getDescriptionText(season.getTranslation(), true));
  }

  public static Season getSeason(ItemStack itemStack) {
    if (itemStack.hasTag()) {
      var tag = itemStack.getTag();
      if (tag.contains("season")) {
        return Season.values()[tag.getInt("season")];
      }
    }
    return Season.DEFAULT;
  }

  private static void incrementSeason(ItemStack itemStack) {
    Season season = getSeason(itemStack).getNext();
    itemStack.getOrCreateTag().putInt("season", season.ordinal());
  }

  private static Component getDescriptionText(MutableComponent value, boolean tooltip) {
    var title = Component.translatable(Tips.CRAWBAR_SEASON_DESC);
    if(tooltip)
      title.withStyle(ChatFormatting.GRAY);
    return title.append(value.withStyle(ChatFormatting.DARK_PURPLE));
  }
}

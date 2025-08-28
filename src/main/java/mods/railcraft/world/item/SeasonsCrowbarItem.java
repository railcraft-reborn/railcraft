package mods.railcraft.world.item;

import java.util.function.Consumer;
import mods.railcraft.Translations.Tips;
import mods.railcraft.season.Season;
import mods.railcraft.world.item.component.RailcraftDataComponents;
import mods.railcraft.world.item.component.SeasonComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

public class SeasonsCrowbarItem extends CrowbarItem {

  public SeasonsCrowbarItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult use(Level level, Player player, InteractionHand hand) {
    ItemStack itemStack = player.getItemInHand(hand);
    if (!level.isClientSide()) {
      incrementSeason(itemStack);
      var season = getSeason(itemStack);
      player.displayClientMessage(getDescriptionText(season, false), true);
    }
    return InteractionResult.SUCCESS;
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context,
      TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
    var season = getSeason(stack);
    tooltipAdder.accept(getDescriptionText(season, true));
  }

  public static Season getSeason(ItemStack itemStack) {
    if (itemStack.has(RailcraftDataComponents.SEASON)) {
      return itemStack.get(RailcraftDataComponents.SEASON).season();
    }
    return Season.DEFAULT;
  }

  private static void incrementSeason(ItemStack itemStack) {
    var season = getSeason(itemStack).getNext();
    itemStack.set(RailcraftDataComponents.SEASON, new SeasonComponent(season));
  }

  private static Component getDescriptionText(Season value, boolean tooltip) {
    var title = Component.translatable(Tips.CROWBAR_SEASON_DESC);
    if (tooltip) {
      title.withStyle(ChatFormatting.GRAY);
    }
    return title.append(CommonComponents.SPACE)
        .append(value.getDisplayName().copy().withStyle(ChatFormatting.DARK_PURPLE));
  }
}

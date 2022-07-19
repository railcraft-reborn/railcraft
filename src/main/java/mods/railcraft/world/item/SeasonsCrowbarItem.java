package mods.railcraft.world.item;

import mods.railcraft.season.Season;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

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
      player.displayClientMessage(getDescriptionText(season.getDisplayName()), true);
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
    list.add(getDescriptionText(season.getDisplayName()));
  }

  public static Component getDescriptionText(Component displayName) {
    return Component.translatable("seasons_crowbar.season",
        displayName.copy().withStyle(ChatFormatting.DARK_PURPLE));
  }

  public static Season getSeason(ItemStack itemStack) {
    return Optional.ofNullable(itemStack.getTag())
        .filter(tag -> tag.contains("season", Tag.TAG_STRING))
        .map(tag -> tag.getString("season"))
        .flatMap(Season::getByName)
        .orElse(Season.DEFAULT);
  }

  public static void incrementSeason(ItemStack itemStack) {
    Season season = getSeason(itemStack).getNext();
    itemStack.getOrCreateTag().putString("season", season.getSerializedName());
  }
}

package mods.railcraft.world.item;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import mods.railcraft.season.Season;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class SeasonsCrowbarItem extends CrowbarItem {

  public SeasonsCrowbarItem(Properties properties) {
    super(2.5F, -2.4F, ItemTier.DIAMOND, properties);
  }

  @Override
  public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
    ItemStack itemStack = player.getItemInHand(hand);
    if (!level.isClientSide()) {
      incrementSeason(itemStack);
      Season season = getSeason(itemStack);
      player.displayClientMessage(getDescriptionText(season.getDisplayName()), true);
    }
    return ActionResult.sidedSuccess(itemStack, level.isClientSide());
  }

  @Override
  public boolean isValidRepairItem(ItemStack itemToRepair, ItemStack stack) {
    return false;
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable World level, List<ITextComponent> list,
      ITooltipFlag adv) {
    Season season = getSeason(stack);
    list.add(getDescriptionText(season.getDisplayName()));
  }

  public static ITextComponent getDescriptionText(ITextComponent displayName) {
    return new TranslationTextComponent("seasons_crowbar.season",
        displayName.copy().withStyle(TextFormatting.DARK_PURPLE));
  }

  public static Season getSeason(ItemStack itemStack) {
    return Optional.ofNullable(itemStack.getTag())
        .filter(tag -> tag.contains("season", Constants.NBT.TAG_STRING))
        .map(tag -> tag.getString("season"))
        .flatMap(Season::getByName)
        .orElse(Season.DEFAULT);
  }

  public static void incrementSeason(ItemStack itemStack) {
    Season season = getSeason(itemStack).getNext();
    itemStack.getOrCreateTag().putString("season", season.getSerializedName());
  }
}

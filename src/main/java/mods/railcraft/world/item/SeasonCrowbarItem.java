package mods.railcraft.world.item;

import java.util.List;
import javax.annotation.Nullable;
import mods.railcraft.season.Season;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class SeasonCrowbarItem extends CrowbarItem {

  public SeasonCrowbarItem(Properties properties) {
    super(2.5F, -2.4F, ItemTier.DIAMOND, properties);
  }

  public static Season getCurrentSeason(ItemStack crowbar) {
    Season season = Season.DEFAULT;
    if (crowbar.getItem() instanceof SeasonCrowbarItem) {
      CompoundNBT tag = crowbar.getTag();
      if (tag != null)
        season = Season.getByName(tag.getString("season")).orElse(Season.DEFAULT);
    }
    return season;
  }

  public static void incrementSeason(ItemStack crowbar) {
    crowbar.getOrCreateTag().putString("season",
        getCurrentSeason(crowbar).getNext().getSerializedName());
  }

  @Override
  public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
    ItemStack stack = player.getItemInHand(hand);
    if (!world.isClientSide()) {
      incrementSeason(stack);
      Season aura = getCurrentSeason(stack);
      player.displayClientMessage(new TranslationTextComponent(
          "item.railcraft.tool.crowbar.seasons.tips.mode", "\u00A75" + aura), false);
    }
    return ActionResult.sidedSuccess(stack.copy(), world.isClientSide());
  }

  @Override
  public boolean isValidRepairItem(ItemStack itemToRepair, ItemStack stack) {
    return false;
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> list,
      ITooltipFlag adv) {
    Season aura = getCurrentSeason(stack);
    list.add(new TranslationTextComponent("item.railcraft.tool.crowbar.seasons.tips.mode",
        "\u00A75" + aura));
  }
}

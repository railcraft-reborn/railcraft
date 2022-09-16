package mods.railcraft.world.item.track;

import java.util.List;
import mods.railcraft.Translations;
import mods.railcraft.world.level.block.track.outfitted.LockingTrackBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class LockingTrackBlockItem extends BlockItem {

  public LockingTrackBlockItem(LockingTrackBlock block, Properties properties) {
    super(block, properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, Level level, List<Component> lines,
      TooltipFlag flag) {
    lines.add(Component.translatable(Translations.Tips.LOCKING_TRACK)
        .withStyle(ChatFormatting.GRAY));
    lines.add(Component.translatable(Translations.Tips.HIT_CROWBAR_TO_CHANGE_MODE)
        .withStyle(ChatFormatting.BLUE));
    lines.add(Component.translatable(Translations.Tips.APPLY_REDSTONE_TO_RELEASE_CARTS)
        .withStyle(ChatFormatting.RED));
  }
}

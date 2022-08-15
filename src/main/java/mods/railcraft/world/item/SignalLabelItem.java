package mods.railcraft.world.item;

import mods.railcraft.Translations.Tips;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import mods.railcraft.api.signal.entity.SignalEntity;
import java.util.List;

public class SignalLabelItem extends Item {

  public SignalLabelItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult onItemUseFirst(ItemStack itemStack, UseOnContext context) {
    var level = context.getLevel();
    var player = context.getPlayer();
    if (player.isShiftKeyDown() && itemStack.hasCustomHoverName()
        && level.getBlockEntity(context.getClickedPos()) instanceof SignalEntity signal) {
      if (!level.isClientSide()) {
        signal.setCustomName(itemStack.getHoverName());
        if (!player.isCreative()) {
          itemStack.shrink(1);
        }
      }
      return InteractionResult.sidedSuccess(level.isClientSide());
    }
    return InteractionResult.PASS;
  }

  @Override
  public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> lines,
      TooltipFlag tooltipFlag) {
    lines.add(Component.literal("- ")
        .append(Component.translatable(Tips.SIGNAL_LABEL_DESC1)).append(" -")
        .withStyle(ChatFormatting.BLUE));
    lines.add(Component.translatable(Tips.SIGNAL_LABEL_DESC2)
        .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
  }
}

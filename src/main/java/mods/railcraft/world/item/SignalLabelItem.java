package mods.railcraft.world.item;

import java.util.function.Consumer;
import mods.railcraft.Translations.Tips;
import mods.railcraft.api.signal.entity.SignalEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;

public class SignalLabelItem extends Item {

  public SignalLabelItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult onItemUseFirst(ItemStack itemStack, UseOnContext context) {
    var level = context.getLevel();
    var player = context.getPlayer();
    if (player.isShiftKeyDown() && itemStack.has(DataComponents.CUSTOM_NAME)
        && level.getBlockEntity(context.getClickedPos()) instanceof SignalEntity signal) {
      if (!level.isClientSide()) {
        signal.setCustomName(itemStack.getHoverName());
        if (!player.isCreative()) {
          itemStack.shrink(1);
        }
      }
      return InteractionResult.SUCCESS;
    }
    return InteractionResult.PASS;
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context,
      TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
    tooltipAdder.accept(Component.translatable(Tips.SIGNAL_LABEL_DESC1).withStyle(ChatFormatting.BLUE));
    tooltipAdder.accept(Component.translatable(Tips.SIGNAL_LABEL_DESC2)
        .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
  }
}

package mods.railcraft.world.item;

import java.util.List;
import mods.railcraft.Translations;
import mods.railcraft.world.entity.vehicle.TrackRemover;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class TrackRemoverCartItem extends CartItem {

  public TrackRemoverCartItem(Properties properties) {
    super(TrackRemover::new, properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable Level level,
      List<Component> tooltipComponents, TooltipFlag isAdvanced) {
    super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    tooltipComponents.add(Component.translatable(Translations.Tips.TRACK_REMOVER)
        .withStyle(ChatFormatting.GRAY));
  }
}

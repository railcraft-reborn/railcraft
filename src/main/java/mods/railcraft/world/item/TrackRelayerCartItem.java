package mods.railcraft.world.item;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.world.entity.vehicle.TrackRelayer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class TrackRelayerCartItem extends CartItem {

  public TrackRelayerCartItem(Properties properties) {
    super(TrackRelayer::new, properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable Level level,
      List<Component> tooltipComponents, TooltipFlag isAdvanced) {
    super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    tooltipComponents.add(Component.translatable(Translations.Tips.TRACK_RELAYER)
        .withStyle(ChatFormatting.GRAY));
  }
}

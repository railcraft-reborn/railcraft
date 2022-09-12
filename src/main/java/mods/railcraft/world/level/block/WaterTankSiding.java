package mods.railcraft.world.level.block;

import java.util.List;
import mods.railcraft.Translations.Tips;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class WaterTankSiding extends Block {

  public WaterTankSiding(Properties properties) {
    super(properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level,
      List<Component> tooltip, TooltipFlag flag) {
    tooltip.add(Component.translatable(Tips.WATER_TANK_SIDING).withStyle(ChatFormatting.GRAY));
  }
}

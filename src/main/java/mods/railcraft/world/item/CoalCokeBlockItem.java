package mods.railcraft.world.item;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class CoalCokeBlockItem extends BlockItem {

  public CoalCokeBlockItem(Properties properties) {
    super(RailcraftBlocks.COKE_BLOCK.get(), properties);
  }

  @Override
  public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
    return CoalCokeItem.BURN_TIME * 10;
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip,
      TooltipFlag flag) {
    super.appendHoverText(stack, level, tooltip, flag);
    tooltip.add(Component.translatable(Translations.Tips.COAL_COKE_BLOCK, getBurnTime(stack, null))
        .withStyle(ChatFormatting.GRAY));
  }
}

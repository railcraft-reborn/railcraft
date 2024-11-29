package mods.railcraft.world.level.block;

import java.util.List;
import mods.railcraft.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CoalCokeBlock extends Block {

  private final int spread, flammability;

  public CoalCokeBlock(int flammability, int spread, Properties properties) {
    super(properties);
    this.flammability = flammability;
    this.spread = spread;
  }

  @Override
  public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos,
      Direction direction) {
    return true;
  }

  @Override
  public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos,
      Direction direction) {
    return spread;
  }

  @Override
  public int getFlammability(BlockState state, BlockGetter level, BlockPos pos,
      Direction direction) {
    return flammability;
  }

  @Override
  public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip,
      TooltipFlag flag) {
    super.appendHoverText(stack, context, tooltip, flag);
    var burnTime = stack.getBurnTime(null, context.level().fuelValues());
    tooltip.add(Component.translatable(Translations.Tips.COAL_COKE_BLOCK, burnTime)
        .withStyle(ChatFormatting.GRAY));
  }
}

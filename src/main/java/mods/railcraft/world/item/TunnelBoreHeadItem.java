package mods.railcraft.world.item;

import java.util.Set;
import javax.annotation.Nullable;
import mods.railcraft.api.carts.TunnelBoreHead;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TieredItem;
import net.minecraftforge.common.ToolType;

public abstract class TunnelBoreHeadItem extends TieredItem implements TunnelBoreHead {

  public TunnelBoreHeadItem(IItemTier tier, Properties properties) {
    super(tier, properties);
  }

  @Override
  public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
    return true;
  }

  @Override
  public int getHarvestLevel() {
    return this.getTier().getLevel();
  }

  @Override
  public int getHarvestLevel(ItemStack stack, ToolType toolType, @Nullable PlayerEntity player,
      @Nullable BlockState blockState) {
    return TunnelBoreHead.super.getHarvestLevel(stack, toolType, player, blockState);
  }

  @Override
  public Set<ToolType> getToolTypes(ItemStack stack) {
    return TunnelBoreHead.super.getToolTypes(stack);
  }
}

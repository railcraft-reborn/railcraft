package mods.railcraft.world.item.tunnelbore;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.google.common.collect.Sets;
import mods.railcraft.api.carts.TunnelBoreHead;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public abstract class TunnelBoreHeadItem extends TieredItem implements TunnelBoreHead {

  private static final Set<ToolAction> TOOL_ACTIONS =
      Stream.of(ToolActions.AXE_DIG, ToolActions.PICKAXE_DIG, ToolActions.SHOVEL_DIG)
          .collect(Collectors.toCollection(Sets::newIdentityHashSet));

  public TunnelBoreHeadItem(Tier tier, Properties properties) {
    super(tier, properties);
  }

  @Override
  public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
    return true;
  }

  @Override
  public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
    return TOOL_ACTIONS.contains(toolAction);
  }

  @Override
  public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
    return TierSortingRegistry.isCorrectTierForDrops(this.getTier(), state);
  }
}

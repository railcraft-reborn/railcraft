package mods.railcraft.world.item.tunnelbore;

import java.util.Set;
import mods.railcraft.api.carts.TunnelBoreHead;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.TierSortingRegistry;
import net.neoforged.neoforge.common.ToolAction;
import net.neoforged.neoforge.common.ToolActions;

public class TunnelBoreHeadItem extends TieredItem implements TunnelBoreHead {

  private static final Set<ToolAction> TOOL_ACTIONS =
      Set.of(ToolActions.AXE_DIG, ToolActions.PICKAXE_DIG, ToolActions.SHOVEL_DIG);
  private final ResourceLocation textureLocation;

  public TunnelBoreHeadItem(Tier tier, String tierName, Properties properties) {
    super(tier, properties);
    this.textureLocation =
        RailcraftConstants.rl("textures/entity/tunnel_bore/%s_tunnel_bore.png".formatted(tierName));
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

  @Override
  public ResourceLocation getTextureLocation() {
    return this.textureLocation;
  }
}

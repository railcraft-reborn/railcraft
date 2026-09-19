package mods.railcraft.world.inventory.slot;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;

public class WaterSlot extends RailcraftSlot {

  public WaterSlot(Container container, int slotIndex, int posX, int posY) {
    super(container, slotIndex, posX, posY);
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    return FluidUtil.getFirstStackContained(stack).is(FluidTags.WATER);
  }
}

package mods.railcraft.world.inventory.slot;

import net.minecraft.world.Container;

public class BlockFilterSlotLinked extends BlockFilterSlot {

  public BlockFilterSlotLinked(BlockFilterSlot slot, Container container,
      int slotIndex, int posX, int posY) {
    super(slot::mayPlace, container, slotIndex, posX, posY);
  }
}

package mods.railcraft.world.inventory;

import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.ItemManipulatorBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class ItemManipulatorMenu extends ManipulatorMenu<ItemManipulatorBlockEntity> {

  public ItemManipulatorMenu(ItemManipulatorBlockEntity manipulator,
      int id, PlayerInventory inventory) {
    super(manipulator, RailcraftMenuTypes.ITEM_MANIPULATOR.get(), id, inventory);
  }

  @Override
  protected void addSlots(ItemManipulatorBlockEntity manipulator) {
    for (int i = 0; i < 3; i++) {
      for (int k = 0; k < 3; k++) {
        this.addSlot(new SlotRailcraft(
            manipulator.getItemFilters(), k + i * 3, 8 + k * 18, 26 + i * 18).setPhantom());
      }
    }

    for (int i = 0; i < 3; i++) {
      for (int k = 0; k < 3; k++) {
        this.addSlot(manipulator.getBufferSlot(k + i * 3, 116 + k * 18, 26 + i * 18));
      }
    }
  }

  public static ItemManipulatorMenu create(int id, PlayerInventory inventory, PacketBuffer data) {
    BlockPos blockPos = data.readBlockPos();
    ItemManipulatorBlockEntity manipulator = LevelUtil
        .getBlockEntity(inventory.player.level, blockPos,
            ItemManipulatorBlockEntity.class)
        .orElseThrow(() -> new IllegalStateException(
            "No item manipulator found at [" + blockPos.toString() + "]"));
    return new ItemManipulatorMenu(manipulator, id, inventory);
  }
}

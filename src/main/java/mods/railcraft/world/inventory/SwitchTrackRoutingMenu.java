package mods.railcraft.world.inventory;

import mods.railcraft.world.item.RoutingTableBookItem;
import mods.railcraft.world.level.block.entity.SwitchTrackRoutingBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SwitchTrackRoutingMenu extends RailcraftMenu {

  public SwitchTrackRoutingMenu(int id, Inventory inventory,
      SwitchTrackRoutingBlockEntity blockEntity) {
    super(RailcraftMenuTypes.SWITCH_TRACK_ROUTING.get(), id, inventory.player, blockEntity::stillValid);

    var routingTableBookSlot = new Slot(blockEntity, 0, 35, 24) {
      @Override
      public boolean mayPlace(ItemStack stack) {
        return RoutingTableBookItem.FILTER.test(stack);
      }

      @Override
      public void setChanged() {
        super.setChanged();
        blockEntity.resetLogic();
      }
    };

    this.addSlot(routingTableBookSlot);
    this.addInventorySlots(inventory, 160);
  }
}

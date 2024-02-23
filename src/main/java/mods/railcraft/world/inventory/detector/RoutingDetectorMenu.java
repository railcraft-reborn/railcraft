package mods.railcraft.world.inventory.detector;

import mods.railcraft.gui.widget.Widget;
import mods.railcraft.world.inventory.RailcraftMenu;
import mods.railcraft.world.inventory.RailcraftMenuTypes;
import mods.railcraft.world.item.RoutingTableBookItem;
import mods.railcraft.world.level.block.entity.detector.RoutingDetectorBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class RoutingDetectorMenu extends RailcraftMenu {

  private final Widget error;

  private final RoutingDetectorBlockEntity blockEntity;

  public RoutingDetectorMenu(int id, Inventory inventory,
      RoutingDetectorBlockEntity blockEntity) {
    super(RailcraftMenuTypes.ROUTING_DETECTOR.get(), id, inventory.player,
        blockEntity::isStillValid);
    this.blockEntity = blockEntity;
    var routingTableBookSlot = new Slot(blockEntity, 0, 35, 24) {
      @Override
      public boolean mayPlace(ItemStack stack) {
        return RoutingTableBookItem.FILTER.test(stack) && stack.getCount() == 1;
      }

      @Override
      public boolean mayPickup(Player player) {
        return blockEntity.canAccess(player.getGameProfile());
      }

      @Override
      public boolean allowModification(Player player) {
        if (blockEntity.canAccess(player.getGameProfile())) {
          return super.allowModification(player);
        }
        return false;
      }

      @Override
      public void setChanged() {
        super.setChanged();
        blockEntity.resetLogic();
        error.hidden = blockEntity.logicError().isEmpty();
      }
    };

    this.addWidget(error = new Widget(16, 24, 176, 0, 16, 16));
    this.addSlot(routingTableBookSlot);
    this.addInventorySlots(inventory, 160);
  }

  public RoutingDetectorBlockEntity getRoutingDetector() {
    return this.blockEntity;
  }

  public Widget getErrorWidget() {
    return this.error;
  }
}

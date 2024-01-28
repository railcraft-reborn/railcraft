package mods.railcraft.world.inventory;

import mods.railcraft.gui.widget.Widget;
import mods.railcraft.world.item.RoutingTableBookItem;
import mods.railcraft.world.level.block.entity.SwitchTrackRouterBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SwitchTrackRouterMenu extends RailcraftMenu {

  private final Widget error;
  private final SwitchTrackRouterBlockEntity switchTrackRouter;

  public SwitchTrackRouterMenu(int id, Inventory inventory,
      SwitchTrackRouterBlockEntity blockEntity) {
    super(RailcraftMenuTypes.SWITCH_TRACK_ROUTER.get(), id, inventory.player,
        blockEntity::isStillValid);
    this.switchTrackRouter = blockEntity;

    var routingTableBookSlot = new Slot(switchTrackRouter, 0, 35, 24) {
      @Override
      public boolean mayPlace(ItemStack stack) {
        return RoutingTableBookItem.FILTER.test(stack) && stack.getCount() == 1;
      }

      @Override
      public boolean mayPickup(Player player) {
        return switchTrackRouter.canAccess(player.getGameProfile());
      }

      @Override
      public boolean allowModification(Player player) {
        if (switchTrackRouter.canAccess(player.getGameProfile())) {
          return super.allowModification(player);
        }
        return false;
      }

      @Override
      public void setChanged() {
        super.setChanged();
        switchTrackRouter.resetLogic();
        error.hidden = switchTrackRouter.logicError().isEmpty();
      }
    };

    this.addWidget(error = new Widget(16, 24, 176, 0, 16, 16));
    this.addSlot(routingTableBookSlot);
    this.addInventorySlots(inventory, 160);
  }

  public SwitchTrackRouterBlockEntity getSwitchTrackRouter() {
    return this.switchTrackRouter;
  }

  public Widget getErrorWidget() {
    return this.error;
  }
}

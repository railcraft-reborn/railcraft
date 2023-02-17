package mods.railcraft.world.inventory;

import java.util.Optional;
import mods.railcraft.gui.widget.Widget;
import mods.railcraft.util.routing.RoutingLogic;
import mods.railcraft.world.item.RoutingTableBookItem;
import mods.railcraft.world.level.block.entity.SwitchTrackRouterBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SwitchTrackRouterMenu extends RailcraftMenu {

  private final Widget error;
  private final SwitchTrackRouterBlockEntity switchTrackRouting;

  public SwitchTrackRouterMenu(int id, Inventory inventory,
      SwitchTrackRouterBlockEntity blockEntity) {
    super(RailcraftMenuTypes.SWITCH_TRACK_ROUTER.get(), id, inventory.player, blockEntity::stillValid);
    this.switchTrackRouting = blockEntity;

    var routingTableBookSlot = new Slot(switchTrackRouting, 0, 35, 24) {
      @Override
      public boolean mayPlace(ItemStack stack) {
        return RoutingTableBookItem.FILTER.test(stack);
      }

      @Override
      public boolean mayPickup(Player player) {
        return switchTrackRouting.canAccess(player.getGameProfile());
      }

      @Override
      public boolean allowModification(Player player) {
        if (switchTrackRouting.canAccess(player.getGameProfile())) {
          return super.allowModification(player);
        }
        return false;
      }

      @Override
      public void setChanged() {
        super.setChanged();
        switchTrackRouting.resetLogic();
        if (switchTrackRouting.getLogic().isPresent()) {
          error.hidden = switchTrackRouting.getLogic().get().getError() == null;
        }
      }
    };

    this.addWidget(error = new Widget(16, 24, 176, 0, 16, 16));
    this.addSlot(routingTableBookSlot);
    this.addInventorySlots(inventory, 160);
  }

  public SwitchTrackRouterBlockEntity getSwitchTrackRouting() {
    return this.switchTrackRouting;
  }

  public Widget getErrorWidget() {
    return this.error;
  }

  public Optional<RoutingLogic> getLogic() {
    return this.switchTrackRouting.getLogic();
  }
}

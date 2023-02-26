package mods.railcraft.world.inventory;

import mods.railcraft.world.item.GoldenTicketItem;
import mods.railcraft.world.level.block.entity.track.RoutingTrackBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class RoutingTrackMenu extends RailcraftMenu {

  private final RoutingTrackBlockEntity blockEntity;

  public RoutingTrackMenu(int id, Inventory inventory, RoutingTrackBlockEntity blockEntity) {
    super(RailcraftMenuTypes.ROUTING_TRACK.get(), id, inventory.player, blockEntity::stillValid);
    this.blockEntity = blockEntity;

    var goldenTicketSlot = new Slot(blockEntity, 0, 44, 24) {
      @Override
      public boolean mayPlace(ItemStack stack) {
        return GoldenTicketItem.FILTER.test(stack) && stack.getCount() == 1;
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
    };

    this.addSlot(goldenTicketSlot);
    this.addInventorySlots(inventory, 140);
  }

  public RoutingTrackBlockEntity getRoutingBlockEntity() {
    return blockEntity;
  }
}

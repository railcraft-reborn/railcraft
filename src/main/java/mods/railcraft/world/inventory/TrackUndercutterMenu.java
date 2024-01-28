package mods.railcraft.world.inventory;

import mods.railcraft.world.entity.vehicle.TrackUndercutter;
import mods.railcraft.world.inventory.slot.BlockFilterSlot;
import mods.railcraft.world.inventory.slot.SlotLinked;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class TrackUndercutterMenu extends RailcraftMenu {

  private final TrackUndercutter trackUndercutter;

  public TrackUndercutterMenu(int id, Inventory inventory, TrackUndercutter trackUndercutter) {
    super(RailcraftMenuTypes.TRACK_UNDERCUTTER.get(), id, inventory.player,
        trackUndercutter::stillValid);
    this.trackUndercutter = trackUndercutter;

    var container = trackUndercutter.getPattern();
    this.addSlot(new BlockFilterSlot(container, 0, 17, 36));
    this.addSlot(new BlockFilterSlot(container, 1, 35, 36));
    this.addSlot(new BlockFilterSlot(container, 2, 17, 78));
    this.addSlot(new BlockFilterSlot(container, 3, 35, 78));
    var under = new SlotUndercutterFilter(container, 4, 80, 36, trackUndercutter);
    this.addSlot(under);
    var side = new SlotUndercutterFilter(container, 5, 80, 78, trackUndercutter);
    this.addSlot(side);
    this.addSlot(new SlotLinked(trackUndercutter, 0, 131, 36, under));
    this.addSlot(new SlotLinked(trackUndercutter, 1, 131, 78, side));

    this.addInventorySlots(inventory, 205);
  }

  public TrackUndercutter getTrackUndercutter() {
    return trackUndercutter;
  }

  private static class SlotUndercutterFilter extends BlockFilterSlot {

    private final TrackUndercutter trackUndercutter;

    public SlotUndercutterFilter(Container container, int slotIndex, int posX, int posY,
        TrackUndercutter trackUndercutter) {
      super(container, slotIndex, posX, posY);
      this.trackUndercutter = trackUndercutter;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
      return TrackUndercutter.isValidBallast(stack, trackUndercutter);
    }
  }
}

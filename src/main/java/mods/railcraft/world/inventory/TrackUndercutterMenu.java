package mods.railcraft.world.inventory;

import mods.railcraft.world.entity.vehicle.TrackUndercutter;
import mods.railcraft.world.inventory.slot.BlockFilterSlot;
import mods.railcraft.world.inventory.slot.BlockFilterSlotLinked;
import mods.railcraft.world.inventory.slot.SlotLinked;
import net.minecraft.world.entity.player.Inventory;

public class TrackUndercutterMenu extends RailcraftMenu {

  private final TrackUndercutter trackUndercutter;

  public TrackUndercutterMenu(int id, Inventory inventory, TrackUndercutter trackUndercutter) {
    super(RailcraftMenuTypes.TRACK_UNDERCUTTER.get(), id, inventory.player,
        trackUndercutter::stillValid);
    this.trackUndercutter = trackUndercutter;

    var container = trackUndercutter.getPattern();

    var under = new BlockFilterSlot(trackUndercutter::isValidBallast, container,
        TrackUndercutter.SLOT_REPLACE_UNDER, 80, 36);
    this.addSlot(under);
    this.addSlot(new BlockFilterSlotLinked(under, container,
        TrackUndercutter.SLOT_EXIST_UNDER_A, 17, 36));
    this.addSlot(new BlockFilterSlotLinked(under, container,
        TrackUndercutter.SLOT_EXIST_UNDER_B, 35, 36));

    var side = new BlockFilterSlot(trackUndercutter::isValidBallast, container,
        TrackUndercutter.SLOT_REPLACE_SIDE, 80, 78);
    this.addSlot(side);
    this.addSlot(new BlockFilterSlotLinked(side, container,
        TrackUndercutter.SLOT_EXIST_SIDE_A, 17, 78));
    this.addSlot(new BlockFilterSlotLinked(side, container,
        TrackUndercutter.SLOT_EXIST_SIDE_B, 35, 78));

    this.addSlot(new SlotLinked(trackUndercutter,
        TrackUndercutter.SLOT_STOCK_UNDER, 131, 36, under));
    this.addSlot(new SlotLinked(trackUndercutter,
        TrackUndercutter.SLOT_STOCK_SIDE, 131, 78, side));

    this.addInventorySlots(inventory, 205);
  }

  public TrackUndercutter getTrackUndercutter() {
    return this.trackUndercutter;
  }
}

package mods.railcraft.world.inventory;

import mods.railcraft.api.carts.TunnelBoreHead;
import mods.railcraft.util.container.StackFilter;
import mods.railcraft.world.entity.vehicle.TunnelBore;
import mods.railcraft.world.inventory.slots.ItemFilterSlot;
import mods.railcraft.world.inventory.slots.RailcraftSlot;
import net.minecraft.world.entity.player.Inventory;

public class TunnelBoreMenu extends RailcraftMenu {

  public static final int IMAGE_HEIGHT = 222;

  private final TunnelBore tunnelBore;

  public TunnelBoreMenu(int id, Inventory inventory, TunnelBore tunnelBore) {
    super(RailcraftMenuTypes.TUNNEL_BORE.get(), id, inventory.player, tunnelBore::stillValid);
    this.tunnelBore = tunnelBore;

    this.addSlot(new ItemFilterSlot(StackFilter.of(TunnelBoreHead.class), tunnelBore, 0, 17, 36)
        .setStackLimit(1));

    // Fuel
    for (int i = 0; i < 6; i++) {
      this.addSlot(new RailcraftSlot(tunnelBore, i + 1, 62 + i * 18, 36));
    }

    // Ballast
    for (int i = 0; i < 9; i++) {
      this.addSlot(new RailcraftSlot(tunnelBore, i + 7, 8 + i * 18, 72));
    }

    // Track
    for (int i = 0; i < 9; i++) {
      this.addSlot(new RailcraftSlot(tunnelBore, i + 16, 8 + i * 18, 108));
    }

    this.addPlayerSlots(inventory, IMAGE_HEIGHT);

    this.addDataSlot(new SimpleDataSlot(
        tunnelBore::getBurnTime, tunnelBore::setBurnTime));

    this.addDataSlot(new SimpleDataSlot(
        tunnelBore::getFuel, tunnelBore::setFuel));
  }

  public TunnelBore getTunnelBore() {
    return this.tunnelBore;
  }
}

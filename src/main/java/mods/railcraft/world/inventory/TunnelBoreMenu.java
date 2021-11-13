package mods.railcraft.world.inventory;

import mods.railcraft.api.carts.TunnelBoreHead;
import mods.railcraft.util.inventory.filters.StackFilters;
import mods.railcraft.world.entity.cart.TunnelBoreEntity;
import net.minecraft.entity.player.PlayerInventory;

public class TunnelBoreMenu extends RailcraftMenu {

  public static final int IMAGE_HEIGHT = 222;

  private final TunnelBoreEntity tunnelBore;

  public TunnelBoreMenu(int id, PlayerInventory playerInventory, TunnelBoreEntity tunnelBore) {
    super(RailcraftMenuTypes.TUNNEL_BORE.get(), id, playerInventory);
    this.tunnelBore = tunnelBore;

    this.addSlot(new SlotStackFilter(StackFilters.of(TunnelBoreHead.class), tunnelBore, 0, 17, 36)
        .setStackLimit(1));

    // Fuel
    for (int i = 0; i < 6; i++) {
      this.addSlot(new SlotRailcraft(tunnelBore, i + 1, 62 + i * 18, 36));
    }

    // Ballast
    for (int i = 0; i < 9; i++) {
      this.addSlot(new SlotRailcraft(tunnelBore, i + 7, 8 + i * 18, 72));
    }

    // Track
    for (int i = 0; i < 9; i++) {
      this.addSlot(new SlotRailcraft(tunnelBore, i + 16, 8 + i * 18, 108));
    }

    this.addPlayerSlots(playerInventory, IMAGE_HEIGHT);

    this.addDataSlot(new SimpleIntReferenceHolder(
        tunnelBore::getBurnTime, tunnelBore::setBurnTime));

    this.addDataSlot(new SimpleIntReferenceHolder(
        tunnelBore::getFuel, tunnelBore::setFuel));
  }

  public TunnelBoreEntity getTunnelBore() {
    return this.tunnelBore;
  }
}

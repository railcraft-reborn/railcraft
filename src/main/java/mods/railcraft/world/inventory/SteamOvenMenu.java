package mods.railcraft.world.inventory;

import mods.railcraft.gui.widget.FluidGaugeWidget;
import mods.railcraft.world.inventory.slot.RailcraftSlot;
import mods.railcraft.world.level.block.entity.SteamOvenBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.FurnaceResultSlot;

public final class SteamOvenMenu extends CrafterMenu {

  private final SteamOvenBlockEntity steamOven;
  private final FluidGaugeWidget steamFluidGauge;

  public SteamOvenMenu(int id, Inventory inventory, SteamOvenBlockEntity steamOven) {
    super(RailcraftMenuTypes.STEAM_OVEN.get(), id, inventory.player, steamOven.getSteamOvenModule());
    this.steamOven = steamOven;

    var logic = steamOven.getSteamOvenModule();
    this.steamFluidGauge = new FluidGaugeWidget(logic.getSteamTank(), 94, 20, 176, 0, 16, 47);
    this.addWidget(this.steamFluidGauge);

    for (int i = 0; i < 3; i++) {
      for (int k = 0; k < 3; k++) {
        this.addSlot(new RailcraftSlot(this.module, i * 3 + k, 8 + k * 18, 17 + i * 18));
      }
    }
    for (int i = 0; i < 3; i++) {
      for (int k = 0; k < 3; k++) {
        this.addSlot(new FurnaceResultSlot(inventory.player, this.module, 9 + i * 3 + k,
            116 + k * 18, 17 + i * 18));
      }
    }
    this.addInventorySlots(inventory);
  }

  public SteamOvenBlockEntity getSteamOven() {
    return this.steamOven;
  }

  public FluidGaugeWidget getSteamFluidGauge() {
    return steamFluidGauge;
  }
}

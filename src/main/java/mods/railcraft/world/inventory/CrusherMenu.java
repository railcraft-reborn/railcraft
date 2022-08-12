package mods.railcraft.world.inventory;

import mods.railcraft.gui.widget.FEEnergyIndicator;
import mods.railcraft.gui.widget.GaugeWidget;
import mods.railcraft.world.level.block.entity.CrusherBlockEntity;
import net.minecraft.world.entity.player.Inventory;

public class CrusherMenu extends CrafterMenu {

  private final CrusherBlockEntity crusher;
  private final GaugeWidget energyWidget;

  public CrusherMenu(int id, Inventory inventory, CrusherBlockEntity crusher) {
    super(RailcraftMenuTypes.CRUSHER.get(), id, inventory.player, crusher.getCrusherModule());

    this.crusher = crusher;

    var logic = crusher.getCrusherModule();

    var energyIndicator = new FEEnergyIndicator(logic.getEnergyStorage());
    this.energyWidget = new GaugeWidget(energyIndicator, 157, 23, 176, 53, 6, 48);
    addWidget(energyWidget);

    for (int i = 0; i < 3; i++) {
      for (int k = 0; k < 3; k++) {
        addSlot(new RailcraftSlot(logic, i * 3 + k, 8 + k * 18, 21 + i * 18));
      }
    }
    for (int i = 0; i < 3; i++) {
      for (int k = 0; k < 3; k++) {
        addSlot(new OutputSlot(logic, 9 + i * 3 + k, 98 + k * 18, 21 + i * 18));
      }
    }
    this.addInventorySlots(inventory, 171);
  }

  public CrusherBlockEntity getCrusher() {
    return this.crusher;
  }

  public GaugeWidget getEnergyWidget() {
    return energyWidget;
  }
}

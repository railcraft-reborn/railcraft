package mods.railcraft.world.inventory;

import mods.railcraft.gui.widget.AnalogGaugeWidget;
import mods.railcraft.gui.widget.ChargeNetworkLevelIndicator;
import mods.railcraft.world.inventory.slot.OutputSlot;
import mods.railcraft.world.inventory.slot.RailcraftSlot;
import mods.railcraft.world.level.block.entity.CrusherBlockEntity;
import net.minecraft.world.entity.player.Inventory;

public class CrusherMenu extends CrafterMenu {

  private final CrusherBlockEntity crusher;
  private final AnalogGaugeWidget energyWidget;

  public CrusherMenu(int id, Inventory inventory, CrusherBlockEntity crusher) {
    super(RailcraftMenuTypes.CRUSHER.get(), id, inventory.player, crusher.getCrusherModule());

    this.crusher = crusher;

    var logic = crusher.getCrusherModule();

    var chargeIndicator =
        new ChargeNetworkLevelIndicator(crusher.getLevel(), crusher.getBlockPos());
    this.energyWidget = new AnalogGaugeWidget(chargeIndicator, 74, 59, 28, 14, 99, 65);
    addWidget(energyWidget);

    for (int i = 0; i < 3; i++) {
      for (int k = 0; k < 3; k++) {
        addSlot(new RailcraftSlot(logic, i * 3 + k, 17 + k * 18, 21 + i * 18));
      }
    }
    for (int i = 0; i < 3; i++) {
      for (int k = 0; k < 3; k++) {
        addSlot(new OutputSlot(logic, 9 + i * 3 + k, 107 + k * 18, 21 + i * 18));
      }
    }
    this.addInventorySlots(inventory, 171);
  }

  public CrusherBlockEntity getCrusher() {
    return this.crusher;
  }

  public AnalogGaugeWidget getEnergyWidget() {
    return energyWidget;
  }
}

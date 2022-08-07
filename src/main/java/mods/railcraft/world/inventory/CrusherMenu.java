package mods.railcraft.world.inventory;

import mods.railcraft.world.level.block.entity.CrusherBlockEntity;
import net.minecraft.world.entity.player.Inventory;

public class CrusherMenu extends CrafterMenu {

  private final CrusherBlockEntity crusher;

  public CrusherMenu(int id, Inventory inventory, CrusherBlockEntity crusher) {
    super(RailcraftMenuTypes.CRUSHER.get(), id, inventory.player, crusher.getCrusherModule());

    this.crusher = crusher;

    var logic = crusher.getCrusherModule();

    //addWidget(new AnalogGaugeWidget(new ChargeNetworkIndicator(crusher.getLevel(), crusher.blockPos()), 74, 59, 28, 14, 99, 65));

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
}

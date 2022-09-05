package mods.railcraft.world.inventory;

import mods.railcraft.gui.widget.FluidGaugeWidget;
import mods.railcraft.world.inventory.slots.EmptyFluidContainerSlot;
import mods.railcraft.world.inventory.slots.OutputSlot;
import mods.railcraft.world.inventory.slots.RailcraftSlot;
import mods.railcraft.world.level.block.entity.CokeOvenBlockEntity;
import net.minecraft.world.entity.player.Inventory;

public class CokeOvenMenu extends CrafterMenu {

  private final CokeOvenBlockEntity cokeOven;
  private final FluidGaugeWidget fluidGauge;

  public CokeOvenMenu(int id, Inventory inventory, CokeOvenBlockEntity cokeOven) {
    super(RailcraftMenuTypes.COKE_OVEN.get(), id, inventory.player, cokeOven.getCokeOvenModule());

    this.cokeOven = cokeOven;

    var logic = cokeOven.getCokeOvenModule();

    this.addWidget(this.fluidGauge = new FluidGaugeWidget(logic.getTank(), 90, 24, 176, 0, 48, 47));

    this.addSlot(new RailcraftSlot(logic, 0, 16, 43));
    this.addSlot(new OutputSlot(logic, 1, 62, 43));
    this.addSlot(new OutputSlot(logic, 2, 149, 57));
    this.addSlot(new EmptyFluidContainerSlot(logic, 3, 149, 22));

    this.addInventorySlots(inventory);
  }

  public CokeOvenBlockEntity getCokeOven() {
    return this.cokeOven;
  }

  public FluidGaugeWidget getFluidGauge() {
    return this.fluidGauge;
  }
}

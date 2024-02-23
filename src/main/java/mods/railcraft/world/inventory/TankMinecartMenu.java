package mods.railcraft.world.inventory;

import mods.railcraft.gui.widget.FluidGaugeWidget;
import mods.railcraft.util.container.StackFilter;
import mods.railcraft.world.entity.vehicle.TankMinecart;
import mods.railcraft.world.inventory.slot.FluidFilterSlot;
import mods.railcraft.world.inventory.slot.ItemFilterSlot;
import mods.railcraft.world.inventory.slot.OutputSlot;
import net.minecraft.world.entity.player.Inventory;

public class TankMinecartMenu extends RailcraftMenu {

  private final FluidGaugeWidget fluidGauge;

  public TankMinecartMenu(int id, Inventory inventory, TankMinecart tankMinecart) {
    super(RailcraftMenuTypes.TANK_MINECART.get(), id, inventory.player, tankMinecart::stillValid);

    this.addWidget(this.fluidGauge =
        new FluidGaugeWidget(tankMinecart.getTankManager(), 35, 23, 176, 0, 16, 47));

    this.addSlot(new FluidFilterSlot(tankMinecart.getFilterInv(),
        TankMinecart.SLOT_INPUT, 80, 21));
    this.addSlot(new ItemFilterSlot(StackFilter.FLUID_CONTAINER, tankMinecart.getInvLiquids(),
        TankMinecart.SLOT_INPUT, 116, 21));
    this.addSlot(new OutputSlot(tankMinecart.getInvLiquids(),
        TankMinecart.SLOT_PROCESSING, 116, 57));
    this.addSlot(new OutputSlot(tankMinecart.getInvLiquids(),
        TankMinecart.SLOT_OUTPUT, 80, 57));

    this.addInventorySlots(inventory);
  }

  public FluidGaugeWidget getFluidGauge() {
    return this.fluidGauge;
  }
}

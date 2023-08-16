package mods.railcraft.world.inventory;

import mods.railcraft.gui.widget.FluidGaugeWidget;
import mods.railcraft.world.inventory.slot.OutputSlot;
import mods.railcraft.world.inventory.slot.RailcraftSlot;
import mods.railcraft.world.level.block.entity.tank.TankBlockEntity;
import mods.railcraft.world.module.TankModule;
import net.minecraft.world.entity.player.Inventory;

public class TankMenu extends RailcraftMenu {

  private final FluidGaugeWidget fluidGauge;

  public TankMenu(int id, Inventory inventory, TankBlockEntity tank) {
    super(RailcraftMenuTypes.TANK.get(), id, inventory.player, tank::isStillValid);

    this.addWidget(this.fluidGauge = this.createGauge(tank, 35, 20, 176, 0, 48, 47));

    this.addSlot(new RailcraftSlot(tank.getModule(), TankModule.SLOT_INPUT, 116, 18));
    this.addSlot(new OutputSlot(tank.getModule(), TankModule.SLOT_PROCESS, 140, 36));
    this.addSlot(new OutputSlot(tank.getModule(), TankModule.SLOT_OUTPUT, 116, 54));

    this.addInventorySlots(inventory);
  }

  public FluidGaugeWidget getFluidGauge() {
    return this.fluidGauge;
  }

  private FluidGaugeWidget createGauge(TankBlockEntity blockEntity, int x, int y, int u, int v,
      int w, int h) {
    var tank = blockEntity.getModule().getTank();
    return new FluidGaugeWidget(tank, x, y, u, v, w, h);
  }
}

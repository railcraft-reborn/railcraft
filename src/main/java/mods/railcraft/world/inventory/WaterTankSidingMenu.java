package mods.railcraft.world.inventory;

import java.util.Collection;
import mods.railcraft.gui.widget.FluidGaugeWidget;
import mods.railcraft.gui.widget.WaterCollectionGaugeWidget;
import mods.railcraft.world.inventory.slot.OutputSlot;
import mods.railcraft.world.inventory.slot.RailcraftSlot;
import mods.railcraft.world.level.block.entity.WaterTankSidingBlockEntity;
import mods.railcraft.world.module.TankModule;
import mods.railcraft.world.module.WaterCollectionModule;
import net.minecraft.world.entity.player.Inventory;

public class WaterTankSidingMenu extends RailcraftMenu {

  private final FluidGaugeWidget fluidGauge;

  public WaterTankSidingMenu(int id, Inventory inventory, WaterTankSidingBlockEntity tank) {
    super(RailcraftMenuTypes.WATER_TANK_SIDING.get(), id, inventory.player, tank::stillValid);

    this.addWidget(this.fluidGauge = this.createGauge(tank, 35, 20, 176, 0, 48, 47));

    this.addSlot(new RailcraftSlot(tank.getModule(), TankModule.SLOT_INPUT, 116, 18));
    this.addSlot(new OutputSlot(tank.getModule(), TankModule.SLOT_PROCESS, 140, 36));
    this.addSlot(new OutputSlot(tank.getModule(), TankModule.SLOT_OUTPUT, 116, 54));

    this.addInventorySlots(inventory);
  }

  public FluidGaugeWidget getFluidGauge() {
    return this.fluidGauge;
  }

  private FluidGaugeWidget createGauge(WaterTankSidingBlockEntity blockEntity, int x, int y, int u,
      int v, int w, int h) {
    var tank = blockEntity.getModule().getTank();
    var modules = blockEntity.getMembers().stream()
        .flatMap(Collection::stream)
        .flatMap(member -> member.getModule(WaterCollectionModule.class).stream())
        .toList();
    return new WaterCollectionGaugeWidget(modules, tank, x, y, u, v, w, h);
  }
}

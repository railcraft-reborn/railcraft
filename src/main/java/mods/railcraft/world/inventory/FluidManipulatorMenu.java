package mods.railcraft.world.inventory;

import mods.railcraft.gui.widget.FluidGaugeWidget;
import mods.railcraft.util.container.StackFilter;
import mods.railcraft.world.inventory.slots.FluidFilterSlot;
import mods.railcraft.world.inventory.slots.ItemFilterSlot;
import mods.railcraft.world.inventory.slots.OutputSlot;
import mods.railcraft.world.level.block.entity.manipulator.FluidManipulatorBlockEntity;
import net.minecraft.world.entity.player.Inventory;

public class FluidManipulatorMenu extends ManipulatorMenu<FluidManipulatorBlockEntity> {

  private final FluidGaugeWidget fluidGauge;

  public FluidManipulatorMenu(int id, Inventory inventory,
      FluidManipulatorBlockEntity manipulator) {
    super(RailcraftMenuTypes.FLUID_MANIPULATOR.get(), id, inventory, manipulator);
    this.addWidget(this.fluidGauge =
        new FluidGaugeWidget(manipulator.getTankManager().get(0), 17, 21, 176, 0, 16, 47));
  }

  public FluidGaugeWidget getFluidGauge() {
    return this.fluidGauge;
  }

  @Override
  protected void addSlots(FluidManipulatorBlockEntity manipulator) {
    this.addSlot(new FluidFilterSlot(manipulator.getFluidFilter(), 0, 116, 26));
    this.addSlot(new ItemFilterSlot(StackFilter.FLUID_CONTAINER, manipulator, 0, 152, 26));
    this.addSlot(new OutputSlot(manipulator, 1, 152, 62));
    this.addSlot(new OutputSlot(manipulator, 2, 116, 62));
  }
}

package mods.railcraft.world.inventory;

import mods.railcraft.gui.widget.FluidGaugeWidget;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.FluidManipulatorBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class FluidManipulatorMenu extends ManipulatorMenu<FluidManipulatorBlockEntity> {

  private final FluidGaugeWidget fluidGuage;

  public FluidManipulatorMenu(FluidManipulatorBlockEntity manipulator,
      int id, PlayerInventory inventory) {
    super(manipulator, RailcraftMenuTypes.FLUID_MANIPULATOR.get(), id, inventory);
    this.addWidget(this.fluidGuage =
        new FluidGaugeWidget(manipulator.getTankManager().get(0), 17, 21, 176, 0, 16, 47));
  }

  public FluidGaugeWidget getFluidGuage() {
    return this.fluidGuage;
  }

  @Override
  protected void addSlots(FluidManipulatorBlockEntity manipulator) {
    this.addSlot(new SlotFluidFilter(manipulator.getFluidFilter(), 0, 116, 26));
    this.addSlot(new SlotRailcraft(manipulator, 0, 152, 26));
    this.addSlot(new SlotOutput(manipulator, 1, 152, 62));
    this.addSlot(new SlotOutput(manipulator, 2, 116, 62));
  }

  public static FluidManipulatorMenu create(int id, PlayerInventory inventory, PacketBuffer data) {
    BlockPos blockPos = data.readBlockPos();
    FluidManipulatorBlockEntity manipulator = LevelUtil
        .getBlockEntity(inventory.player.level, blockPos,
            FluidManipulatorBlockEntity.class)
        .orElseThrow(() -> new IllegalStateException(
            "No fluid manipulator found at [" + blockPos.toString() + "]"));
    return new FluidManipulatorMenu(manipulator, id, inventory);
  }
}

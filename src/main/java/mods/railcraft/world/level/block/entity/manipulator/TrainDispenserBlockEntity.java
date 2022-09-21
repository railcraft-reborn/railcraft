package mods.railcraft.world.level.block.entity.manipulator;

import mods.railcraft.util.container.manipulator.ContainerManipulator;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.state.BlockState;

public class TrainDispenserBlockEntity extends ItemManipulatorBlockEntity {

  public TrainDispenserBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.TRAIN_DISPENSER.get(), blockPos, blockState);
  }

  @Override
  public ContainerManipulator<?> getSource() {
    return null;
  }

  @Override
  public ContainerManipulator<?> getDestination() {
    return null;
  }

  @Override
  public Slot getBufferSlot(int id, int x, int y) {
    return null;
  }
}

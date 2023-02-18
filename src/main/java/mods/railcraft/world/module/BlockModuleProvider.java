package mods.railcraft.world.module;

import java.util.function.Predicate;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.util.container.manipulator.ContainerManipulator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface BlockModuleProvider extends ModuleProvider {

  BlockPos blockPos();

  @Override
  default void dropItem(ItemStack itemStack) {
    ContainerTools.dropItem(itemStack, this.level(), this.blockPos());
  }

  default ContainerManipulator<?> findAdjacentContainers() {
    return ContainerManipulator.findAdjacent(this.level(), this.blockPos());
  }

  default ContainerManipulator<?> findAdjacentContainers(Predicate<BlockEntity> filter) {
    return ContainerManipulator.findAdjacent(this.level(), this.blockPos(), filter);
  }
}

package mods.railcraft.world.module;

import java.util.function.Predicate;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.util.container.manipulator.CompositeContainerManipulator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface BlockModuleProvider extends ModuleProvider {

  BlockPos blockPos();

  @Override
  default void dropItem(ItemStack itemStack) {
    ContainerTools.dropItem(itemStack, this.level(), this.blockPos());
  }

  default CompositeContainerManipulator<?> getAdjacentContainers() {
    return CompositeContainerManipulator.findAdjacent(this.level(), this.blockPos());
  }

  default CompositeContainerManipulator<?> getAdjacentContainers(Predicate<BlockEntity> filter) {
    return CompositeContainerManipulator.findAdjacent(this.level(), this.blockPos(), filter);
  }
}

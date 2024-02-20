package mods.railcraft.world.level.block.entity.detector;

import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.state.BlockState;

public class AnimalDetectorBlockEntity extends EntityDetectorBlockEntity<Animal> {

  public AnimalDetectorBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.ANIMAL_DETECTOR.get(), blockPos, blockState, Animal.class);
  }
}

package mods.railcraft.world.level.block.entity.worldspike;

import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class PersonalWorldSpikeBlockEntity extends WorldSpikeBlockEntity {

  public PersonalWorldSpikeBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.PERSONAL_WORLD_SPIKE.get(), blockPos, blockState);
  }
}

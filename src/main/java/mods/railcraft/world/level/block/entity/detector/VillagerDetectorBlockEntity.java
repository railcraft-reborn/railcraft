package mods.railcraft.world.level.block.entity.detector;

import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.state.BlockState;

public class VillagerDetectorBlockEntity extends EntityDetectorBlockEntity<Villager> {

  public VillagerDetectorBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.VILLAGER_DETECTOR.get(), blockPos, blockState, Villager.class);
  }
}

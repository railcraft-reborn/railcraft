package mods.railcraft.world.level.block.entity.detector;

import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.state.BlockState;

public class MobDetectorBlockEntity extends EntityDetectorBlockEntity<Mob> {

  public MobDetectorBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.MOB_DETECTOR.get(), blockPos, blockState, Mob.class);
  }
}

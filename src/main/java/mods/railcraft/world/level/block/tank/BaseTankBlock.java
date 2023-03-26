package mods.railcraft.world.level.block.tank;

import org.jetbrains.annotations.Nullable;
import mods.railcraft.world.level.block.MultiblockBlock;
import mods.railcraft.world.level.block.entity.tank.TankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BaseTankBlock extends MultiblockBlock {

  public BaseTankBlock(Properties properties) {
    super(properties);
  }

  @Override
  @Nullable
  public abstract TankBlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState);

  @Override
  public boolean isValidSpawn(BlockState state, BlockGetter level, BlockPos pos,
      SpawnPlacements.Type type, EntityType<?> entityType) {
    return false;
  }
}

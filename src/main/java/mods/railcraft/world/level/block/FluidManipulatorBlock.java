package mods.railcraft.world.level.block;

import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.FluidManipulatorBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public abstract class FluidManipulatorBlock<T extends FluidManipulatorBlockEntity>
    extends ManipulatorBlock<T> {

  protected FluidManipulatorBlock(Class<T> blockEntityType, Properties properties) {
    super(blockEntityType, properties);
  }

  @Override
  public ActionResultType use(BlockState blockState, World level,
      BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
    return LevelUtil.getBlockEntity(level, blockPos, FluidManipulatorBlockEntity.class)
        .filter(blockEntity -> blockEntity.use(player, hand))
        .map(__ -> ActionResultType.SUCCESS)
        .orElseGet(() -> super.use(blockState, level, blockPos, player, hand, rayTraceResult));
  }
}

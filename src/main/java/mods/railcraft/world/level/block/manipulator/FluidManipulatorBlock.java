package mods.railcraft.world.level.block.manipulator;

import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.manipulator.FluidManipulatorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public abstract class FluidManipulatorBlock<T extends FluidManipulatorBlockEntity>
    extends ManipulatorBlock<T> {

  protected FluidManipulatorBlock(Class<T> blockEntityType, Properties properties) {
    super(blockEntityType, properties);
  }

  @Override
  public InteractionResult use(BlockState blockState, Level level,
      BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
    return LevelUtil.getBlockEntity(level, blockPos, FluidManipulatorBlockEntity.class)
        .filter(blockEntity -> blockEntity.use(player, hand))
        .map(__ -> InteractionResult.SUCCESS)
        .orElseGet(() -> super.use(blockState, level, blockPos, player, hand, rayTraceResult));
  }
}

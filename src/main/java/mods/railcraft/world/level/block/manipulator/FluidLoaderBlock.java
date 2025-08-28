package mods.railcraft.world.level.block.manipulator;

import org.jetbrains.annotations.Nullable;
import com.mojang.serialization.MapCodec;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.manipulator.FluidLoaderBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.ManipulatorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FluidLoaderBlock extends FluidManipulatorBlock<FluidLoaderBlockEntity> {

  private static final MapCodec<FluidLoaderBlock> CODEC = simpleCodec(FluidLoaderBlock::new);

  public FluidLoaderBlock(Properties properties) {
    super(FluidLoaderBlockEntity.class, properties);
  }

  @Override
  protected MapCodec<? extends FluidManipulatorBlock<FluidLoaderBlockEntity>> codec() {
    return CODEC;
  }

  @Override
  public Direction getFacing(BlockState blockState) {
    return Direction.DOWN;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new FluidLoaderBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide() ? null
        : createTickerHelper(type, RailcraftBlockEntityTypes.FLUID_LOADER.get(),
            ManipulatorBlockEntity::serverTick);
  }
}

package mods.railcraft.world.level.block;

import mods.railcraft.world.level.block.entity.FluidLoaderBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;

public class FluidLoaderBlock extends FluidManipulatorBlock<FluidLoaderBlockEntity> {

  protected FluidLoaderBlock(Properties properties) {
    super(FluidLoaderBlockEntity.class, properties);
  }

  @Override
  public FluidLoaderBlockEntity createTileEntity(BlockState blockState, IBlockReader level) {
    return new FluidLoaderBlockEntity();
  }

  @Override
  public Direction getFacing(BlockState blockState) {
    return Direction.DOWN;
  }
}

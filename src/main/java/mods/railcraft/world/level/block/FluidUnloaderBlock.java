package mods.railcraft.world.level.block;

import mods.railcraft.world.level.block.entity.FluidUnloaderBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;

public class FluidUnloaderBlock extends ManipulatorBlock<FluidUnloaderBlockEntity> {

  protected FluidUnloaderBlock(Properties properties) {
    super(FluidUnloaderBlockEntity.class, properties);
  }

  @Override
  public FluidUnloaderBlockEntity createTileEntity(BlockState blockState, IBlockReader level) {
    return new FluidUnloaderBlockEntity();
  }

  @Override
  public Direction getFacing(BlockState blockState) {
    return Direction.UP;
  }
}

package mods.railcraft.world.level.block;

import mods.railcraft.world.level.block.entity.ItemUnloaderBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;

public class ItemUnloaderBlock extends ItemManipulatorBlock {

  protected ItemUnloaderBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(POWERED, false));
  }

  @Override
  public Direction getFacing(BlockState blockState) {
    return Direction.UP;
  }

  @Override
  public boolean hasTileEntity(BlockState blockState) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState blockState, IBlockReader level) {
    return new ItemUnloaderBlockEntity();
  }
}

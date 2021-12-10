package mods.railcraft.world.level.block.signal;

import net.minecraft.world.level.block.state.BlockState;

public class SelfAttachableSignalBoxBlock extends SignalBoxBlock {

  public SelfAttachableSignalBoxBlock(Properties properties) {
    super(properties);
  }

  @Override
  public boolean attachesTo(BlockState blockState, BlockState otherBlockState) {
    return otherBlockState.is(this) || super.attachesTo(blockState, otherBlockState);
  }
}

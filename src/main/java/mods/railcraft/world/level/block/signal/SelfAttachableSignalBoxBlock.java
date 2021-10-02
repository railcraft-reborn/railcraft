package mods.railcraft.world.level.block.signal;

import java.util.function.Supplier;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBoxBlockEntity;
import net.minecraft.block.BlockState;

public class SelfAttachableSignalBoxBlock extends SignalBoxBlock {

  public SelfAttachableSignalBoxBlock(
      Supplier<? extends AbstractSignalBoxBlockEntity> blockEntityFactory, Properties properties) {
    super(blockEntityFactory, properties);
  }

  @Override
  public boolean attachesTo(BlockState blockState, BlockState otherBlockState) {
    return otherBlockState.getBlock().is(this) || super.attachesTo(blockState, otherBlockState);
  }
}

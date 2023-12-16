package mods.railcraft.world.level.block.signal;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.state.BlockState;

public class SelfAttachableSignalBoxBlock extends SignalBoxBlock {

  private static final MapCodec<SelfAttachableSignalBoxBlock> CODEC =
      simpleCodec(SelfAttachableSignalBoxBlock::new);

  public SelfAttachableSignalBoxBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected MapCodec<? extends SignalBoxBlock> codec() {
    return CODEC;
  }

  @Override
  public boolean attachesTo(BlockState blockState, BlockState otherBlockState) {
    return otherBlockState.is(this) || super.attachesTo(blockState, otherBlockState);
  }
}

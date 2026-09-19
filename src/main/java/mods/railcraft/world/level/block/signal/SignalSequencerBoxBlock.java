package mods.railcraft.world.level.block.signal;

import mods.railcraft.world.level.block.entity.signal.SignalSequencerBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SignalSequencerBoxBlock extends SelfAttachableSignalBoxBlock implements EntityBlock {

  public SignalSequencerBoxBlock(Properties properties) {
    super(properties);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new SignalSequencerBoxBlockEntity(blockPos, blockState);
  }
}

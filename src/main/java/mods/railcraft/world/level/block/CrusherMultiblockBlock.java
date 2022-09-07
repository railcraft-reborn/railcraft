package mods.railcraft.world.level.block;

import org.jetbrains.annotations.Nullable;
import mods.railcraft.world.level.block.entity.CrusherBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CrusherMultiblockBlock extends MultiblockBlock {

  public CrusherMultiblockBlock(Properties properties) {
    super(properties);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new CrusherBlockEntity(pos, state);
  }
}

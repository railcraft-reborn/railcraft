package mods.railcraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CoalCokeBlock extends Block {

  private final int spread, flammability;

  public CoalCokeBlock(int flammability, int spread, Properties properties) {
    super(properties);
    this.flammability = flammability;
    this.spread = spread;
  }

  @Override
  public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos,
      Direction direction) {
    return true;
  }

  @Override
  public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos,
      Direction direction) {
    return spread;
  }

  @Override
  public int getFlammability(BlockState state, BlockGetter level, BlockPos pos,
      Direction direction) {
    return flammability;
  }
}

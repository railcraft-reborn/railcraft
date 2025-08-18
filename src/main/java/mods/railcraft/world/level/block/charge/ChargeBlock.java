package mods.railcraft.world.level.block.charge;

import mods.railcraft.api.charge.Charge;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ChargeBlock extends Block implements mods.railcraft.api.charge.ChargeBlock {

  public ChargeBlock(Properties properties) {
    super(properties);
  }

  protected boolean isSparking(BlockState state) {
    return true;
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    if (isSparking(state)) {
      Charge.zapEffectProvider().throwSparks(state, level, pos, random, 50);
    }
  }

  @Override
  protected void randomTick(BlockState state, ServerLevel level, BlockPos pos,
      RandomSource random) {
    super.randomTick(state, level, pos, random);
    level.updateNeighbourForOutputSignal(pos, this);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    super.tick(state, level, pos, random);
    this.registerNode(state, level, pos);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState,
      boolean isMoving) {
    super.onPlace(state, level, pos, oldState, isMoving);
    if (!state.is(oldState.getBlock())) {
      this.registerNode(state, (ServerLevel) level, pos);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onRemove(BlockState state, Level level, BlockPos pos, BlockState oldState,
      boolean isMoving) {
    super.onRemove(state, level, pos, oldState, isMoving);
    if (!state.is(oldState.getBlock())) {
      this.deregisterNode((ServerLevel) level, pos);
    }
  }

  @Override
  public boolean hasAnalogOutputSignal(BlockState state) {
    return true;
  }

  @Override
  public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
    if (level instanceof ServerLevel serverLevel) {
      return Charge.distribution.network(serverLevel).access(pos).getComparatorOutput();
    }
    return 0;
  }
}

package mods.railcraft.world.level.block;

import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.ChargeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

/**
 * Base implementation of {@link ChargeBlock}.
 *
 * @author Sm0keySa1m0n
 */
public abstract class BaseChargeBlock extends Block implements ChargeBlock {

  public BaseChargeBlock(Properties properties) {
    super(properties);
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
    Charge.zapEffectProvider().throwSparks(state, level, pos, rand, 50);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void tick(BlockState blockState, ServerLevel level, BlockPos blockPos, RandomSource random) {
    super.tick(blockState, level, blockPos, random);
    this.registerNode(blockState, level, blockPos);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState oldState,
      boolean moved) {
    super.onPlace(blockState, level, blockPos, oldState, moved);
    if (!blockState.is(oldState.getBlock())) {
      this.registerNode(blockState, (ServerLevel) level, blockPos);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState,
      boolean moved) {
    super.onRemove(blockState, level, blockPos, newState, moved);
    if (!blockState.is(newState.getBlock())) {
      this.deregisterNode((ServerLevel) level, blockPos);
    }
  }

  @Override
  public boolean hasAnalogOutputSignal(BlockState state) {
    return true;
  }

  @Override
  public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
    return Charge.distribution.network((ServerLevel) level).access(pos).getComparatorOutput();
  }
}

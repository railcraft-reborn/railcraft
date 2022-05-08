package mods.railcraft.world.level.block.track;

import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.ChargeBlock;
import mods.railcraft.api.track.TrackType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Created by CovertJaguar on 8/2/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class ElectricTrackBlock extends TrackBlock implements ChargeBlock {

  private static final Map<Charge, ChargeSpec> CHARGE_SPECS =
      ChargeSpec.make(Charge.distribution, ConnectType.TRACK, 0.01F);

  public ElectricTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }

  @Override
  public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
    Charge.effects().throwSparks(stateIn, worldIn, pos, rand, 75);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
    super.randomTick(state, level, pos, random);
    this.registerNode(state, level, pos);
  }

  @Override
  public void onPlace(BlockState state, Level level, BlockPos pos,
      BlockState oldState, boolean moved) {
    super.onPlace(state, level, pos, oldState, moved);
    if (!state.is(oldState.getBlock()) && level instanceof ServerLevel serverLevel) {
      this.registerNode(state, serverLevel, pos);
    }
  }

  @Override
  public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState,
      boolean moved) {
    super.onRemove(state, level, pos, newState, moved);
    if (!state.is(newState.getBlock()) && level instanceof ServerLevel serverLevel) {
      Charge.distribution.network(serverLevel).removeNode(pos);
    }
  }

  @Override
  public Map<Charge, ChargeSpec> getChargeSpecs(BlockState state, BlockGetter level,
      BlockPos pos) {
    return CHARGE_SPECS;
  }
}

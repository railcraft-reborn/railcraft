package mods.railcraft.world.level.block.track;

import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.IChargeBlock;
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
public class ElectricTrackBlock extends TrackBlock implements IChargeBlock {

  private static final Map<Charge, ChargeSpec> CHARGE_SPECS =
      ChargeSpec.make(Charge.distribution, ConnectType.TRACK, 0.01);

  public ElectricTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }

  @Override
  public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
    Charge.effects().throwSparks(stateIn, worldIn, pos, rand, 75);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
    super.randomTick(state, worldIn, pos, random);
    registerNode(state, worldIn, pos);
  }

  @Override
  public void onPlace(BlockState state, Level worldIn, BlockPos pos,
      BlockState oldState, boolean something) {
    super.onPlace(state, worldIn, pos, state, something);
    registerNode(state, worldIn, pos);
  }

  @Override
  public void onRemove(
      BlockState state, Level worldIn, BlockPos pos, BlockState newState,
      boolean p_196243_5_) {
    super.onRemove(state, worldIn, pos, state, p_196243_5_);
    Charge.distribution.network(worldIn).removeNode(pos);
  }

  @Override
  public Map<Charge, ChargeSpec> getChargeSpecs(BlockState state, BlockGetter world,
      BlockPos pos) {
    return CHARGE_SPECS;
  }
}

package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.track.CouplerTrackBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

public class CouplerTrackBlock extends PoweredOutfittedTrackBlock {

  public static final EnumProperty<CouplerTrackBlockEntity.Mode> MODE =
      EnumProperty.create("mode", CouplerTrackBlockEntity.Mode.class);

  public CouplerTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }
  
  @Override
  protected BlockState buildDefaultState(BlockState blockState) {
    return super.buildDefaultState(blockState)
        .setValue(MODE, CouplerTrackBlockEntity.Mode.COUPLER);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(MODE);
  }

  @Override
  public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player,
      InteractionHand hand, BlockHitResult rayTraceResult) {
    return level.getBlockEntity(pos, RailcraftBlockEntityTypes.COUPLER_TRACK.get())
        .map(track -> track.use(player, hand))
        .orElseGet(() -> super.use(blockState, level, pos, player, hand, rayTraceResult));
  }

  @Override
  public void onMinecartPass(BlockState blockState, Level level, BlockPos pos,
      AbstractMinecart cart) {
    super.onMinecartPass(blockState, level, pos, cart);
    level.getBlockEntity(pos, RailcraftBlockEntityTypes.COUPLER_TRACK.get())
        .ifPresent(lockingTrack -> lockingTrack.minecartPassed(cart));
  }

  @Override
  public int getPowerPropagation(BlockState blockState, Level level, BlockPos pos) {
    return getMode(blockState).getPowerPropagation();
  }

  public static CouplerTrackBlockEntity.Mode getMode(BlockState blockState) {
    return blockState.getValue(MODE);
  }

  public static void setMode(BlockState blockState, CouplerTrackBlockEntity.Mode mode) {
    blockState.setValue(MODE, mode);
  }
}

package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import javax.annotation.Nullable;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.track.LockingTrackBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class LockingTrackBlock extends PoweredOutfittedTrackBlock implements EntityBlock {

  public static final EnumProperty<LockingMode> LOCKING_MODE =
      EnumProperty.create("locking_mode", LockingMode.class);

  public LockingTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }

  @Override
  protected BlockState buildDefaultState(BlockState blockState) {
    return super.buildDefaultState(blockState)
        .setValue(LOCKING_MODE, LockingMode.LOCKDOWN);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(LOCKING_MODE);
  }

  @Override
  public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player,
      InteractionHand hand, BlockHitResult rayTraceResult) {
    return level.getBlockEntity(pos, RailcraftBlockEntityTypes.LOCKING_TRACK.get())
        .map(lockingTrack -> lockingTrack.use(player, hand))
        .orElseGet(() -> super.use(blockState, level, pos, player, hand, rayTraceResult));
  }

  @Override
  public void onMinecartPass(BlockState blockState, Level level, BlockPos pos,
      AbstractMinecart cart) {
    super.onMinecartPass(blockState, level, pos, cart);
    level.getBlockEntity(pos, RailcraftBlockEntityTypes.LOCKING_TRACK.get())
        .ifPresent(lockingTrack -> lockingTrack.minecartPassed(cart));
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new LockingTrackBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide() ? null
        : BaseEntityBlock.createTickerHelper(type,
            RailcraftBlockEntityTypes.LOCKING_TRACK.get(),
            LockingTrackBlockEntity::serverTick);
  }

  public static LockingMode getLockingMode(BlockState blockState) {
    return blockState.getValue(LOCKING_MODE);
  }
}

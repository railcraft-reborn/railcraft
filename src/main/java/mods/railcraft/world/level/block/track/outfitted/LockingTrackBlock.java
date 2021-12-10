package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import javax.annotation.Nullable;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.track.LockingTrackBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.BlockHitResult;

public class LockingTrackBlock extends OutfittedTrackBlock implements EntityBlock {

  public static final EnumProperty<LockingMode> LOCKING_MODE =
      EnumProperty.create("locking_mode", LockingMode.class);
  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

  public LockingTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(this.getShapeProperty(), RailShape.NORTH_SOUTH)
        .setValue(WATERLOGGED, false)
        .setValue(POWERED, false)
        .setValue(LOCKING_MODE, LockingMode.LOCKDOWN));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(POWERED, LOCKING_MODE);
  }

  @Override
  public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player,
      InteractionHand hand, BlockHitResult rayTraceResult) {
    return LevelUtil.getBlockEntity(level, pos, LockingTrackBlockEntity.class)
        .map(lockingTrack -> lockingTrack.use(player, hand))
        .orElseGet(() -> super.use(blockState, level, pos, player, hand, rayTraceResult));
  }

  @Override
  public void onMinecartPass(BlockState blockState, Level level, BlockPos pos,
      AbstractMinecart cart) {
    super.onMinecartPass(blockState, level, pos, cart);
    LevelUtil.getBlockEntity(level, pos, LockingTrackBlockEntity.class)
        .ifPresent(lockingTrack -> lockingTrack.minecartPassed(cart));
  }

  @Override
  public void neighborChanged(BlockState blockState, Level level, BlockPos pos,
      Block neighborBlock, BlockPos neighborPos, boolean moved) {
    if (!level.isClientSide()) {
      boolean powered = blockState.getValue(POWERED);
      boolean neighborSignal = level.hasNeighborSignal(pos);
      if (powered != neighborSignal) {
        level.setBlockAndUpdate(pos, blockState.setValue(POWERED, neighborSignal));
      }
    }
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
        : createTickerHelper(type, RailcraftBlockEntityTypes.LOCKING_TRACK.get(),
            LockingTrackBlockEntity::serverTick);
  }

  @SuppressWarnings("unchecked")
  @Nullable
  protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
      BlockEntityType<A> type, BlockEntityType<E> expectedType,
      BlockEntityTicker<? super E> ticker) {
    return expectedType == type ? (BlockEntityTicker<A>) ticker : null;
  }

  public static LockingMode getLockingMode(BlockState blockState) {
    return blockState.getValue(LOCKING_MODE);
  }

  public static boolean isPowered(BlockState blockState) {
    return blockState.getValue(POWERED);
  }
}

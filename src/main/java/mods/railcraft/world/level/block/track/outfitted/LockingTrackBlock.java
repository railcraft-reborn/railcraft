package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.track.LockingTrackBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class LockingTrackBlock extends OutfittedTrackBlock {

  public static final EnumProperty<LockingMode> LOCKING_MODE =
      EnumProperty.create("locking_mode", LockingMode.class);
  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

  public LockingTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(this.getShapeProperty(), RailShape.NORTH_SOUTH)
        .setValue(POWERED, false)
        .setValue(LOCKING_MODE, LockingMode.LOCKDOWN));
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(POWERED, LOCKING_MODE);
  }

  @Override
  public ActionResultType use(BlockState blockState, World level, BlockPos pos, PlayerEntity player,
      Hand hand, BlockRayTraceResult rayTraceResult) {
    return LevelUtil.getBlockEntity(level, pos, LockingTrackBlockEntity.class)
        .map(lockingTrack -> lockingTrack.use(player, hand))
        .orElseGet(() -> super.use(blockState, level, pos, player, hand, rayTraceResult));
  }

  @Override
  public void onMinecartPass(BlockState blockState, World level, BlockPos pos,
      AbstractMinecartEntity cart) {
    super.onMinecartPass(blockState, level, pos, cart);
    LevelUtil.getBlockEntity(level, pos, LockingTrackBlockEntity.class)
        .ifPresent(lockingTrack -> lockingTrack.minecartPassed(cart));
  }

  @Override
  public void neighborChanged(BlockState blockState, World level, BlockPos pos,
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
  public boolean hasTileEntity(BlockState blockState) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState blockState, IBlockReader level) {
    return new LockingTrackBlockEntity(getLockingMode(blockState));
  }

  public static LockingMode getLockingMode(BlockState blockState) {
    return blockState.getValue(LOCKING_MODE);
  }

  public static boolean isPowered(BlockState blockState) {
    return blockState.getValue(POWERED);
  }
}

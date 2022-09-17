package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.track.TurnoutTrackBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.RailShape;

public class TurnoutTrackBlock extends SwitchTrackBlock implements EntityBlock {

  public static final BooleanProperty MIRRORED = BooleanProperty.create("mirrored");

  public TurnoutTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }

  @Override
  protected BlockState buildDefaultState(BlockState blockState) {
    return super.buildDefaultState(blockState)
        .setValue(MIRRORED, false);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(MIRRORED);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    BlockState blockState = super.getStateForPlacement(context);
    BlockPos blockPos = context.getClickedPos();
    return blockState.setValue(MIRRORED,
        this.determineMirrored(context.getLevel(), blockPos, getFacing(blockState)));
  }

  private boolean determineMirrored(Level level, BlockPos blockPos, Direction facing) {
    return level
        .getBlockState(blockPos.relative(facing.getCounterClockWise()))
        .is(BlockTags.RAILS);
  }

  @Override
  public void neighborChanged(BlockState blockState, Level level, BlockPos pos, Block neighborBlock,
      BlockPos neighborPos, boolean moved) {
    level.setBlockAndUpdate(pos,
        blockState.setValue(MIRRORED, this.determineMirrored(level, pos, getFacing(blockState))));
    super.neighborChanged(blockState, level, pos, neighborBlock, neighborPos, moved);
  }

  @Override
  protected boolean crowbarWhack(BlockState blockState, Level level, BlockPos pos,
      Player player, InteractionHand hand, ItemStack itemStack) {
    level.setBlockAndUpdate(pos, blockState.cycle(REVERSED).cycle(MIRRORED));
    return true;
  }

  @Override
  public RailShape getRailDirection(BlockState blockState, BlockGetter world, BlockPos pos,
      @Nullable AbstractMinecart cart) {
    final boolean mirrored = isMirrored(blockState);
    if (isSwitched(blockState)) {
      switch (getFacing(blockState)) {
        case NORTH:
          return mirrored ? RailShape.SOUTH_WEST : RailShape.SOUTH_EAST;
        case SOUTH:
          return mirrored ? RailShape.NORTH_EAST : RailShape.NORTH_WEST;
        case EAST:
          return mirrored ? RailShape.NORTH_WEST : RailShape.SOUTH_WEST;
        case WEST:
          return mirrored ? RailShape.SOUTH_EAST : RailShape.NORTH_EAST;
        default:
          throw new IllegalStateException("Invalid facing direction.");
      }
    }
    return getRailShapeRaw(blockState);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new TurnoutTrackBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide() ? null
        : BaseEntityBlock.createTickerHelper(type,
            RailcraftBlockEntityTypes.TURNOUT_TRACK.get(),
            TurnoutTrackBlockEntity::serverTick);
  }

  public static boolean isMirrored(BlockState blockState) {
    return blockState.getValue(MIRRORED);
  }
}

package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import javax.annotation.Nullable;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.track.WyeTrackBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

public class WyeTrackBlock extends SwitchTrackBlock implements EntityBlock {

  public WyeTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }

  @Override
  public RailShape getRailDirection(BlockState blockState, BlockGetter world, BlockPos pos,
      @Nullable AbstractMinecart cart) {
    final boolean switched = isSwitched(blockState);
    switch (getFacing(blockState)) {
      case NORTH:
        return switched ? RailShape.SOUTH_WEST : RailShape.SOUTH_EAST;
      case SOUTH:
        return switched ? RailShape.NORTH_EAST : RailShape.NORTH_WEST;
      case EAST:
        return switched ? RailShape.NORTH_WEST : RailShape.SOUTH_WEST;
      case WEST:
        return switched ? RailShape.SOUTH_EAST : RailShape.NORTH_EAST;
      default:
        throw new IllegalStateException("Invalid facing direction.");
    }
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new WyeTrackBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide() ? null
        : createTickerHelper(type, RailcraftBlockEntityTypes.WYE_TRACK.get(),
            WyeTrackBlockEntity::serverTick);
  }

  @SuppressWarnings("unchecked")
  @Nullable
  protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
      BlockEntityType<A> type, BlockEntityType<E> expectedType,
      BlockEntityTicker<? super E> ticker) {
    return expectedType == type ? (BlockEntityTicker<A>) ticker : null;
  }
}

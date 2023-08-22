package mods.railcraft.world.level.block.track;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.ChargeBlock;
import mods.railcraft.api.item.SpikeMaulTarget;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.api.track.TypedTrack;
import mods.railcraft.world.level.block.track.behaivor.TrackSupportTools;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TrackBlock extends BaseRailBlock implements TypedTrack, ChargeBlock, SpikeMaulTarget {

  public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE;

  private static final Map<Charge, Spec> CHARGE_SPECS =
      Spec.make(Charge.distribution, ConnectType.TRACK, 0.01F);

  private final Supplier<? extends TrackType> trackType;

  public TrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(false, properties);
    this.trackType = trackType;
    this.registerDefaultState(this.buildDefaultState(this.stateDefinition.any()));
  }

  @Override
  public TrackType getTrackType() {
    return this.trackType.get();
  }

  @Override
  public Property<RailShape> getShapeProperty() {
    return SHAPE;
  }

  protected BlockState buildDefaultState(BlockState blockState) {
    return blockState
        .setValue(this.getShapeProperty(), RailShape.NORTH_SOUTH)
        .setValue(WATERLOGGED, false);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(this.getShapeProperty(), WATERLOGGED);
  }

  @Override
  public List<? extends Supplier<? extends Block>> getSpikeMaulVariants() {
    return this.trackType.get().getSpikeMaulVariants();
  }

  @SuppressWarnings("deprecation")
  @Override
  public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    super.tick(state, level, pos, random);
    if (this.getTrackType().isElectric()) {
      this.registerNode(state, level, pos);
    }
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
    if (this.getTrackType().isElectric()) {
      Charge.zapEffectProvider().throwSparks(state, level, pos, rand, 75);
    }
  }

  @Override
  public void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState oldBlockState,
      boolean moved) {
    super.onPlace(blockState, level, pos, oldBlockState, moved);
    if (!blockState.is(oldBlockState.getBlock())) {
      if (!TrackSupportTools.isSupported(level, pos, this.getTrackType().getMaxSupportDistance())) {
        level.destroyBlock(pos, true);
        return;
      }

      if (this.getTrackType().isElectric()) {
        this.registerNode(blockState, (ServerLevel) level, pos);
      }
    }
  }

  @Override
  public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newBlockState,
      boolean moved) {
    super.onRemove(blockState, level, pos, newBlockState, moved);
    if (this.getTrackType().isElectric() && !blockState.is(newBlockState.getBlock())) {
      this.deregisterNode((ServerLevel) level, pos);
    }
  }

  @Override
  public Map<Charge, Spec> getChargeSpecs(BlockState state, ServerLevel level, BlockPos pos) {
    return this.getTrackType().isElectric() ? CHARGE_SPECS : Collections.emptyMap();
  }

  public int getMaxSupportedDistance(Level level, BlockPos pos) {
    return this.getTrackType().getMaxSupportDistance();
  }

  protected boolean isRailValid(BlockState state, Level level, BlockPos pos,
      int maxSupportedDistance) {
    var railShape = TrackUtil.getRailShapeRaw(state);
    if (!TrackSupportTools.isSupported(level, pos, maxSupportedDistance)) {
      return false;
    }
    if (maxSupportedDistance == 0) {
      if (railShape == RailShape.ASCENDING_EAST && !canSupportRigidBlock(level, pos.east())) {
        return false;
      }
      if (railShape == RailShape.ASCENDING_WEST && !canSupportRigidBlock(level, pos.west())) {
        return false;
      }
      if (railShape == RailShape.ASCENDING_NORTH && !canSupportRigidBlock(level, pos.north())) {
        return false;
      }
      if (railShape == RailShape.ASCENDING_SOUTH && !canSupportRigidBlock(level, pos.south())) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void neighborChanged(BlockState blockState, Level level, BlockPos pos,
      Block neighborBlock, BlockPos neighborPos, boolean moved) {
    if (level.isClientSide()) {
      return;
    }
    if (!this.isRailValid(blockState, level, pos, this.getMaxSupportedDistance(level, pos))) {
      level.destroyBlock(pos, true);
      return;
    }
    this.updateState(blockState, level, pos, neighborBlock);
    TrackUtil.traverseConnectedTracks(level, pos, (l, p) -> {
      var state = l.getBlockState(p);
      var block = state.getBlock();
      if (!BaseRailBlock.isRail(state)) {
        return false;
      }
      if (block instanceof TrackBlock track) {
        int maxSupportedDistance = track.getMaxSupportedDistance(l, p);
        if (maxSupportedDistance <= 0 || TrackSupportTools.isSupportedDirectly(l, p)) {
          return false;
        }
        if (!track.isRailValid(state, l, p, maxSupportedDistance)) {
          l.destroyBlock(p, true);
          return false;
        }
      }
      return true;
    });
  }

  @Override
  public void onMinecartPass(BlockState state, Level level, BlockPos pos,
      AbstractMinecart cart) {
    this.getTrackType().getEventHandler().minecartPass(level, cart, pos);
  }

  @Override
  public RailShape getRailDirection(BlockState state, BlockGetter blockGetter, BlockPos pos,
      @Nullable AbstractMinecart cart) {
    return this.getTrackType().getEventHandler().getRailShapeOverride(blockGetter, pos, state, cart)
        .orElseGet(() -> super.getRailDirection(state, blockGetter, pos, cart));
  }

  @Override
  public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
    if (level instanceof ServerLevel serverLevel) {
      this.getTrackType().getEventHandler().entityInside(serverLevel, pos, state, entity);
    }
  }

  @Override
  public float getRailMaxSpeed(BlockState state, Level level, BlockPos pos,
      AbstractMinecart cart) {
    return (float) this.getTrackType().getEventHandler().getMaxSpeed(level, cart, pos);
  }

  @Override
  public boolean canMakeSlopes(BlockState state, BlockGetter blockGetter, BlockPos pos) {
    return TrackSupportTools.isSupportedDirectly(blockGetter, pos);
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
    return !BaseRailBlock.isRail(level.getBlockState(pos.above()))
        && !BaseRailBlock.isRail(level.getBlockState(pos.below()))
        && TrackSupportTools.isSupported(level, pos, this.getTrackType().getMaxSupportDistance());
  }

  @Override
  public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos blockPos,
      CollisionContext context) {
    RailShape railShape = blockState.is(this) ? blockState.getValue(this.getShapeProperty()) : null;
    return railShape != null && railShape.isAscending() ? HALF_BLOCK_AABB : FLAT_AABB;
  }

  /**
   * @see net.minecraft.world.level.block.RailBlock#rotate(BlockState, Rotation)
   */
  @Override
  public BlockState rotate(BlockState state, Rotation rot) {
    switch (rot) {
      case CLOCKWISE_180:
        switch (state.getValue(getShapeProperty())) {
          case ASCENDING_EAST:
            return state.setValue(getShapeProperty(), RailShape.ASCENDING_WEST);
          case ASCENDING_WEST:
            return state.setValue(getShapeProperty(), RailShape.ASCENDING_EAST);
          case ASCENDING_NORTH:
            return state.setValue(getShapeProperty(), RailShape.ASCENDING_SOUTH);
          case ASCENDING_SOUTH:
            return state.setValue(getShapeProperty(), RailShape.ASCENDING_NORTH);
          case SOUTH_EAST:
            return state.setValue(getShapeProperty(), RailShape.NORTH_WEST);
          case SOUTH_WEST:
            return state.setValue(getShapeProperty(), RailShape.NORTH_EAST);
          case NORTH_WEST:
            return state.setValue(getShapeProperty(), RailShape.SOUTH_EAST);
          case NORTH_EAST:
            return state.setValue(getShapeProperty(), RailShape.SOUTH_WEST);
          case NORTH_SOUTH: // Forge fix: MC-196102
          case EAST_WEST:
            return state;
        }
      case COUNTERCLOCKWISE_90:
        switch (state.getValue(getShapeProperty())) {
          case ASCENDING_EAST:
            return state.setValue(getShapeProperty(), RailShape.ASCENDING_NORTH);
          case ASCENDING_WEST:
            return state.setValue(getShapeProperty(), RailShape.ASCENDING_SOUTH);
          case ASCENDING_NORTH:
            return state.setValue(getShapeProperty(), RailShape.ASCENDING_WEST);
          case ASCENDING_SOUTH:
            return state.setValue(getShapeProperty(), RailShape.ASCENDING_EAST);
          case SOUTH_EAST:
            return state.setValue(getShapeProperty(), RailShape.NORTH_EAST);
          case SOUTH_WEST:
            return state.setValue(getShapeProperty(), RailShape.SOUTH_EAST);
          case NORTH_WEST:
            return state.setValue(getShapeProperty(), RailShape.SOUTH_WEST);
          case NORTH_EAST:
            return state.setValue(getShapeProperty(), RailShape.NORTH_WEST);
          case NORTH_SOUTH:
            return state.setValue(getShapeProperty(), RailShape.EAST_WEST);
          case EAST_WEST:
            return state.setValue(getShapeProperty(), RailShape.NORTH_SOUTH);
        }
      case CLOCKWISE_90:
        switch (state.getValue(getShapeProperty())) {
          case ASCENDING_EAST:
            return state.setValue(getShapeProperty(), RailShape.ASCENDING_SOUTH);
          case ASCENDING_WEST:
            return state.setValue(getShapeProperty(), RailShape.ASCENDING_NORTH);
          case ASCENDING_NORTH:
            return state.setValue(getShapeProperty(), RailShape.ASCENDING_EAST);
          case ASCENDING_SOUTH:
            return state.setValue(getShapeProperty(), RailShape.ASCENDING_WEST);
          case SOUTH_EAST:
            return state.setValue(getShapeProperty(), RailShape.SOUTH_WEST);
          case SOUTH_WEST:
            return state.setValue(getShapeProperty(), RailShape.NORTH_WEST);
          case NORTH_WEST:
            return state.setValue(getShapeProperty(), RailShape.NORTH_EAST);
          case NORTH_EAST:
            return state.setValue(getShapeProperty(), RailShape.SOUTH_EAST);
          case NORTH_SOUTH:
            return state.setValue(getShapeProperty(), RailShape.EAST_WEST);
          case EAST_WEST:
            return state.setValue(getShapeProperty(), RailShape.NORTH_SOUTH);
        }
      default:
        return state;
    }
  }

  /**
   * @see net.minecraft.world.level.block.RailBlock#mirror(BlockState, Mirror)
   */
  @SuppressWarnings("deprecation")
  @Override
  public BlockState mirror(BlockState state, Mirror mirror) {
    Property<RailShape> shape = getShapeProperty();
    RailShape railshape = state.getValue(shape);

    switch (mirror) {
      case LEFT_RIGHT:
        switch (railshape) {
          case ASCENDING_NORTH:
            return state.setValue(shape, RailShape.ASCENDING_SOUTH);
          case ASCENDING_SOUTH:
            return state.setValue(shape, RailShape.ASCENDING_NORTH);
          case SOUTH_EAST:
            return state.setValue(shape, RailShape.NORTH_EAST);
          case SOUTH_WEST:
            return state.setValue(shape, RailShape.NORTH_WEST);
          case NORTH_WEST:
            return state.setValue(shape, RailShape.SOUTH_WEST);
          case NORTH_EAST:
            return state.setValue(shape, RailShape.SOUTH_EAST);
          default:
            return super.mirror(state, mirror);
        }
      case FRONT_BACK:
        switch (railshape) {
          case ASCENDING_EAST:
            return state.setValue(shape, RailShape.ASCENDING_WEST);
          case ASCENDING_WEST:
            return state.setValue(shape, RailShape.ASCENDING_EAST);
          case ASCENDING_NORTH:
          case ASCENDING_SOUTH:
          default:
            break;
          case SOUTH_EAST:
            return state.setValue(shape, RailShape.SOUTH_WEST);
          case SOUTH_WEST:
            return state.setValue(shape, RailShape.SOUTH_EAST);
          case NORTH_WEST:
            return state.setValue(shape, RailShape.NORTH_EAST);
          case NORTH_EAST:
            return state.setValue(shape, RailShape.NORTH_WEST);
        }
      default:
        break;
    }

    return super.mirror(state, mirror);
  }

  @SuppressWarnings("deprecation")
  public static RailShape getRailShapeRaw(BlockState state) {
    return state.getValue(((BaseRailBlock) state.getBlock()).getShapeProperty());
  }
}

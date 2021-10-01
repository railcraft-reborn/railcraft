package mods.railcraft.world.level.block.track;

import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.IChargeBlock;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.api.track.TypedTrack;
import mods.railcraft.util.TrackTools;
import mods.railcraft.world.level.block.RailcraftToolTypes;
import mods.railcraft.world.level.block.track.behaivor.TrackSupportTools;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySpawnPlacementRegistry.PlacementType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

/**
 * Created by CovertJaguar on 8/29/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class TrackBlock extends AbstractRailBlock implements TypedTrack, IChargeBlock {

  private static final Map<Charge, ChargeSpec> CHARGE_SPECS =
      ChargeSpec.make(Charge.distribution, ConnectType.TRACK, 0.01);

  private final Supplier<? extends TrackType> trackType;

  public TrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(false, properties.strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
        .sound(SoundType.METAL)
        .harvestTool(RailcraftToolTypes.CROWBAR));
    this.trackType = trackType;
    this.registerDefaultState(
        this.stateDefinition.any().setValue(this.getShapeProperty(), RailShape.NORTH_SOUTH));
  }

  @Override
  public Property<RailShape> getShapeProperty() {
    return BlockStateProperties.RAIL_SHAPE;
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(this.getShapeProperty());
  }

  @Override
  public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player,
      boolean willHarvest, FluidState fluid) {
    Charge.distribution.network(world).removeNode(pos);
    return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
  }

  @Override
  public void destroy(IWorld world, BlockPos pos, BlockState state) {
    super.destroy(world, pos, state);
    Charge.distribution.network(world).removeNode(pos);
  }

  @Override
  public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
    if (this.getTrackType().isElectric())
      Charge.effects().throwSparks(stateIn, worldIn, pos, rand, 75);
  }

  @Override
  public void onPlace(BlockState state, World level, BlockPos pos, BlockState oldBlockState,
      boolean moved) {
    super.onPlace(state, level, pos, oldBlockState, moved);
    if (this.getTrackType().isElectric())
      this.registerNode(state, level, pos);
  }

  @Override
  public Map<Charge, ChargeSpec> getChargeSpecs(
      BlockState state, IBlockReader level, BlockPos pos) {
    return this.getTrackType().isElectric() ? CHARGE_SPECS : Collections.emptyMap();
  }

  public void breakRail(World level, BlockPos pos) {
    if (!level.isClientSide())
      level.destroyBlock(pos, true);
  }

  @Override
  public void setPlacedBy(World world, BlockPos pos, BlockState state,
      @Nullable LivingEntity placer, ItemStack stack) {
    super.setPlacedBy(world, pos, state, placer, stack);
    if (!TrackSupportTools.isSupported(world, pos,
        this.getTrackType().getMaxSupportDistance()))
      breakRail(world, pos);
  }

  public int getMaxSupportedDistance(World worldIn, BlockPos pos) {
    return this.getTrackType().getMaxSupportDistance();
  }

  protected boolean isRailValid(BlockState state, World world, BlockPos pos,
      int maxSupportedDistance) {
    RailShape dir = TrackTools.getTrackDirectionRaw(state);
    if (!TrackSupportTools.isSupported(world, pos, maxSupportedDistance))
      return false;
    if (maxSupportedDistance == 0) {
      if (dir == RailShape.ASCENDING_EAST && !canSupportRigidBlock(world, pos.east()))
        return false;
      if (dir == RailShape.ASCENDING_WEST && !canSupportRigidBlock(world, pos.west()))
        return false;
      if (dir == RailShape.ASCENDING_NORTH && !canSupportRigidBlock(world, pos.north()))
        return false;
      if (dir == RailShape.ASCENDING_SOUTH && !canSupportRigidBlock(world, pos.south()))
        return false;
    }
    return true;
  }

  @Override
  public void neighborChanged(BlockState state, World worldIn, BlockPos pos,
      Block neighborBlock, BlockPos neighborPos, boolean p_220069_6_) {
    super.neighborChanged(state, worldIn, pos, neighborBlock, neighborPos, dynamicShape);
    if (worldIn.isClientSide())
      return;
    if (!isRailValid(state, worldIn, pos, getMaxSupportedDistance(worldIn, pos))) {
      breakRail(worldIn, pos);
      return;
    }
    updateState(state, worldIn, pos, neighborBlock);
    TrackTools.traverseConnectedTracks(worldIn, pos, (w, p) -> {
      BlockState s = w.getBlockState(p);
      Block b = s.getBlock();
      if (!TrackTools.isRail(b))
        return false;
      if (b instanceof TrackBlock) {
        TrackBlock track = (TrackBlock) b;
        int maxSupportedDistance = track.getMaxSupportedDistance(w, p);
        if (maxSupportedDistance <= 0 || TrackSupportTools.isSupportedDirectly(w, p))
          return false;
        if (!track.isRailValid(s, w, p, maxSupportedDistance)) {
          breakRail(w, p);
          return false;
        }
      }
      return true;
    });
  }

  @Override
  public void onMinecartPass(BlockState state, World world, BlockPos pos,
      AbstractMinecartEntity cart) {
    this.getTrackType().getEventHandler().minecartPass(world, cart, pos);
  }

  @Override
  public RailShape getRailDirection(BlockState state, IBlockReader world, BlockPos pos,
      @Nullable AbstractMinecartEntity cart) {
    RailShape shape =
        this.getTrackType().getEventHandler().getRailShapeOverride(world, pos, state, cart);
    return shape == null ? super.getRailDirection(state, world, pos, cart) : shape;
  }

  @Override
  public void entityInside(BlockState state, World world, BlockPos pos, Entity entity) {
    if (world.isClientSide())
      return;
    this.getTrackType().getEventHandler().entityInside(world, pos, state, entity);
  }

  @Override
  public float getRailMaxSpeed(BlockState state, World world, BlockPos pos,
      AbstractMinecartEntity cart) {
    return (float) this.getTrackType().getEventHandler().getMaxSpeed(world, cart, pos);
  }

  @Override
  public boolean canMakeSlopes(BlockState state, IBlockReader world, BlockPos pos) {
    return TrackSupportTools.isSupportedDirectly(world, pos);
  }

  @Override
  public boolean canSurvive(BlockState state, IWorldReader level, BlockPos pos) {
    return !AbstractRailBlock.isRail(level.getBlockState(pos.above()))
        && !AbstractRailBlock.isRail(level.getBlockState(pos.below()))
        && TrackSupportTools.isSupported(level, pos, this.getTrackType().getMaxSupportDistance());
  }

  @Override
  public boolean canBeReplacedByLeaves(BlockState state, IWorldReader world, BlockPos pos) {
    return false;
  }

  @Override
  public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos,
      PlacementType type, EntityType<?> entityType) {
    return false;
  }

  @Override
  public boolean isConduitFrame(BlockState state, IWorldReader world, BlockPos pos,
      BlockPos conduit) {
    return false;
  }

  @Override
  public VoxelShape getShape(BlockState blockState, IBlockReader level, BlockPos blockPos,
      ISelectionContext context) {
    RailShape railShape = blockState.is(this) ? blockState.getValue(this.getShapeProperty()) : null;
    return railShape != null && railShape.isAscending() ? HALF_BLOCK_AABB : FLAT_AABB;
  }

  /**
   * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable,
   * returns the passed blockstate.
   * 
   * @see net.minecraft.block.RailBlock#rotate() Railblock's rotate
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
   * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns
   * the passed blockstate.
   * 
   * @see net.minecraft.block.RailBlock#mirror() Railblock's rotate
   */
  @Override
  @SuppressWarnings({"incomplete-switch", "deprecation"})
  public BlockState mirror(BlockState state, Mirror mirrorIn) {
    Property<RailShape> shape = getShapeProperty();
    RailShape railshape = state.getValue(shape);

    switch (mirrorIn) {
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
            return super.mirror(state, mirrorIn);
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
    }

    return super.mirror(state, mirrorIn);
  }

  @Override
  public TrackType getTrackType() {
    return this.trackType.get();
  }

  @SuppressWarnings("deprecation")
  public static RailShape getRailShapeRaw(BlockState state) {
    return AbstractRailBlock.isRail(state)
        ? state.getValue(((AbstractRailBlock) state.getBlock()).getShapeProperty())
        : RailShape.NORTH_SOUTH;
  }
}

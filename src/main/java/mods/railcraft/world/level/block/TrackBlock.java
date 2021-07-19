package mods.railcraft.world.level.block;

import java.util.function.Supplier;
import javax.annotation.Nullable;
import mods.railcraft.api.tracks.IBlockTrack;
import mods.railcraft.api.tracks.TrackToolsAPI;
import mods.railcraft.api.tracks.TrackType;
import mods.railcraft.util.TrackTools;
import mods.railcraft.world.level.block.track.TrackConstants;
import mods.railcraft.world.level.block.track.behaivor.TrackSupportTools;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RailBlock;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySpawnPlacementRegistry.PlacementType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

/**
 * Created by CovertJaguar on 8/29/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class TrackBlock extends AbstractRailBlock implements IBlockTrack {

  private final Supplier<? extends TrackType> trackType;

  public TrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(false, properties);
    this.trackType = trackType;
    properties.strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE);
    properties.sound(SoundType.METAL);
    this.registerDefaultState(
        this.stateDefinition.any().setValue(this.getShapeProperty(), RailShape.NORTH_SOUTH));
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(this.getShapeProperty());
  }

  public void breakRail(World world, BlockPos pos) {
    if (!world.isClientSide())
      world.destroyBlock(pos, true);
  }

  @Override
  public Property<RailShape> getShapeProperty() {
    return RailBlock.SHAPE;
  }

  @Override
  public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
    return state;
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
    boolean valid = true;
    RailShape dir = TrackTools.getTrackDirectionRaw(state);
    if (!TrackSupportTools.isSupported(world, pos, maxSupportedDistance))
      valid = false;
    if (maxSupportedDistance == 0) {
      if (dir == RailShape.ASCENDING_EAST && !canSupportRigidBlock(world, pos.east()))
        valid = false;
      else if (dir == RailShape.ASCENDING_WEST && !canSupportRigidBlock(world, pos.west()))
        valid = false;
      else if (dir == RailShape.ASCENDING_NORTH && !canSupportRigidBlock(world, pos.north()))
        valid = false;
      else if (dir == RailShape.ASCENDING_SOUTH && !canSupportRigidBlock(world, pos.south()))
        valid = false;
    }
    return valid;
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
    this.getTrackType().getEventHandler().onMinecartPass(world, cart, pos, null);
  }

  @Override
  public RailShape getRailDirection(BlockState state, IBlockReader world, BlockPos pos,
      @Nullable AbstractMinecartEntity cart) {
    RailShape shape = this.getTrackType().getEventHandler()
        .getRailDirectionOverride(world, pos, state, cart);
    if (shape != null)
      return shape;
    return super.getRailDirection(state, world, pos, cart);
  }

  @Override
  public void entityInside(BlockState state, World world, BlockPos pos, Entity entity) {
    if (world.isClientSide())
      return;
    this.getTrackType().getEventHandler().onEntityCollision(world, pos, state, entity);
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
  public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
    if (TrackToolsAPI.isRailBlockAt(world, pos.above())
        || TrackToolsAPI.isRailBlockAt(world, pos.below()))
      return false;
    return TrackSupportTools.isSupported(world, pos, this.getTrackType().getMaxSupportDistance());
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

  /**
   * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable,
   * returns the passed blockstate.
   */
  @Override
  public BlockState rotate(BlockState state, Rotation rot) {
    switch (rot) {
      case CLOCKWISE_180:
        switch (state.getValue(getShapeProperty())) {
          case ASCENDING_EAST:
            return state.setValue(getShapeProperty(),
                RailShape.ASCENDING_WEST);
          case ASCENDING_WEST:
            return state.setValue(getShapeProperty(),
                RailShape.ASCENDING_EAST);
          case ASCENDING_NORTH:
            return state.setValue(getShapeProperty(),
                RailShape.ASCENDING_SOUTH);
          case ASCENDING_SOUTH:
            return state.setValue(getShapeProperty(),
                RailShape.ASCENDING_NORTH);
          case SOUTH_EAST:
            return state.setValue(getShapeProperty(),
                RailShape.NORTH_WEST);
          case SOUTH_WEST:
            return state.setValue(getShapeProperty(),
                RailShape.NORTH_EAST);
          case NORTH_WEST:
            return state.setValue(getShapeProperty(),
                RailShape.SOUTH_EAST);
          case NORTH_EAST:
            return state.setValue(getShapeProperty(),
                RailShape.SOUTH_WEST);
          default:
            break;
        }

      case COUNTERCLOCKWISE_90:

        switch (state.getValue(getShapeProperty())) {
          case ASCENDING_EAST:
            return state.setValue(getShapeProperty(),
                RailShape.ASCENDING_NORTH);
          case ASCENDING_WEST:
            return state.setValue(getShapeProperty(),
                RailShape.ASCENDING_SOUTH);
          case ASCENDING_NORTH:
            return state.setValue(getShapeProperty(),
                RailShape.ASCENDING_WEST);
          case ASCENDING_SOUTH:
            return state.setValue(getShapeProperty(),
                RailShape.ASCENDING_EAST);
          case SOUTH_EAST:
            return state.setValue(getShapeProperty(),
                RailShape.NORTH_EAST);
          case SOUTH_WEST:
            return state.setValue(getShapeProperty(),
                RailShape.SOUTH_EAST);
          case NORTH_WEST:
            return state.setValue(getShapeProperty(),
                RailShape.SOUTH_WEST);
          case NORTH_EAST:
            return state.setValue(getShapeProperty(),
                RailShape.NORTH_WEST);
          case NORTH_SOUTH:
            return state.setValue(getShapeProperty(),
                RailShape.EAST_WEST);
          case EAST_WEST:
            return state.setValue(getShapeProperty(),
                RailShape.NORTH_SOUTH);
        }

      case CLOCKWISE_90:

        switch (state.getValue(getShapeProperty())) {
          case ASCENDING_EAST:
            return state.setValue(getShapeProperty(),
                RailShape.ASCENDING_SOUTH);
          case ASCENDING_WEST:
            return state.setValue(getShapeProperty(),
                RailShape.ASCENDING_NORTH);
          case ASCENDING_NORTH:
            return state.setValue(getShapeProperty(),
                RailShape.ASCENDING_EAST);
          case ASCENDING_SOUTH:
            return state.setValue(getShapeProperty(),
                RailShape.ASCENDING_WEST);
          case SOUTH_EAST:
            return state.setValue(getShapeProperty(),
                RailShape.SOUTH_WEST);
          case SOUTH_WEST:
            return state.setValue(getShapeProperty(),
                RailShape.NORTH_WEST);
          case NORTH_WEST:
            return state.setValue(getShapeProperty(),
                RailShape.NORTH_EAST);
          case NORTH_EAST:
            return state.setValue(getShapeProperty(),
                RailShape.SOUTH_EAST);
          case NORTH_SOUTH:
            return state.setValue(getShapeProperty(),
                RailShape.EAST_WEST);
          case EAST_WEST:
            return state.setValue(getShapeProperty(),
                RailShape.NORTH_SOUTH);
        }

      default:
        return state;
    }
  }

  /**
   * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns
   * the passed blockstate.
   */
  @Override
  @SuppressWarnings({"incomplete-switch", "deprecation"})
  public BlockState mirror(BlockState state, Mirror mirrorIn) {
    RailShape trackShape = state.getValue(getShapeProperty());

    switch (mirrorIn) {
      case LEFT_RIGHT:

        switch (trackShape) {
          case ASCENDING_NORTH:
            return state.setValue(getShapeProperty(),
                RailShape.ASCENDING_SOUTH);
          case ASCENDING_SOUTH:
            return state.setValue(getShapeProperty(),
                RailShape.ASCENDING_NORTH);
          case SOUTH_EAST:
            return state.setValue(getShapeProperty(),
                RailShape.NORTH_EAST);
          case SOUTH_WEST:
            return state.setValue(getShapeProperty(),
                RailShape.NORTH_WEST);
          case NORTH_WEST:
            return state.setValue(getShapeProperty(),
                RailShape.SOUTH_WEST);
          case NORTH_EAST:
            return state.setValue(getShapeProperty(),
                RailShape.SOUTH_EAST);
          default:
            return super.mirror(state, mirrorIn);
        }

      case FRONT_BACK:

        switch (trackShape) {
          case ASCENDING_EAST:
            return state.setValue(getShapeProperty(),
                RailShape.ASCENDING_WEST);
          case ASCENDING_WEST:
            return state.setValue(getShapeProperty(),
                RailShape.ASCENDING_EAST);
          case ASCENDING_NORTH:
          case ASCENDING_SOUTH:
          default:
            break;
          case SOUTH_EAST:
            return state.setValue(getShapeProperty(),
                RailShape.SOUTH_WEST);
          case SOUTH_WEST:
            return state.setValue(getShapeProperty(),
                RailShape.SOUTH_EAST);
          case NORTH_WEST:
            return state.setValue(getShapeProperty(),
                RailShape.NORTH_EAST);
          case NORTH_EAST:
            return state.setValue(getShapeProperty(),
                RailShape.NORTH_WEST);
        }
    }

    return super.mirror(state, mirrorIn);
  }

  @Override
  public TrackType getTrackType() {
    return this.trackType.get();
  }
}

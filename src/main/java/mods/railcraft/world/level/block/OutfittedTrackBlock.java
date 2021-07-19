package mods.railcraft.world.level.block;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.IChargeBlock;
import mods.railcraft.api.core.IPostConnection;
import mods.railcraft.api.tracks.IBlockTrackOutfitted;
import mods.railcraft.api.tracks.ITrackKitComparator;
import mods.railcraft.api.tracks.ITrackKitCustomShape;
import mods.railcraft.api.tracks.ITrackKitEmitter;
import mods.railcraft.api.tracks.ITrackKitInstance;
import mods.railcraft.api.tracks.TrackKit;
import mods.railcraft.api.tracks.TrackToolsAPI;
import mods.railcraft.api.tracks.TrackType;
import mods.railcraft.plugins.PowerPlugin;
import mods.railcraft.plugins.WorldPlugin;
import mods.railcraft.util.TrackShapeHelper;
import mods.railcraft.util.TrackTools;
import mods.railcraft.util.inventory.InvTools;
import mods.railcraft.world.level.block.entity.OutfittedTrackBlockEntity;
import mods.railcraft.world.level.block.entity.TickableOutfittedTrackBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.Property;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fluids.IFluidBlock;

public class OutfittedTrackBlock extends TrackBlock
    implements IPostConnection, IChargeBlock, IBlockTrackOutfitted {
  // TODO: Move to rail network
  private static final Map<Charge, ChargeSpec> CHARGE_SPECS =
      ChargeSpec.make(Charge.distribution, ConnectType.TRACK, 0.01);
  public static final EnumProperty<RailShape> SHAPE = EnumProperty.create("shape",
      RailShape.class, TrackShapeHelper::isStraight);

  public static final IntegerProperty STATE = IntegerProperty.create("state", 0, 15);

  private final Supplier<? extends TrackKit> trackKit;

  public OutfittedTrackBlock(Supplier<? extends TrackKit> trackKit,
      Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
    this.trackKit = trackKit;
  }

  @Override
  public Property<RailShape> getShapeProperty() {
    return SHAPE;
  }

  @Override
  public OutfittedTrackBlockEntity createTileEntity(BlockState state, IBlockReader blockReader) {
    return this.trackKit.get().requiresTicking()
        ? new TickableOutfittedTrackBlockEntity()
        : new OutfittedTrackBlockEntity();
  }

  @Override
  public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world,
      BlockPos pos, PlayerEntity player) {
    TileEntity tile = world.getBlockEntity(pos);
    if (tile instanceof OutfittedTrackBlockEntity) {
      OutfittedTrackBlockEntity trackTile = (OutfittedTrackBlockEntity) tile;
      ITrackKitInstance track = trackTile.getTrackKitInstance();
      ItemStack itemStack = track.getTrackKit().getTrackKitItem();
      if (!InvTools.isEmpty(itemStack))
        return itemStack;
    }
    return new ItemStack(this);
  }

  @Override
  public TrackKit getTrackKit() {
    return this.trackKit.get();
  }

  @Override
  public VoxelShape getCollisionShape(BlockState state, IBlockReader world,
      BlockPos pos, ISelectionContext context) {
    TileEntity tile = world.getBlockEntity(pos);
    if (tile instanceof OutfittedTrackBlockEntity) {
      ITrackKitInstance track = ((OutfittedTrackBlockEntity) tile).getTrackKitInstance();
      if (track instanceof ITrackKitCustomShape)
        return ((ITrackKitCustomShape) track).getCollisionShape(state);
    }
    return VoxelShapes.empty();
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos,
      ISelectionContext context) {
    TileEntity tile = world.getBlockEntity(pos);
    if (tile instanceof OutfittedTrackBlockEntity) {
      ITrackKitInstance track = ((OutfittedTrackBlockEntity) tile).getTrackKitInstance();
      if (track instanceof ITrackKitCustomShape)
        return ((ITrackKitCustomShape) track).getShape();
    }
    return super.getShape(state, world, pos, context);
  }

  @Override
  public void entityInside(BlockState state, World world, BlockPos pos, Entity entity) {
    if (world.isClientSide())
      return;
    TileEntity tile = world.getBlockEntity(pos);
    if (tile instanceof OutfittedTrackBlockEntity) {
      ((OutfittedTrackBlockEntity) tile).getTrackType().getEventHandler().onEntityCollision(world, pos,
          state, entity);
    }
  }

  @Override
  public boolean isSignalSource(BlockState state) {
    return true;
  }

  @Override
  public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos,
      @Nullable Direction side) {
    TileEntity tile = world.getBlockEntity(pos);
    if (tile instanceof OutfittedTrackBlockEntity) {
      ITrackKitInstance track = ((OutfittedTrackBlockEntity) tile).getTrackKitInstance();
      return track instanceof ITrackKitEmitter;
    }
    return false;
  }

  @Override
  public int getSignal(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
    TileEntity tile = world.getBlockEntity(pos);
    if (tile instanceof OutfittedTrackBlockEntity) {
      ITrackKitInstance track = ((OutfittedTrackBlockEntity) tile).getTrackKitInstance();
      return track instanceof ITrackKitEmitter ? ((ITrackKitEmitter) track).getPowerOutput()
          : PowerPlugin.NO_POWER;
    }
    return PowerPlugin.NO_POWER;
  }

  @Override
  public int getDirectSignal(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
    TileEntity tile = world.getBlockEntity(pos);
    if (tile instanceof OutfittedTrackBlockEntity) {
      ITrackKitInstance track = ((OutfittedTrackBlockEntity) tile).getTrackKitInstance();
      return track instanceof ITrackKitEmitter ? ((ITrackKitEmitter) track).getPowerOutput()
          : PowerPlugin.NO_POWER;
    }
    return PowerPlugin.NO_POWER;
  }

  @Override
  public boolean hasAnalogOutputSignal(BlockState state) {
    return true;
  }

  @Override
  public int getAnalogOutputSignal(BlockState blockState, World worldIn, BlockPos pos) {
    return WorldPlugin.getTileEntity(worldIn, pos, OutfittedTrackBlockEntity.class)
        .map(OutfittedTrackBlockEntity::getTrackKitInstance)
        .filter(ITrackKitComparator.class::isInstance)
        .map(ITrackKitComparator.class::cast)
        .map(ITrackKitComparator::getComparatorInputOverride)
        .orElse(0);
  }

  @Override
  public void onMinecartPass(BlockState state, World world, BlockPos pos,
      AbstractMinecartEntity cart) {
    TileEntity tile = world.getBlockEntity(pos);
    if (tile instanceof OutfittedTrackBlockEntity) {
      OutfittedTrackBlockEntity track = (OutfittedTrackBlockEntity) tile;
      this.getTrackType().getEventHandler().onMinecartPass(world, cart, pos,
          track.getTrackKitInstance().getTrackKit());
      track.getTrackKitInstance().onMinecartPass(cart);
    }
  }

  @Override
  public RailShape getRailDirection(BlockState state, IBlockReader world, BlockPos pos,
      @Nullable AbstractMinecartEntity cart) {
    TileEntity tile = world.getBlockEntity(pos);
    if (tile instanceof OutfittedTrackBlockEntity) {
      OutfittedTrackBlockEntity track = (OutfittedTrackBlockEntity) tile;
      RailShape shape =
          track.getTrackType().getEventHandler().getRailDirectionOverride(world, pos, state, cart);
      if (shape != null)
        return shape;
      return track.getTrackKitInstance().getRailDirection(state, cart);
    }
    return state.getValue(getShapeProperty());
  }

  @Override
  public float getRailMaxSpeed(BlockState state, World world, BlockPos pos,
      AbstractMinecartEntity cart) {
    TileEntity tile = world.getBlockEntity(pos);
    if (tile instanceof OutfittedTrackBlockEntity)
      return (float) ((OutfittedTrackBlockEntity) tile).getTrackKitInstance()
          .getRailMaxSpeed(world, cart, pos);
    return 0.4f;
  }

  @Override
  public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn,
      Hand hand, BlockRayTraceResult result) {
    TileEntity tile = worldIn.getBlockEntity(pos);
    return tile instanceof OutfittedTrackBlockEntity
        ? ((OutfittedTrackBlockEntity) tile).getTrackKitInstance().use(playerIn, hand)
        : ActionResultType.CONSUME;
  }

  @Override
  public boolean canMakeSlopes(BlockState state, IBlockReader world, BlockPos pos) {
    TileEntity tile = world.getBlockEntity(pos);
    return !(tile instanceof OutfittedTrackBlockEntity)
        || ((OutfittedTrackBlockEntity) tile).getTrackKitInstance().getTrackKit().isAllowedOnSlopes();
  }

  @Override
  public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
    TileEntity tile = builder.getOptionalParameter(LootParameters.BLOCK_ENTITY);
    if (tile instanceof OutfittedTrackBlockEntity)
      return ((OutfittedTrackBlockEntity) tile).getTrackKitInstance().getDrops(state, builder);
    return Collections.emptyList();
  }

  @Override
  public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player,
      boolean willHarvest, FluidState fluid) {
    BlockState newState = TrackToolsAPI.setShape(this.getTrackType().getBaseBlock(),
        TrackTools.getTrackDirectionRaw(state));
    Charge.distribution.network(world).removeNode(pos);
    boolean result = WorldPlugin.setBlockState(world, pos, newState);
    world.updateNeighborsAt(pos, this);
    // Below is ugly workaround for fluids!
    if (Arrays.stream(Direction.values())
        .map(pos::relative)
        .map(world::getBlockState)
        .map(BlockState::getBlock)
        .anyMatch(block -> block instanceof IFluidBlock || block instanceof FlowingFluidBlock)) {
      Block.dropResources(newState, world, pos);
    }
    return result;
  }

  // Determine direction here
  @Override
  public void setPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer,
      ItemStack stack) {
    TileEntity tile = world.getBlockEntity(pos);
    if (tile instanceof OutfittedTrackBlockEntity) {
      ((OutfittedTrackBlockEntity) tile).setTrackKitInstance(this.trackKit.get().createInstance());
      ((OutfittedTrackBlockEntity) tile).setPlacedBy(state, placer, stack);
      WorldPlugin.markBlockForUpdate(world, pos, state);
    }
  }

  @Override
  public void destroy(IWorld world, BlockPos pos, BlockState state) {
    TileEntity tile = world.getBlockEntity(pos);
    if (tile instanceof OutfittedTrackBlockEntity)
      ((OutfittedTrackBlockEntity) tile).getTrackKitInstance().onBlockRemoved();
    super.destroy(world, pos, state);
    Charge.distribution.network(world).removeNode(pos);
  }

  @Override
  public void neighborChanged(BlockState state, World world, BlockPos pos, Block neighborBlock,
      BlockPos neighborPos, boolean something) {
    if (world.isClientSide())
      return;
    TileEntity t = world.getBlockEntity(pos);
    if (t instanceof OutfittedTrackBlockEntity) {
      ((OutfittedTrackBlockEntity) t).neighborChanged(state, neighborBlock, neighborPos);
    }
    super.neighborChanged(state, world, pos, neighborBlock, neighborPos, something);
  }

  @Override
  public int getMaxSupportedDistance(World worldIn, BlockPos pos) {
    return Math.max(super.getMaxSupportedDistance(worldIn, pos),
        this.getTrackKit().getMaxSupportDistance());
  }

  @SuppressWarnings("deprecation")
  @Override
  public float getDestroyProgress(BlockState state, PlayerEntity player, IBlockReader world,
      BlockPos pos) {
    TileEntity tile = world.getBlockEntity(pos);
    if (tile instanceof OutfittedTrackBlockEntity
        && ((OutfittedTrackBlockEntity) tile).getTrackKitInstance().isProtected())
      return 0.0F;
    return super.getDestroyProgress(state, player, world, pos);
  }

  @Override
  public float getExplosionResistance(BlockState state, IBlockReader world, BlockPos pos,
      Explosion explosion) {
    TileEntity tile = world.getBlockEntity(pos);
    if (tile instanceof OutfittedTrackBlockEntity)
      return ((OutfittedTrackBlockEntity) tile).getTrackType().getBaseBlock().getExplosionResistance(state,
          world, pos, explosion) * 3f / 5f;
    return super.getExplosionResistance(state, world, pos, explosion);
  }

  @Override
  public ConnectStyle connectsToPost(IBlockReader world, BlockPos pos, BlockState state,
      Direction side) {
    TileEntity tile = world.getBlockEntity(pos);
    if (tile instanceof OutfittedTrackBlockEntity) {
      ITrackKitInstance track = ((OutfittedTrackBlockEntity) tile).getTrackKitInstance();
      if (track instanceof IPostConnection)
        return ((IPostConnection) track).connectsToPost(world, pos, state, side);
    }
    return ConnectStyle.NONE;
  }

  @Override
  public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
    if (this.getTrackType().isElectric())
      Charge.effects().throwSparks(stateIn, worldIn, pos, rand, 75);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void tick(BlockState state, ServerWorld world, BlockPos pos,
      Random random) {
    super.tick(state, world, pos, random);
    if (this.getTrackType().isElectric())
      registerNode(state, world, pos);
  }

  @Override
  public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldBlockState,
      boolean p_220082_5_) {
    super.onPlace(state, worldIn, pos, oldBlockState, p_220082_5_);
    if (this.getTrackType().isElectric())
      registerNode(state, worldIn, pos);
  }

  @Override
  public Map<Charge, ChargeSpec> getChargeSpecs(BlockState state, IBlockReader world,
      BlockPos pos) {
    if (this.getTrackType().isElectric())
      return CHARGE_SPECS;
    else
      return Collections.emptyMap();
  }

  @Override
  public boolean isFlexibleRail(BlockState state, IBlockReader world, BlockPos pos) {
    return false;
  }
}

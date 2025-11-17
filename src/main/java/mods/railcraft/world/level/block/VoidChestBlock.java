package mods.railcraft.world.level.block;

import javax.annotation.Nullable;
import com.mojang.serialization.MapCodec;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.VoidChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoidChestBlock extends BaseEntityBlock {

  private static final VoxelShape AABB = Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0);
  private static final MapCodec<VoidChestBlock> CODEC = simpleCodec(VoidChestBlock::new);

  public VoidChestBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(ChestBlock.FACING, Direction.NORTH)
        .setValue(ChestBlock.WATERLOGGED, false));
  }

  @Override
  public MapCodec<? extends BaseEntityBlock> codec() {
    return CODEC;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(ChestBlock.FACING, ChestBlock.WATERLOGGED);
  }

  @Override
  protected RenderShape getRenderShape(BlockState state) {
    return RenderShape.ENTITYBLOCK_ANIMATED;
  }

  @Override
  protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState,
      LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
    if (state.getValue(ChestBlock.WATERLOGGED)) {
      level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
    }
    return state;
  }

  @Override
  protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos,
      CollisionContext context) {
    return AABB;
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    var direction = context.getHorizontalDirection().getOpposite();
    var fluidstate = context.getLevel().getFluidState(context.getClickedPos());
    return this.defaultBlockState()
        .setValue(ChestBlock.FACING, direction)
        .setValue(ChestBlock.WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
  }

  @Override
  protected FluidState getFluidState(BlockState state) {
    return state.getValue(ChestBlock.WATERLOGGED)
        ? Fluids.WATER.getSource(false)
        : super.getFluidState(state);
  }

  @Override
  protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
      Player player, BlockHitResult hitResult) {
    if (player instanceof ServerPlayer serverPlayer) {
      level.getBlockEntity(pos, RailcraftBlockEntityTypes.VOID_CHEST.get())
          .ifPresent(blockEntity -> serverPlayer.openMenu(blockEntity, pos));
      PiglinAi.angerNearbyPiglins(player, true);
      return InteractionResult.CONSUME;
    }
    return InteractionResult.SUCCESS;
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
    return createTickerHelper(blockEntityType, RailcraftBlockEntityTypes.VOID_CHEST.get(),
        level.isClientSide ? VoidChestBlockEntity::clientTick : VoidChestBlockEntity::serverTick);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new VoidChestBlockEntity(pos, state);
  }

  @Override
  protected boolean hasAnalogOutputSignal(BlockState state) {
    return false;
  }

  @Override
  protected BlockState rotate(BlockState state, Rotation rotation) {
    return state.setValue(ChestBlock.FACING, rotation.rotate(state.getValue(ChestBlock.FACING)));
  }

  @Override
  protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
    return false;
  }

  @Override
  protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    if (level.getBlockEntity(pos) instanceof VoidChestBlockEntity voidChestBlockEntity) {
      voidChestBlockEntity.recheckOpen();
    }
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    for (int i = 0; i < 3; i++) {
      var x = pos.getX() + (random.nextInt(2) * 2 - 1) * random.nextFloat();
      var y = pos.getY();
      var z = pos.getZ() + (random.nextInt(2) * 2 - 1) * random.nextFloat();
      level.addParticle(ParticleTypes.PORTAL, x, y, z,
          (random.nextInt(2) * 2 - 1) * random.nextFloat(),
          (random.nextInt(2) * 2 - 1) * random.nextFloat(),
          (random.nextInt(2) * 2 - 1) * random.nextFloat());
    }
  }
}

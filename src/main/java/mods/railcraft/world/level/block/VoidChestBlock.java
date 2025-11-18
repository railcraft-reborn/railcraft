package mods.railcraft.world.level.block;

import javax.annotation.Nullable;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.VoidChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
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

  private static final MapCodec<VoidChestBlock> CODEC =
      RecordCodecBuilder.mapCodec(instance -> instance.group(
          BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("open_sound").forGetter(VoidChestBlock::getOpenChestSound),
          BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("close_sound").forGetter(VoidChestBlock::getCloseChestSound),
          propertiesCodec()
      ).apply(instance, VoidChestBlock::new)
  );
  private static final VoxelShape SHAPE = Block.column(14.0, 0.0, 14.0);
  private final SoundEvent openSound;
  private final SoundEvent closeSound;

  public VoidChestBlock(SoundEvent openSound, SoundEvent closeSound, Properties properties) {
    super(properties);
    this.openSound = openSound;
    this.closeSound = closeSound;
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
  protected BlockState updateShape(BlockState state, LevelReader level,
      ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction,
      BlockPos neighborPos, BlockState neighborState, RandomSource random) {
    if (state.getValue(ChestBlock.WATERLOGGED)) {
      scheduledTickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
    }
    return super.updateShape(state, level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
  }

  @Override
  protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos,
      CollisionContext context) {
    return SHAPE;
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    var direction = context.getHorizontalDirection().getOpposite();
    var fluidstate = context.getLevel().getFluidState(context.getClickedPos());
    return this.defaultBlockState()
        .setValue(ChestBlock.FACING, direction)
        .setValue(ChestBlock.WATERLOGGED, fluidstate.getType() == Fluids.WATER);
  }

  @Override
  protected FluidState getFluidState(BlockState state) {
    return state.getValue(ChestBlock.WATERLOGGED)
        ? Fluids.WATER.getSource(false)
        : super.getFluidState(state);
  }

  @Override
  protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos,
      boolean movedByPiston) {
    Containers.updateNeighboursAfterDestroy(state, level, pos);
  }

  @Override
  protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
      Player player, BlockHitResult hitResult) {
    if (player instanceof ServerPlayer serverPlayer) {
      level.getBlockEntity(pos, RailcraftBlockEntityTypes.VOID_CHEST.get())
          .ifPresent(blockEntity -> serverPlayer.openMenu(blockEntity, pos));
      PiglinAi.angerNearbyPiglins(serverPlayer.level(), player, true);
      return InteractionResult.CONSUME;
    }
    return InteractionResult.SUCCESS;
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
    return createTickerHelper(blockEntityType, RailcraftBlockEntityTypes.VOID_CHEST.get(),
        level.isClientSide() ? VoidChestBlockEntity::clientTick : VoidChestBlockEntity::serverTick);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new VoidChestBlockEntity(pos, state);
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

  private SoundEvent getOpenChestSound() {
    return this.openSound;
  }

  private SoundEvent getCloseChestSound() {
    return this.closeSound;
  }
}

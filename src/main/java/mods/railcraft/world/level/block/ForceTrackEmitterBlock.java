package mods.railcraft.world.level.block;

import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.ChargeBlock;
import mods.railcraft.util.Optionals;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.world.level.block.entity.ForceTrackEmitterBlockEntity;
import mods.railcraft.world.level.block.entity.ForceTrackEmitterState;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.*;

public class ForceTrackEmitterBlock extends BaseEntityBlock implements ChargeBlock {

  public static final DyeColor DEFAULT_COLOR = DyeColor.LIGHT_BLUE;
  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
  public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
  public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
  private static final Map<Charge, Spec> CHARGE_SPECS =
      Spec.make(Charge.distribution, ConnectType.BLOCK, 0.1F);

  public ForceTrackEmitterBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(FACING, Direction.NORTH)
        .setValue(POWERED, false)
        .setValue(COLOR, DEFAULT_COLOR));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, POWERED, COLOR);
  }

  @Override
  public RenderShape getRenderShape(BlockState state) {
    return RenderShape.MODEL;
  }

  @Override
  public Map<Charge, Spec> getChargeSpecs(BlockState state, ServerLevel level,
      BlockPos pos) {
    return CHARGE_SPECS;
  }

  @SuppressWarnings("deprecation")
  @Override
  public InteractionResult use(BlockState state, Level worldIn, BlockPos pos,
      Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
    if (super.use(state, worldIn, pos, player, hand, rayTraceResult).consumesAction())
      return InteractionResult.CONSUME;
    if (player.isShiftKeyDown())
      return InteractionResult.FAIL;
    ItemStack heldItem = player.getItemInHand(hand);
    if (heldItem.isEmpty() || hand == InteractionHand.OFF_HAND)
      return InteractionResult.FAIL;
    BlockEntity blockEntity = worldIn.getBlockEntity(pos);
    if (blockEntity instanceof ForceTrackEmitterBlockEntity) {
      ForceTrackEmitterBlockEntity t = (ForceTrackEmitterBlockEntity) blockEntity;
      if (Optionals.test(Optional.ofNullable(DyeColor.getColor(heldItem)), t::setColor)) {
        if (!player.isCreative())
          player.setItemInHand(hand, ContainerTools.depleteItem(heldItem));
        return InteractionResult.SUCCESS;
      }
    }
    return InteractionResult.FAIL;
  }

  private ItemStack getItem(BlockState blockState) {
    ItemStack itemStack = this.asItem().getDefaultInstance();
    CompoundTag tag = itemStack.getOrCreateTag();
    tag.putInt("color", blockState.getValue(COLOR).getId());
    return itemStack;
  }

  @Override
  public ItemStack getCloneItemStack(BlockState blockState, HitResult target, BlockGetter world,
      BlockPos pos, Player player) {
    return this.getItem(blockState);
  }

  @Override
  public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder builder) {
    return Collections.singletonList(this.getItem(blockState));
  }

  @Override
  public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
    if (stateIn.getValue(POWERED)) {
      Charge.zapEffectProvider().throwSparks(stateIn, worldIn, pos, rand, 10);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource rand) {
    super.tick(state, worldIn, pos, rand);
    this.registerNode(state, worldIn, pos);
  }

  @Override
  public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos,
      Rotation direction) {
    if (!level.getBlockEntity(pos, RailcraftBlockEntityTypes.FORCE_TRACK_EMITTER.get())
        .map(ForceTrackEmitterBlockEntity::getStateInstance)
        .map(ForceTrackEmitterState.Instance::getState)
        .filter(ForceTrackEmitterState.RETRACTED::equals)
        .isPresent()) {
      return state;
    }
    return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState,
      boolean moved) {
    super.onPlace(state, level, pos, oldState, moved);
    if (!state.is(oldState.getBlock())) {
      this.registerNode(state, (ServerLevel) level, pos);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block,
      BlockPos changedPos, boolean something) {
    super.neighborChanged(state, level, pos, block, changedPos, something);
    level.getBlockEntity(pos, RailcraftBlockEntityTypes.FORCE_TRACK_EMITTER.get())
        .ifPresent(ForceTrackEmitterBlockEntity::checkSignal);
  }

  @Override
  public void setPlacedBy(Level level, BlockPos pos, BlockState state,
      @Nullable LivingEntity livingEntity, ItemStack itemStack) {
    super.setPlacedBy(level, pos, state, livingEntity, itemStack);
    DyeColor color = DyeColor.getColor(itemStack);
    if (color != null) {
      state.setValue(COLOR, color);
    }
    level.getBlockEntity(pos, RailcraftBlockEntityTypes.FORCE_TRACK_EMITTER.get())
        .ifPresent(ForceTrackEmitterBlockEntity::checkSignal);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState,
      boolean moved) {
    super.onRemove(state, level, pos, newState, moved);
    if (!state.is(newState.getBlock())) {
      this.deregisterNode((ServerLevel) level, pos);
    }
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new ForceTrackEmitterBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide() ? null
        : createTickerHelper(type, RailcraftBlockEntityTypes.FORCE_TRACK_EMITTER.get(),
            ForceTrackEmitterBlockEntity::serverTick);
  }

  public static Direction getFacing(BlockState blockState) {
    return blockState.getValue(FACING);
  }
}

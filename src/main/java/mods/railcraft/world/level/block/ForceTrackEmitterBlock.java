package mods.railcraft.world.level.block;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.ChargeBlock;
import mods.railcraft.api.charge.ChargeStorage;
import mods.railcraft.api.charge.ChargeStorage.State;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.world.level.block.entity.ForceTrackEmitterBlockEntity;
import mods.railcraft.world.level.block.entity.ForceTrackEmitterState;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ForceTrackEmitterBlock extends BaseEntityBlock implements ChargeBlock {

  public static final DyeColor DEFAULT_COLOR = DyeColor.LIGHT_BLUE;
  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
  public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
  public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
  private static final Map<Charge, Spec> CHARGE_SPECS =
      Spec.make(Charge.distribution, ConnectType.BLOCK, 0,
          new ChargeStorage.Spec(State.RECHARGEABLE, 1000, 1000, 1));

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
  public Map<Charge, Spec> getChargeSpecs(BlockState state, ServerLevel level, BlockPos pos) {
    return CHARGE_SPECS;
  }

  @SuppressWarnings("deprecation")
  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos,
      Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
    if (super.use(state, level, pos, player, hand, rayTraceResult).consumesAction()) {
      return InteractionResult.CONSUME;
    }
    if (player.isShiftKeyDown()) {
      return InteractionResult.FAIL;
    }
    ItemStack heldItem = player.getItemInHand(hand);
    if (heldItem.isEmpty() || hand == InteractionHand.OFF_HAND) {
      return InteractionResult.FAIL;
    }
    if (level.getBlockEntity(pos) instanceof ForceTrackEmitterBlockEntity t) {
      var color = DyeColor.getColor(heldItem);
      if (color != null && t.setColor(color)) {
        if (!player.isCreative()) {
          player.setItemInHand(hand, ContainerTools.depleteItem(heldItem));
        }
        return InteractionResult.SUCCESS;
      }
    }
    return InteractionResult.FAIL;
  }

  private ItemStack getItem(BlockState blockState) {
    var itemStack = this.asItem().getDefaultInstance();
    var tag = itemStack.getOrCreateTag();
    tag.putString("color", blockState.getValue(COLOR).getName());
    return itemStack;
  }

  @Override
  public ItemStack getCloneItemStack(BlockState blockState, HitResult target, BlockGetter world,
      BlockPos pos, Player player) {
    return this.getItem(blockState);
  }

  @Override
  public List<ItemStack> getDrops(BlockState blockState, LootParams.Builder builder) {
    return Collections.singletonList(this.getItem(blockState));
  }

  @Override
  public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand) {
    if (stateIn.getValue(POWERED)) {
      Charge.zapEffectProvider().throwSparks(stateIn, level, pos, rand, 10);
    }
  }

  @Override
  public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    this.registerNode(state, level, pos);
  }

  @Override
  public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos,
      Rotation direction) {
    if (level.getBlockEntity(pos, RailcraftBlockEntityTypes.FORCE_TRACK_EMITTER.get())
        .map(ForceTrackEmitterBlockEntity::getStateInstance)
        .map(ForceTrackEmitterState.Instance::state)
        .filter(ForceTrackEmitterState.RETRACTED::equals)
        .isEmpty()) {
      return state;
    }
    return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
  }

  @Override
  public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState,
      boolean isMoving) {
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
    var tag = itemStack.getTag();
    if (tag != null) {
      if (level.getBlockEntity(pos) instanceof ForceTrackEmitterBlockEntity t) {
        var color = DyeColor.byName(tag.getString("color"), null);
        if (color != null) {
          t.setColor(color);
        }
      }
    }
    level.getBlockEntity(pos, RailcraftBlockEntityTypes.FORCE_TRACK_EMITTER.get())
        .ifPresent(ForceTrackEmitterBlockEntity::checkSignal);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState,
      boolean moved) {
    if (!state.is(newState.getBlock())) {
      level.getBlockEntity(pos, RailcraftBlockEntityTypes.FORCE_TRACK_EMITTER.get())
          .ifPresent(ForceTrackEmitterBlockEntity::clearTracks);
    }
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

  @Override
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip,
      TooltipFlag flag) {
    super.appendHoverText(stack, level, tooltip, flag);
    tooltip.add(Component.translatable(Translations.Tips.FORCE_TRACK_EMITTER).withStyle(ChatFormatting.GRAY));
  }

  public static Direction getFacing(BlockState blockState) {
    return blockState.getValue(FACING);
  }
}

package mods.railcraft.world.level.block;

import org.jetbrains.annotations.Nullable;
import com.mojang.serialization.MapCodec;
import mods.railcraft.Translations;
import mods.railcraft.integrations.jei.JeiSearchable;
import mods.railcraft.world.level.block.entity.LogBookBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;

public class LogBookBlock extends HorizontalDirectionalBlock implements EntityBlock, JeiSearchable {

  private static final MapCodec<LogBookBlock> CODEC = simpleCodec(LogBookBlock::new);

  public LogBookBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(FACING, Direction.NORTH));
  }

  @Override
  protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
    return CODEC;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING);
  }

  @Override
  public RenderShape getRenderShape(BlockState pState) {
    return RenderShape.MODEL;
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return this.defaultBlockState()
        .setValue(FACING, context.getHorizontalDirection().getOpposite());
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new LogBookBlockEntity(pos, state);
  }

  @Override
  protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos pos,
      Player player, BlockHitResult rayTraceResult) {
    if (player instanceof ServerPlayer serverPlayer) {
      level.getBlockEntity(pos, RailcraftBlockEntityTypes.LOGBOOK.get())
          .ifPresent(blockEntity -> blockEntity.use(serverPlayer));
    }
    return InteractionResult.SUCCESS;
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide() ? null
        : BaseEntityBlock.createTickerHelper(type, RailcraftBlockEntityTypes.LOGBOOK.get(),
            LogBookBlockEntity::serverTick);
  }

  @Override
  public void setPlacedBy(Level level, BlockPos pos, BlockState state,
      @Nullable LivingEntity placer, ItemStack stack) {
    if (level.getBlockEntity(pos) instanceof LogBookBlockEntity blockEntity) {
      if (placer instanceof Player player) {
        blockEntity.setOwner(player.nameAndId());
      }
    }
  }

  @Override
  public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player,
      ItemStack toolStack, boolean willHarvest, FluidState fluid) {
    if (level.getBlockEntity(pos) instanceof LogBookBlockEntity blockEntity) {
      if (blockEntity.getOwner().isEmpty() ||
          blockEntity.isOwnerOrOperator(player.nameAndId())) {
        return super.onDestroyedByPlayer(state, level, pos, player, toolStack, willHarvest, fluid);
      }
    }
    return false;
  }

  @Override
  public Component jeiDescription() {
    return Component.translatable(Translations.Jei.LOGBOOK);
  }
}

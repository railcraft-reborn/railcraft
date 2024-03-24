package mods.railcraft.world.level.block;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import com.mojang.serialization.MapCodec;
import mods.railcraft.Translations;
import mods.railcraft.integrations.jei.JeiSearchable;
import mods.railcraft.world.level.block.entity.FeedStationBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class FeedStationBlock extends BaseEntityBlock implements JeiSearchable {

  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
  private static final MapCodec<FeedStationBlock> CODEC = simpleCodec(FeedStationBlock::new);

  protected FeedStationBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
  }

  @Override
  protected MapCodec<? extends BaseEntityBlock> codec() {
    return CODEC;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(POWERED);
  }

  @Override
  public RenderShape getRenderShape(BlockState blockState) {
    return RenderShape.MODEL;
  }

  @Override
  public InteractionResult use(BlockState blockState, Level level, BlockPos pos,
      Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
    if (player instanceof ServerPlayer serverPlayer) {
      level.getBlockEntity(pos, RailcraftBlockEntityTypes.FEED_STATION.get())
          .ifPresent(blockEntity -> serverPlayer.openMenu(blockEntity, pos));
    }
    return InteractionResult.sidedSuccess(level.isClientSide());
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new FeedStationBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide() ? null
        : createTickerHelper(type, RailcraftBlockEntityTypes.FEED_STATION.get(),
            FeedStationBlockEntity::serverTick);
  }

  @Override
  public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos,
      Block neighborBlock, BlockPos neighborPos, boolean moved) {
    if (!level.isClientSide()) {
      var powered = blockState.getValue(POWERED);
      var neighborSignal = level.hasNeighborSignal(blockPos);
      if (powered != neighborSignal) {
        level.setBlock(blockPos, blockState.setValue(POWERED, neighborSignal),
            Block.UPDATE_CLIENTS);
      }
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState,
      boolean isMoving) {
    if (!state.is(newState.getBlock())
        && level.getBlockEntity(pos) instanceof FeedStationBlockEntity feedStation) {
      Containers.dropContents(level, pos, feedStation);
      level.updateNeighbourForOutputSignal(pos, this);
    }
    super.onRemove(state, level, pos, newState, isMoving);
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip,
      TooltipFlag flag) {
    super.appendHoverText(stack, level, tooltip, flag);
    tooltip
        .add(Component.translatable(Translations.Tips.FEED_STATION).withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable(Translations.Tips.APPLY_REDSTONE_TO_DISABLE)
        .withStyle(ChatFormatting.RED));
  }

  @Override
  public Component jeiDescription() {
    return Component.translatable(Translations.Jei.FEED_STATION);
  }
}

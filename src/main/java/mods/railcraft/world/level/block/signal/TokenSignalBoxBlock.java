package mods.railcraft.world.level.block.signal;

import org.jspecify.annotations.Nullable;
import com.mojang.serialization.MapCodec;
import mods.railcraft.client.ScreenFactories;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.signal.TokenSignalBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class TokenSignalBoxBlock extends SignalBoxBlock implements EntityBlock {

  private static final MapCodec<TokenSignalBoxBlock> CODEC =
      simpleCodec(TokenSignalBoxBlock::new);

  public TokenSignalBoxBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected MapCodec<? extends CrossCollisionBlock> codec() {
    return CODEC;
  }

  @Override
  protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos pos,
      Player player, BlockHitResult rayTraceResult) {
    if (level.isClientSide()) {
      level.getBlockEntity(pos, RailcraftBlockEntityTypes.TOKEN_SIGNAL_BOX.get())
          .ifPresent(ScreenFactories::openActionSignalBoxScreen);
    }
    return InteractionResult.SUCCESS;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new TokenSignalBoxBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return BaseEntityBlock.createTickerHelper(type,
        RailcraftBlockEntityTypes.TOKEN_SIGNAL_BOX.get(),
        level.isClientSide()
            ? TokenSignalBoxBlockEntity::clientTick
            : TokenSignalBoxBlockEntity::serverTick);
  }
}

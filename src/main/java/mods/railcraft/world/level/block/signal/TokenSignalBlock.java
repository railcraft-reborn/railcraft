package mods.railcraft.world.level.block.signal;

import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.integrations.jei.JeiSearchable;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.signal.TokenSignalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TokenSignalBlock extends SingleSignalBlock implements JeiSearchable {

  public TokenSignalBlock(Properties properties) {
    super(properties);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState,
      boolean moved) {
    if (!blockState.is(newState.getBlock())) {
      level.getBlockEntity(blockPos, RailcraftBlockEntityTypes.TOKEN_SIGNAL.get())
          .ifPresent(TokenSignalBlockEntity::blockRemoved);
    }
    super.onRemove(blockState, level, blockPos, newState, moved);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new TokenSignalBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return createTickerHelper(type, RailcraftBlockEntityTypes.TOKEN_SIGNAL.get(),
        level.isClientSide()
            ? TokenSignalBlockEntity::clientTick
            : TokenSignalBlockEntity::serverTick);
  }

  @Override
  public Component addJeiInfo() {
    return Component.translatable(Translations.Jei.TOKEN_SIGNAL);
  }
}

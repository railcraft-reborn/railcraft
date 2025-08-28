package mods.railcraft.world.level.block.signal;

import org.jetbrains.annotations.Nullable;
import com.mojang.serialization.MapCodec;
import mods.railcraft.Translations;
import mods.railcraft.integrations.jei.JeiSearchable;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.signal.DualBlockSignalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DualBlockSignalBlock extends DualSignalBlock implements JeiSearchable {

  private static final MapCodec<DualBlockSignalBlock> CODEC =
      simpleCodec(DualBlockSignalBlock::new);

  public DualBlockSignalBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected MapCodec<? extends DualSignalBlock> codec() {
    return CODEC;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new DualBlockSignalBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return createTickerHelper(type, RailcraftBlockEntityTypes.DUAL_BLOCK_SIGNAL.get(),
        level.isClientSide()
            ? DualBlockSignalBlockEntity::clientTick
            : DualBlockSignalBlockEntity::serverTick);
  }

  @Override
  public Component jeiDescription() {
    return Component.translatable(Translations.Jei.DUAL_BLOCK_SIGNAL);
  }
}

package mods.railcraft.world.level.block.signal;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import com.mojang.serialization.MapCodec;
import mods.railcraft.Translations;
import mods.railcraft.integrations.jei.JeiSearchable;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.signal.BlockSignalBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockSignalBlock extends SingleSignalBlock implements JeiSearchable {

  private static final MapCodec<BlockSignalBlock> CODEC = simpleCodec(BlockSignalBlock::new);

  public BlockSignalBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected MapCodec<? extends SingleSignalBlock> codec() {
    return CODEC;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState,
      boolean moved) {
    if (!blockState.is(newState.getBlock())) {
      level.getBlockEntity(blockPos, RailcraftBlockEntityTypes.BLOCK_SIGNAL.get())
          .ifPresent(BlockSignalBlockEntity::blockRemoved);
    }
    super.onRemove(blockState, level, blockPos, newState, moved);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new BlockSignalBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return createTickerHelper(type, RailcraftBlockEntityTypes.BLOCK_SIGNAL.get(),
        level.isClientSide()
            ? BlockSignalBlockEntity::clientTick
            : BlockSignalBlockEntity::serverTick);
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level,
      List<Component> tooltip, TooltipFlag flag) {
    tooltip.add(Component.translatable(Translations.Tips.BLOCK_SIGNAL)
        .withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable(Translations.Tips.AERIAL_LINKAGES)
        .withStyle(ChatFormatting.BLUE));
    tooltip.add(Component.literal("- ")
        .append(Component.translatable(Translations.Tips.SIGNAL_BLOCKS, 1))
        .withStyle(ChatFormatting.BLUE));
    tooltip.add(Component.literal("- ")
        .append(Component.translatable(Translations.Tips.CONTROLLERS, 1))
        .withStyle(ChatFormatting.BLUE));
    tooltip.add(Component.translatable(Translations.Tips.RELEVANT_TOOLS)
        .withStyle(ChatFormatting.RED));
    tooltip.add(Component.literal("- ")
        .append(Component.translatable(Translations.Tips.SIGNAL_SURVEYOR))
        .withStyle(ChatFormatting.RED));
  }

  @Override
  public Component jeiDescription() {
    return Component.translatable(Translations.Jei.BLOCK_SIGNAL);
  }
}

package mods.railcraft.world.level.block.signal;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.integrations.jei.JeiSearchable;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.signal.DistantSignalBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DistantSignalBlock extends SingleSignalBlock implements JeiSearchable {

  public DistantSignalBlock(Properties properties) {
    super(properties);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState,
      boolean moved) {
    if (!blockState.is(newState.getBlock())) {
      level.getBlockEntity(blockPos, RailcraftBlockEntityTypes.DISTANT_SIGNAL.get())
          .ifPresent(DistantSignalBlockEntity::blockRemoved);
    }
    super.onRemove(blockState, level, blockPos, newState, moved);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new DistantSignalBlockEntity(blockPos, blockState);
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level,
      List<Component> tooltip, TooltipFlag flag) {
    tooltip.add(Component.translatable(Translations.Tips.DISTANT_SIGNAL)
        .withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable(Translations.Tips.AERIAL_LINKAGES)
        .withStyle(ChatFormatting.BLUE));
    tooltip.add(Component.literal("- ")
        .append(Component.translatable(Translations.Tips.RECEIVERS, 1))
        .withStyle(ChatFormatting.BLUE));
    tooltip.add(Component.translatable(Translations.Tips.RELEVANT_TOOLS)
        .withStyle(ChatFormatting.RED));
    tooltip.add(Component.literal("- ")
        .append(Component.translatable(Translations.Tips.SIGNAL_TUNER))
        .withStyle(ChatFormatting.RED));
  }

  @Override
  public Component addJeiInfo() {
    return Component.translatable(Translations.Jei.DISTANT_SIGNAL);
  }
}

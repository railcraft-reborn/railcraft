package mods.railcraft.world.level.block.signal;

import java.util.List;
import mods.railcraft.Translations.Tips;
import mods.railcraft.world.level.block.entity.signal.SignalSequencerBoxBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SignalSequencerBoxBlock extends SelfAttachableSignalBoxBlock implements EntityBlock {

  public SignalSequencerBoxBlock(Properties properties) {
    super(properties);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new SignalSequencerBoxBlockEntity(blockPos, blockState);
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level,
      List<Component> tooltip, TooltipFlag flag) {
    tooltip.add(Component.translatable(Tips.SIGNAL_SEQUENCER_BOX).withStyle(ChatFormatting.GRAY));
  }
}

package mods.railcraft.world.level.block.track.actuator;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.world.level.block.entity.SwitchTrackLeverBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class SwitchTrackLeverBlock extends SwitchTrackActuatorBlock implements EntityBlock {

  public SwitchTrackLeverBlock(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos,
      Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
    setSwitched(blockState, level, blockPos, !blockState.getValue(SWITCHED));
    return InteractionResult.sidedSuccess(level.isClientSide());
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new SwitchTrackLeverBlockEntity(pos, state);
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level,
      List<Component> tooltip, TooltipFlag flag) {
    tooltip.add(Component.translatable(Translations.Tips.SWITCH_TRACKS)
        .withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable(Translations.Tips.MANUAL_OPERATION)
        .withStyle(ChatFormatting.BLUE));
  }
}

package mods.railcraft.world.level.block.track.actuator;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.client.ScreenFactories;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.SwitchTrackMotorBlockEntity;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class SwitchTrackMotorBlock extends SwitchTrackActuatorBlock implements EntityBlock {

  public SwitchTrackMotorBlock(Properties properties) {
    super(properties);
  }

  @Override
  public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos,
      Block neighborBlock, BlockPos neighborBlockPos, boolean moved) {
    level.getBlockEntity(blockPos, RailcraftBlockEntityTypes.SWITCH_TRACK_MOTOR.get())
        .ifPresent(SwitchTrackMotorBlockEntity::neighborChanged);
  }

  @Override
  public InteractionResult use(BlockState blockState, Level level, BlockPos pos,
      Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
    if (level.isClientSide()) {
      level.getBlockEntity(pos, RailcraftBlockEntityTypes.SWITCH_TRACK_MOTOR.get())
          .ifPresent(ScreenFactories::openSwitchTrackMotorScreen);
      return InteractionResult.SUCCESS;
    }
    return InteractionResult.CONSUME;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new SwitchTrackMotorBlockEntity(blockPos, blockState);
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level,
      List<Component> tooltip, TooltipFlag flag) {
    tooltip.add(Component.translatable(Translations.Tips.SWITCH_TRACKS)
        .withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable(Translations.Tips.AERIAL_LINKAGES)
        .append(Component.literal(" "))
        .append(Component.translatable(Translations.Tips.RECEIVERS, 1))
        .withStyle(ChatFormatting.BLUE));
    tooltip.add(Component.translatable(Translations.Tips.REDSTONE_LINKAGE)
        .append(Component.literal(" "))
        .append(Component.translatable(Translations.Tips.LISTEN))
        .withStyle(ChatFormatting.BLUE));
    tooltip.add(Component.translatable(Translations.Tips.RELEVANT_TOOLS)
        .withStyle(ChatFormatting.RED));
    tooltip.add(Component.literal("- ")
        .append(Component.translatable(Translations.Tips.SIGNAL_TUNER))
        .withStyle(ChatFormatting.RED));
  }
}

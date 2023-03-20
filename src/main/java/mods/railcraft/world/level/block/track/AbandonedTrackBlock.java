package mods.railcraft.world.level.block.track;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class AbandonedTrackBlock extends TrackBlock {

  public static final BooleanProperty GRASS = BooleanProperty.create("grass");

  public AbandonedTrackBlock(Properties properties) {
    super(TrackTypes.ABANDONED, properties);
  }

  @Override
  protected BlockState buildDefaultState(BlockState blockState) {
    return super.buildDefaultState(blockState)
        .setValue(GRASS, false);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(GRASS);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    boolean grass = Direction.Plane.HORIZONTAL.stream()
        .anyMatch(s -> context.getLevel().getBlockState(context.getClickedPos().relative(s))
            .is(Blocks.TALL_GRASS));
    return super.getStateForPlacement(context).setValue(GRASS, grass);
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level,
      List<Component> tooltip, TooltipFlag flag) {
    tooltip.add(Component.translatable(Translations.Tips.DANGER)
        .append(" ")
        .append(Component.translatable(Translations.Tips.DERAILMENT_RISK))
        .withStyle(ChatFormatting.BLUE));
    tooltip.add(Component.translatable(Translations.Tips.ABANDONED_TRACK)
        .withStyle(ChatFormatting.GRAY));
  }
}

package mods.railcraft.world.level.block.manipulator;

import java.util.List;
import mods.railcraft.Translations.Tips;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

public class AdvancedItemUnloaderBlock extends ItemUnloaderBlock {

  public static final DirectionProperty FACING = BlockStateProperties.FACING;

  public AdvancedItemUnloaderBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(POWERED, false)
        .setValue(FACING, Direction.UP));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(FACING);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return this.defaultBlockState()
        .setValue(FACING, context.getNearestLookingDirection().getOpposite());
  }

  @Override
  public Direction getFacing(BlockState blockState) {
    return blockState.getValue(FACING);
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip,
      TooltipFlag flag) {
    tooltip.add(Component.translatable(Tips.ITEM_UNLOADER).withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable(Tips.HIT_CROWBAR_TO_ROTATE)
        .withStyle(ChatFormatting.BLUE));
  }
}

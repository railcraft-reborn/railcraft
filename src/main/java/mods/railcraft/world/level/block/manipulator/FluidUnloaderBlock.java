package mods.railcraft.world.level.block.manipulator;

import java.util.List;
import mods.railcraft.Translations.Tips;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.manipulator.FluidUnloaderBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.ManipulatorBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FluidUnloaderBlock extends FluidManipulatorBlock<FluidUnloaderBlockEntity> {

  public FluidUnloaderBlock(Properties properties) {
    super(FluidUnloaderBlockEntity.class, properties);
  }

  @Override
  public Direction getFacing(BlockState blockState) {
    return Direction.UP;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new FluidUnloaderBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide() ? null
        : createTickerHelper(type, RailcraftBlockEntityTypes.FLUID_UNLOADER.get(),
            ManipulatorBlockEntity::serverTick);
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip,
      TooltipFlag flag) {
    super.appendHoverText(stack, level, tooltip, flag);
    tooltip.add(Component.translatable(Tips.FLUID_UNLOADER).withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable(Tips.PLACE_UNDER_TRACK).withStyle(ChatFormatting.RED));
  }
}

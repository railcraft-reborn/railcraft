package mods.railcraft.world.level.block;

import java.util.List;
import mods.railcraft.Translations;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.WaterTankSidingBlockEntity;
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
import org.jetbrains.annotations.Nullable;

public class WaterTankSiding extends MultiblockBlock {

  public WaterTankSiding(Properties properties) {
    super(properties);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState blockState) {
    return new WaterTankSidingBlockEntity(pos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide()
        ? null
        : createTickerHelper(type, RailcraftBlockEntityTypes.WATER_TANK_SIDING.get(),
            WaterTankSidingBlockEntity::serverTick);
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level,
      List<Component> tooltip, TooltipFlag flag) {
    tooltip.add(Component.translatable(Translations.Tips.WATER_TANK_SIDING)
        .withStyle(ChatFormatting.GRAY));
  }
}

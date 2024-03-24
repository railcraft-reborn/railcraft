package mods.railcraft.world.level.block;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import com.mojang.serialization.MapCodec;
import mods.railcraft.Translations;
import mods.railcraft.world.level.block.entity.CokeOvenBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CokeOvenBricksBlock extends FurnaceMultiblockBlock {

  private static final MapCodec<CokeOvenBricksBlock> CODEC = simpleCodec(CokeOvenBricksBlock::new);

  public CokeOvenBricksBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected MapCodec<? extends FurnaceMultiblockBlock> codec() {
    return CODEC;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new CokeOvenBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide() ? null
        : createTickerHelper(type, RailcraftBlockEntityTypes.COKE_OVEN.get(),
            CokeOvenBlockEntity::serverTick);
  }

  @Override
  public boolean isValidSpawn(BlockState state, BlockGetter level, BlockPos pos,
      SpawnPlacements.Type type, EntityType<?> entityType) {
    return false;
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level,
      List<Component> tooltip, TooltipFlag flag) {
    tooltip.add(Component.translatable(Translations.Tips.COKE_OVEN).withStyle(ChatFormatting.GRAY));
  }
}

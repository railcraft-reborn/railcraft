package mods.railcraft.world.level.block;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.world.level.block.entity.ManualRollingMachineBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ManualRollingMachineBlock extends BaseEntityBlock {

  public ManualRollingMachineBlock(Properties properties) {
    super(properties);
  }

  @Override
  public RenderShape getRenderShape(BlockState blockState) {
    return RenderShape.MODEL;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new ManualRollingMachineBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide() ? null
        : createTickerHelper(type, RailcraftBlockEntityTypes.MANUAL_ROLLING_MACHINE.get(),
            ManualRollingMachineBlockEntity::serverTick);
  }

  protected void openContainer(Level world, BlockPos blockPos, Player player) {
    BlockEntity blockEntity = world.getBlockEntity(blockPos);
    if (blockEntity instanceof ManualRollingMachineBlockEntity) {
      player.openMenu((MenuProvider) blockEntity);
      // player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
      // TODO: interaction stats
    }
  }

  @Override
  public InteractionResult use(BlockState blockState, Level world,
      BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
    if (world.isClientSide()) {
      return InteractionResult.SUCCESS;
    } else {
      this.openContainer(world, pos, player);
      return InteractionResult.CONSUME;
    }
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip,
      TooltipFlag flag) {
    super.appendHoverText(stack, level, tooltip, flag);
    tooltip.add(Component.translatable(Translations.Tips.MANUAL_ROLLING_MACHINE).withStyle(ChatFormatting.GRAY));
  }
}

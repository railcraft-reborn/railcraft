package mods.railcraft.world.level.block;

import mods.railcraft.crafting.RollingTableContainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class RollingTable extends Block {
  private static final ITextComponent CONTAINER_TITLE = new TranslationTextComponent("gui.railcraft.rolling_table");

  public RollingTable(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResultType use(BlockState blockState, World world,
    BlockPos pos, PlayerEntity user, Hand userHand, BlockRayTraceResult blockRayResult) {
    if (world.isClientSide) {
      return ActionResultType.SUCCESS;
    } else {
      user.openMenu(blockState.getMenuProvider(world, pos));
      user.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE); // TODO custom stats (low prio)
      return ActionResultType.CONSUME;
    }
  }

  @Override
  public INamedContainerProvider getMenuProvider(BlockState blockState, World world, BlockPos pos) {
      return new SimpleNamedContainerProvider((containerProvider, textComponent, unused) -> {
        return new RollingTableContainer(containerProvider, textComponent, IWorldPosCallable.create(world, pos));
      }, CONTAINER_TITLE);
  }

}

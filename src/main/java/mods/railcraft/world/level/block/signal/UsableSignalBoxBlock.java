package mods.railcraft.world.level.block.signal;

import java.util.function.Consumer;
import java.util.function.Supplier;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBoxBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class UsableSignalBoxBlock<T extends AbstractSignalBoxBlockEntity> extends SignalBoxBlock {

  private final Class<T> blockEntityType;
  private final Consumer<T> useListener;

  public UsableSignalBoxBlock(Class<T> blockEntityType, Consumer<T> useListener,
      Supplier<T> blockEntityFactory, Properties properties) {
    super(blockEntityFactory, properties);
    this.blockEntityType = blockEntityType;
    this.useListener = useListener;
  }

  @Override
  public ActionResultType use(BlockState blockState, World level, BlockPos pos,
      PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
    if (level.isClientSide()) {
      LevelUtil.getBlockEntity(level, pos, this.blockEntityType).ifPresent(this.useListener);
      return ActionResultType.SUCCESS;
    }
    return ActionResultType.CONSUME;
  }
}

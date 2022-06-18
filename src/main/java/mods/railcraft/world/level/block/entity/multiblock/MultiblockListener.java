package mods.railcraft.world.level.block.entity.multiblock;

import javax.annotation.Nullable;
import com.google.common.primitives.Ints;
import mods.railcraft.world.level.gameevent.RailcraftGameEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;

public class MultiblockListener implements GameEventListener {

  private final MultiblockBlockEntity<?, ?> blockEntity;
  private final PositionSource positionSource;
  private final int radius;

  public MultiblockListener(MultiblockBlockEntity<?, ?> blockEntity) {
    this.blockEntity = blockEntity;
    this.positionSource = new BlockPositionSource(blockEntity.getBlockPos());

    var radius = 0;
    for (var pattern : blockEntity.getPatterns()) {
      // We use the largest length for the radius as the listener needs to cover every block in the
      // multiblock.
      radius =
          Math.max(radius, Ints.max(pattern.getXSize(), pattern.getYSize(), pattern.getZSize()));
    }
    this.radius = radius;
  }

  @Override
  public PositionSource getListenerSource() {
    return this.positionSource;
  }

  @Override
  public int getListenerRadius() {
    return this.radius;
  }

  @Override
  public boolean handleGameEvent(Level level, GameEvent event, @Nullable Entity entity,
      BlockPos blockPos) {
    if (event == RailcraftGameEvents.NEIGHBOR_NOTIFY.get()) {
      // Can't check immediately as these events are fired before the blocks are actually changed.
      this.blockEntity.enqueueEvaluation();
    }
    return false;
  }
}

package mods.railcraft.world.level.block.entity.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;

public class MultiblockListener implements GameEventListener {

  private final MultiblockBlockEntity<?> blockEntity;
  private final PositionSource positionSource;

  public MultiblockListener(MultiblockBlockEntity<?> blockEntity) {
    this.blockEntity = blockEntity;
    this.positionSource = new BlockPositionSource(blockEntity.getBlockPos());
  }

  @Override
  public PositionSource getListenerSource() {
    return this.positionSource;
  }

  @Override
  public int getListenerRadius() {
    return this.blockEntity.getPattern().getRadius();
  }

  @Override
  public boolean handleGameEvent(Level level, GameEvent event, Entity entity, BlockPos blockPos) {
    if (event == GameEvent.BLOCK_PLACE || event == GameEvent.BLOCK_DESTROY) {
      // Can't check immediately as these events are fired before the blocks are actually changed.
      this.blockEntity.enqueueEvaluation();
    }
    return false;
  }
}

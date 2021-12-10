package mods.railcraft.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public final class AdjacentBlockEntityCache {

  private static final int DELAY_MIN = 20;
  private static final int DELAY_MAX = 2400;
  private static final int DELAY_STEP = 2;
  private final Timer[] timer = new Timer[6];
  private final BlockEntity[] cache = new BlockEntity[6];
  private final int[] delay = new int[6];
  private final BlockEntity blockEntity;
  private final Set<ICacheListener> listeners = new LinkedHashSet<>();

  public AdjacentBlockEntityCache(BlockEntity blockEntity) {
    this.blockEntity = blockEntity;
    Arrays.fill(this.delay, DELAY_MIN);
    for (int i = 0; i < this.timer.length; i++) {
      this.timer[i] = new Timer();
    }
  }

  public void addListener(ICacheListener listener) {
    this.listeners.add(listener);
  }

  private @Nullable BlockEntity searchSide(Direction side) {
    return LevelUtil.getBlockEntityWeak(this.blockEntity.getLevel(),
        this.blockEntity.getBlockPos().relative(side));
  }

  public void refresh() {
    for (Direction side : Direction.values()) {
      this.getTileOnSide(side);
    }
  }

  public void purge() {
    Arrays.fill(this.cache, null);
    this.resetTimers();
    this.listeners.forEach(ICacheListener::purge);
  }

  public void resetTimers() {
    Arrays.fill(this.delay, DELAY_MIN);
    Arrays.stream(this.timer).forEach(Timer::reset);
  }

  protected void setTile(Direction side, @Nullable BlockEntity tile) {
    int s = side.ordinal();
    if (cache[s] != tile) {
      cache[s] = tile;
      changed(side, tile);
    }
  }

  private void changed(Direction side, @Nullable BlockEntity newTile) {
    listeners.forEach(l -> l.changed(side, newTile));
  }

  private boolean isInSameChunk(Direction side) {
    BlockPos pos = blockEntity.getBlockPos();
    BlockPos sidePos = pos.relative(side);
    return pos.getX() >> 4 == sidePos.getX() >> 4 && pos.getZ() >> 4 == sidePos.getZ() >> 4;
  }

  public Optional<BlockEntity> onSide(Direction side) {
    return Optional.ofNullable(getTileOnSide(side));
  }

  public @Nullable BlockEntity getTileOnSide(Direction side) {
    if (!isInSameChunk(side)) {
      BlockEntity tile = searchSide(side);
      changed(side, tile);
      return tile;
    }
    int s = side.ordinal();
    if (cache[s] != null)
      if (cache[s].isRemoved()
          || !MiscTools.areCoordinatesOnSide(blockEntity.getBlockPos(), cache[s].getBlockPos(),
              side))
        setTile(side, null);
      else
        return cache[s];

    if (timer[s].hasTriggered(blockEntity.getLevel(), delay[s])) {
      setTile(side, searchSide(side));
      if (cache[s] == null)
        incrementDelay(s);
      else
        delay[s] = DELAY_MIN;
    }

    return cache[s];
  }

  private void incrementDelay(int side) {
    delay[side] += DELAY_STEP;
    if (delay[side] > DELAY_MAX)
      delay[side] = DELAY_MAX;
  }

  public List<String> getDebugOutput() {
    List<String> debug = new ArrayList<>();
    debug.add("Neighbor Cache: " + Arrays.toString(cache));
    return debug;
  }

  public interface ICacheListener {

    void changed(Direction side, @Nullable BlockEntity newBlockEntity);

    void purge();
  }
}

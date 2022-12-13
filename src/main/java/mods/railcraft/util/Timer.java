package mods.railcraft.util;

import net.minecraft.world.level.Level;

/**
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class Timer {

  private long startTime = Long.MIN_VALUE;

  public boolean hasTriggered(Level level, int ticks) {
    long currentTime = level.getGameTime();
    if (currentTime >= (ticks + startTime) || startTime > currentTime) {
      startTime = currentTime;
      return true;
    }
    return false;
  }

  public void reset() {
    startTime = Long.MIN_VALUE;
  }
}

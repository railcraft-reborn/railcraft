package mods.railcraft.util;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

/**
 * Created by CovertJaguar on 7/26/2017 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class TimerBag<T> {

  private Object2IntMap<T> timers = new Object2IntOpenHashMap<>();
  private final int duration;

  public TimerBag(int duration) {
    this.duration = duration;
  }

  public boolean add(T object) {
    return this.timers.put(object, this.duration) <= 0;
  }

  public boolean contains(T object) {
    return this.timers.getOrDefault(object, 0) > 0;
  }

  public void tick() {
    this.timers.replaceAll((t, time) -> {
      if (time > 0)
        return --time;
      return 0;
    });
    this.timers.object2IntEntrySet().removeIf(entry -> entry.getIntValue() <= 0);
  }
}

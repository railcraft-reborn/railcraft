package mods.railcraft.util;

/**
 * Created by CovertJaguar on 6/7/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class EnumTools {

  public static <T extends Enum<T>> T next(T e, T[] values) {
    return values[(e.ordinal() + 1) % values.length];
  }

  public static <T extends Enum<T>> T previous(T e, T[] values) {
    return values[(e.ordinal() + values.length - 1) % values.length];
  }

  public static <T extends Enum<T>> T inverse(T e, T[] values) {
    return values[values.length - e.ordinal()];
  }

  public static <T extends Enum<T>> T random(T[] values) {
    return values[MiscTools.RANDOM.nextInt(values.length)];
  }

  public static <T extends Enum<T>> T fromOrdinal(int ordinal, T[] values) {
    if (ordinal < 0 || ordinal >= values.length)
      return values[0];
    return values[ordinal];
  }
}

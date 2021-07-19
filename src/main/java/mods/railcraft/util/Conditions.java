package mods.railcraft.util;

import java.util.function.BiPredicate;
import javax.annotation.Nullable;

/**
 * Utility for checking equality for json conditions.
 */
public final class Conditions {

  public static boolean check(@Nullable Boolean goal, boolean test) {
    return goal == null || goal == test;
  }

  public static <T> boolean check(@Nullable T goal, T test) {
    return goal == null || goal.equals(test);
  }

  public static <T> boolean check(@Nullable T goal, T test, BiPredicate<T, T> equality) {
    return goal == null || equality.test(goal, test);
  }

  private Conditions() {}
}

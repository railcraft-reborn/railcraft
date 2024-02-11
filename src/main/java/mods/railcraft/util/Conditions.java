package mods.railcraft.util;

import org.jetbrains.annotations.Nullable;

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

  private Conditions() {}
}

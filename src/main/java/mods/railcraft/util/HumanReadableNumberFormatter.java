package mods.railcraft.util;

import java.text.DecimalFormat;
import java.util.Locale;

public class HumanReadableNumberFormatter {

  private static final DecimalFormat SMALL_NUMBER_FORMATTER =
      (DecimalFormat) DecimalFormat.getInstance(Locale.ENGLISH);
  private static final DecimalFormat LARGE_NUMBER_FORMATTER =
      (DecimalFormat) DecimalFormat.getInstance(Locale.ENGLISH);
  private static final String[] SUFFIX = {"", "K", "M", "B"};

  static {
    SMALL_NUMBER_FORMATTER.applyPattern("#,##0.###");
    LARGE_NUMBER_FORMATTER.applyPattern("#,##0.#");
  }

  public static String format(double number) {
    return format(number, 0);
  }

  private static String format(double number, int iteration) {
    var prefix = number < 0.0 ? "-" : "";
    number = Math.abs(number);
    if (number < 1_000.0 || iteration == SUFFIX.length - 1) {
      var formatter = iteration > 0 ? LARGE_NUMBER_FORMATTER : SMALL_NUMBER_FORMATTER;
      var suffix = SUFFIX[iteration];
      if (!suffix.isEmpty()) {
        suffix = " " + suffix;
      }
      return prefix + formatter.format(number) + suffix;
    }
    return prefix + format(number / 1000.0, iteration + 1);
  }
}

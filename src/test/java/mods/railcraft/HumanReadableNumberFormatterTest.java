package mods.railcraft;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import mods.railcraft.util.HumanReadableNumberFormatter;

public class HumanReadableNumberFormatterTest {

  @ParameterizedTest(name = "{1} -> {0}")
  @CsvSource({
      "0, 0",
      "1, 1",
      "999, 999",
      "1 K, 1_000",
      "10 K, 10_000",
      "1 M, 1_000_000",
      "1 B, 1_000_000_000",
      "-1, -1",
      "-999, -999",
      "-1 K, -1_000",
      "-10 K, -10_000",
      "-1 M, -1_000_000",
      "-1 B, -1_000_000_000",
      "0.5, 0.5",
      "1 K, 1_000.567",
      "1 K, 1_000.9",
      "1 K, 1_001",
      "1.5 K, 1_499",
      "1.5 K, 1_500",
      "1.9 K, 1_900",
      "1.9 K, 1_901",
      "1.9 K, 1_950",
      "2 K, 1_951",
      "2 K, 1_999",
  })
  void readableNumber(String expected, double number) {
    Assertions.assertEquals(expected, HumanReadableNumberFormatter.format(number));
  }
}

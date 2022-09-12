package mods.railcraft.util;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamUtil {

  /**
   * Returns a sequential ordered {@code Integer} from {@code startInclusive}
   * (inclusive) to {@code endInclusive} (inclusive) by an incremental step of
   * {@code 1}.
   * <br>
   * Unlike {@code IntStream.rangeClosed} it works even if {@code endInclusive < startInclusive}
   */
  public static Stream<Integer> customRangeClosed(int startInclusive, int endInclusive) {
    if (startInclusive <= endInclusive) {
      return IntStream.rangeClosed(startInclusive, endInclusive).boxed();
    }

    return IntStream.rangeClosed(endInclusive, startInclusive)
        .boxed()
        .map(i -> endInclusive - i + startInclusive);
  }
}

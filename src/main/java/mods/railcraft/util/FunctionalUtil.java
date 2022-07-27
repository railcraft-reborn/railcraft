package mods.railcraft.util;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;

/**
 * Created by CovertJaguar on 8/28/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class FunctionalUtil {

  /**
   * Helper function to use when casting Streams.
   *
   * Works as both a mapper and a filter.
   *
   * Put it in a {@link Stream#flatMap(Function)} call.
   */
  public static <T, U> Function<T, Stream<U>> ofType(Class<U> clazz) {
    return t -> clazz.isInstance(t) ? Stream.of(clazz.cast(t)) : Stream.empty();
  }

  /**
   * This function exists because {@link Optional#orElse(Object)} won't accept super classes for
   * other.
   */
  public static <T1, T2 extends T1> T1 get(Optional<T2> obj, T1 orElse) {
    return obj.isPresent() ? obj.get() : orElse;
  }

  /**
   * Helper function to use when casting Optionals.
   *
   * Put it in a {@link Optional#map(Function)} call.
   *
   * If the Optional cannot be cast to the given class, it will return null resulting in the map
   * returning an empty Optional.
   */
  public static <T, U> Function<T, @Nullable U> ofTypeOrNull(Class<U> clazz) {
    return t -> clazz.isInstance(t) ? clazz.cast(t) : null;
  }

  public static <T> boolean notEqualOrEmpty(Optional<T> opt1, Optional<T> opt2) {
    return opt1.isPresent() && opt2.isPresent() && !opt1.equals(opt2);
  }
}

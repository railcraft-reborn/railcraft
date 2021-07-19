package mods.railcraft.util;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import com.google.common.collect.Lists;
import mods.railcraft.util.collections.StackKey;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

/**
 * Created by CovertJaguar on 9/9/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public final class Predicates {

  @SafeVarargs
  public static <T> Predicate<T> and(Predicate<? super T> predicate,
      Predicate<? super T>... predicates) {
    return new AndPredicate<>(Lists.asList(predicate, predicates));
  }

  public static <T> Predicate<T> and(Collection<? extends Predicate<? super T>> predicates) {
    return new AndPredicate<>(predicates);
  }

  private static class AndPredicate<T> implements Predicate<T> {
    private final Collection<? extends Predicate<? super T>> components;

    private AndPredicate(Collection<? extends Predicate<? super T>> predicates) {
      components = predicates;
    }

    @Override
    public boolean test(T t) {
      return components.stream().allMatch(p -> p.test(t));
    }
  }

  public static <T> Predicate<T> instanceOf(Class<? extends T> clazz) {
    return clazz::isInstance;
  }

  public static <T> Predicate<T> notInstanceOf(Class<? extends T> clazz) {
    return Predicates.<T>instanceOf(clazz).negate();
  }

  public static <T, O> Predicate<T> distinct(Function<? super T, O> keyFunction) {
    Map<O, Boolean> seen = new ConcurrentHashMap<>();
    return t -> seen.putIfAbsent(keyFunction.apply(t), Boolean.TRUE) == null;
  }

  public static Predicate<ItemStack> distinctStack() {
    return distinct(StackKey::make);
  }

  public static Predicate<BlockState> realBlock(IBlockReader world, BlockPos pos) {
    return state -> state != null && state.getBlock().isAir(state, world, pos);
  }

  public static <T> Predicate<T> alwaysTrue() {
    return t -> true; // No need to put in a field
  }

  public static <T> Predicate<T> alwaysFalse() {
    return t -> false;
  }
}

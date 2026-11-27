package mods.railcraft.world.level.block;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import com.google.common.collect.Maps;

/**
 * One value per {@link DecorativeBlock} material, modelled after
 * {@link net.minecraft.world.level.block.ColorCollection}. Unlike the vanilla record, it is backed
 * by the enum, so adding a material only needs a new {@link DecorativeBlock} constant.
 */
public final class DecorativeCollection<T> {

  public static final DecorativeCollection<DecorativeBlock> VALUES =
      create(Function.identity());
  public static final DecorativeCollection<String> NAMES =
      VALUES.map(DecorativeBlock::getSerializedName);

  private final Map<DecorativeBlock, T> values;

  private DecorativeCollection(Map<DecorativeBlock, T> values) {
    this.values = values;
  }

  public static <T> DecorativeCollection<T> create(Function<DecorativeBlock, T> factory) {
    var values = new EnumMap<DecorativeBlock, T>(DecorativeBlock.class);
    for (var type : DecorativeBlock.values()) {
      values.put(type, factory.apply(type));
    }
    return new DecorativeCollection<>(Maps.immutableEnumMap(values));
  }

  public static <A, B> void zipApply(DecorativeCollection<A> first,
      DecorativeCollection<B> second, BiConsumer<? super A, ? super B> action) {
    for (var type : DecorativeBlock.values()) {
      action.accept(first.pick(type), second.pick(type));
    }
  }

  public static <A, B, R> DecorativeCollection<R> zipMap(DecorativeCollection<A> first,
      DecorativeCollection<B> second, BiFunction<? super A, ? super B, ? extends R> mapper) {
    return create(type -> mapper.apply(first.pick(type), second.pick(type)));
  }

  public T pick(DecorativeBlock type) {
    return this.values.get(type);
  }

  public <R> DecorativeCollection<R> map(Function<? super T, ? extends R> mapper) {
    return create(type -> mapper.apply(this.pick(type)));
  }

  public void forEach(Consumer<? super T> action) {
    this.values.values().forEach(action);
  }

  public List<T> asList() {
    return List.copyOf(this.values.values());
  }
}

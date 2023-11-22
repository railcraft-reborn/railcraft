package mods.railcraft.util;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public sealed interface VariantSet<K extends Enum<K> & StringRepresentable, R, V extends R> {

  Supplier<? extends V> variantFor(K key);

  Collection<DeferredHolder<R, ? extends V>> variants();

  default Stream<? extends V> boundVariants() {
    return this.variants().stream().map(DeferredHolder::get);
  }

  void forEach(BiConsumer<K, DeferredHolder<R, ? extends V>> action);

  public static <K extends Enum<K> & StringRepresentable, R, V extends R> VariantSet<K, R, V> of(
      Class<K> keyType,
      DeferredRegister<R> deferredRegister,
      String nameSuffix,
      Supplier<V> factory) {
    Map<K, DeferredHolder<R, ? extends V>> variants = new EnumMap<>(keyType);
    for (var key : keyType.getEnumConstants()) {
      variants.put(key, deferredRegister.register(
          key.getSerializedName() + "_" + nameSuffix, factory));
    }
    return new MappedVariantSet<>(variants);
  }

  public static <K extends Enum<K> & StringRepresentable, R, V extends R, SR, SV extends SR> VariantSet<K, R, V> ofMapped(
      Class<K> keyType,
      DeferredRegister<R> deferredRegister,
      VariantSet<K, SR, SV> source,
      Function<? super SV, ? extends V> mapper) {
    Map<K, DeferredHolder<R, ? extends V>> variants = new EnumMap<>(keyType);
    source.forEach((key, value) -> variants.put(key,
        deferredRegister.register(
            value.getId().getPath(),
            () -> mapper.apply(value.get()))));
    return new MappedVariantSet<>(variants);
  }

  record MappedVariantSet<K extends Enum<K> & StringRepresentable, R, V extends R>(
      Map<K, DeferredHolder<R, ? extends V>> variantsByKey)
      implements VariantSet<K, R, V> {

    public MappedVariantSet(Map<K, DeferredHolder<R, ? extends V>> variantsByKey) {
      // Used over Map.copyOf as Guava provides enum optimization.
      this.variantsByKey = ImmutableMap.copyOf(variantsByKey);
    }

    @Override
    public Supplier<? extends V> variantFor(K key) {
      return this.variantsByKey.get(key);
    }

    @Override
    public Collection<DeferredHolder<R, ? extends V>> variants() {
      return this.variantsByKey.values();
    }

    @Override
    public void forEach(BiConsumer<K, DeferredHolder<R, ? extends V>> action) {
      this.variantsByKey.forEach(action);
    }
  }
}

package mods.railcraft.util;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class VariantRegistrar<K extends Enum<K> & StringRepresentable, V> {

  private final Class<K> keyType;
  private final Map<K, DeferredHolder<V, ? extends V>> variants;
  private final DeferredRegister<V> deferredRegister;

  public static <V extends Enum<V> & StringRepresentable, T> VariantRegistrar<V, T> from(
      Class<V> variantType, DeferredRegister<T> deferredRegister) {
    return new VariantRegistrar<>(variantType, deferredRegister);
  }

  private VariantRegistrar(Class<K> keyType, DeferredRegister<V> deferredRegister) {
    this.keyType = keyType;
    this.variants = new EnumMap<>(keyType);
    this.deferredRegister = deferredRegister;
  }

  public Supplier<? extends V> variantFor(K key) {
    return this.variants.get(key);
  }

  public Collection<DeferredHolder<V, ? extends V>> variants() {
    return this.variants.values();
  }

  public Stream<? extends V> resolveVariants() {
    return this.variants().stream().map(Supplier::get);
  }

  private void put(K key, DeferredHolder<V, ? extends V> registryObject) {
    this.variants.put(key, registryObject);
  }

  public void forEach(BiConsumer<K, DeferredHolder<? super V, ? extends V>> consumer) {
    this.variants.forEach(consumer);
  }

  public <S> VariantRegistrar<K, V> registerUsing(VariantRegistrar<K, S> other,
      Function<? super S, V> factory) {
    other.forEach((key, otherRegistryObject) -> {
      var registryObject = this.deferredRegister.register(
          otherRegistryObject.getId().getPath(),
          () -> factory.apply(otherRegistryObject.get()));
      this.put(key, registryObject);
    });
    return this;
  }

  public VariantRegistrar<K, V> register(String name, Supplier<V> factory) {
    for (K key : this.keyType.getEnumConstants()) {
      var prefix = key.getSerializedName() + "_";
      var registryObject = this.deferredRegister.register(prefix + name, factory);
      this.put(key, registryObject);
    }
    return this;
  }
}

package mods.railcraft.util;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryObject;

public class VariantRegistrar<K extends Enum<K> & StringRepresentable, V> {

  private final Class<K> keyType;
  private final Map<K, RegistryObject<? extends V>> variants;
  private final DeferredRegister<? super V> deferredRegister;

  public static <V extends Enum<V> & StringRepresentable, T> VariantRegistrar<V, T> from(
      Class<V> variantType, DeferredRegister<? super T> deferredRegister) {
    return new VariantRegistrar<>(variantType, deferredRegister);
  }

  private VariantRegistrar(Class<K> keyType, DeferredRegister<? super V> deferredRegister) {
    this.keyType = keyType;
    this.variants = new EnumMap<>(keyType);
    this.deferredRegister = deferredRegister;
  }

  public RegistryObject<? extends V> variantFor(K key) {
    return this.variants.get(key);
  }

  public Collection<RegistryObject<? extends V>> variants() {
    return this.variants.values();
  }

  public Stream<? extends V> resolveVariants() {
    return this.variants().stream().map(RegistryObject::get);
  }

  private void put(K key, RegistryObject<? extends V> registryObject) {
    this.variants.put(key, registryObject);
  }

  public void forEach(BiConsumer<K, RegistryObject<? extends V>> consumer) {
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

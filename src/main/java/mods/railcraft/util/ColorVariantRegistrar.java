package mods.railcraft.util;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

// Based on an idea from Refined Storage
public class ColorVariantRegistrar<T> {

  private final Map<DyeColor, RegistryObject<? extends T>> variants =
      new EnumMap<>(DyeColor.class);

  private DeferredRegister<? super T> deferredRegister;

  public ColorVariantRegistrar(DeferredRegister<? super T> deferredRegister) {
    this.deferredRegister = deferredRegister;
  }

  public RegistryObject<? extends T> variantFor(DyeColor color) {
    return this.variants.get(color);
  }

  public Collection<RegistryObject<? extends T>> registryObjects() {
    return this.variants.values();
  }

  public Stream<? extends T> resolveValues() {
    return this.registryObjects().stream().map(RegistryObject::get);
  }

  public void put(DyeColor color, RegistryObject<? extends T> object) {
    this.variants.put(color, object);
  }

  public void forEach(BiConsumer<DyeColor, RegistryObject<? extends T>> consumer) {
    this.variants.forEach(consumer);
  }

  public <V> void registerUsing(ColorVariantRegistrar<V> colorMap, Function<? super V, T> factory) {
    colorMap.forEach((color, block) -> {
      var registryObject =
          this.deferredRegister.register(block.getId().getPath(), () -> factory.apply(block.get()));
      this.put(color, registryObject);
    });
  }

  public void register(String name, Supplier<T> factory) {
    for (var color : DyeColor.values()) {
      var prefix = color + "_";
      var block = this.deferredRegister.register(prefix + name, factory);
      this.put(color, block);
    }
  }
}


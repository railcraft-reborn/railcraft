package mods.railcraft.util;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public sealed interface VariantSet<K extends Enum<K> & StringRepresentable, R, V extends R> {

  DeferredHolder<R, ? extends V> variantFor(K key);

  Collection<DeferredHolder<R, ? extends V>> variants();

  default Stream<? extends V> boundVariants() {
    return this.variants().stream().map(DeferredHolder::get);
  }

  void forEach(BiConsumer<K, DeferredHolder<R, ? extends V>> action);

  static <K extends Enum<K> & StringRepresentable, V extends Block> VariantSet<K, Block, V> of(
      Class<K> keyType,
      DeferredRegister.Blocks deferredRegister,
      String nameTemplate,
      BiFunction<BlockBehaviour.Properties, K, V> func,
      BlockBehaviour.Properties properties) {
    Map<K, DeferredHolder<Block, ? extends V>> variants = new EnumMap<>(keyType);
    for (var key : keyType.getEnumConstants()) {
      var name = String.format(nameTemplate, key.getSerializedName());
      variants.put(key, deferredRegister.registerBlock(name, p -> func.apply(p, key), properties));
    }
    return new MappedVariantSet<>(variants);
  }

  static <K extends Enum<K> & StringRepresentable, V extends Block> VariantSet<K, Block, V> of(
      Class<K> keyType,
      DeferredRegister.Blocks deferredRegister,
      String nameTemplate,
      Function<BlockBehaviour.Properties, V> func,
      BlockBehaviour.Properties properties) {
    Map<K, DeferredHolder<Block, ? extends V>> variants = new EnumMap<>(keyType);
    for (var key : keyType.getEnumConstants()) {
      var name = String.format(nameTemplate, key.getSerializedName());
      variants.put(key, deferredRegister.registerBlock(name, func, properties));
    }
    return new MappedVariantSet<>(variants);
  }

  static <K extends Enum<K> & StringRepresentable, V extends Item, SR, SV extends SR> VariantSet<K, Item, V> ofMapped(
      Class<K> keyType,
      DeferredRegister.Items deferredRegister,
      VariantSet<K, SR, SV> source,
      BiFunction<Item.Properties, ? super SV, ? extends V> mapper) {
    Map<K, DeferredHolder<Item, ? extends V>> variants = new EnumMap<>(keyType);
    source.forEach((key, value) -> {
      variants.put(key, deferredRegister.registerItem(value.getId().getPath(),
          properties -> mapper.apply(properties, value.get())));
    });
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
    public DeferredHolder<R, ? extends V> variantFor(K key) {
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

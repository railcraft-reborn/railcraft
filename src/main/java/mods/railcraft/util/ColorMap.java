package mods.railcraft.util;

import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

//Based on an idea from Refined Storage
public class ColorMap<T> {
  private final Map<DyeColor, RegistryObject<T>> map = new EnumMap<>(DyeColor.class);

  private DeferredRegister<Item> itemRegister;
  private DeferredRegister<Block> blockRegister;
  private List<Runnable> lateRegistration;

  public ColorMap(DeferredRegister<Block> blockRegister) {
    this.blockRegister = blockRegister;
  }

  public ColorMap(DeferredRegister<Item> itemRegister, List<Runnable> lateRegistration) {
    this.itemRegister = itemRegister;
    this.lateRegistration = lateRegistration;
  }

  public RegistryObject<T> get(DyeColor color) {
    return map.get(color);
  }

  public Collection<RegistryObject<T>> values() {
    return map.values();
  }

  public void put(DyeColor color, RegistryObject<T> object) {
    map.put(color, object);
  }

  public void forEach(BiConsumer<DyeColor, RegistryObject<T>> consumer) {
    map.forEach(consumer);
  }

  public Block[] getBlocks() {
    return map.values().stream().map(RegistryObject::get).toArray(Block[]::new);
  }

  public <S extends Block> void registerBlocks(String name, Supplier<S> blockFactory) {
    for (DyeColor color : DyeColor.values()) {
      String prefix = color + "_";
      RegistryObject<S> block = blockRegister.register(prefix + name, blockFactory);
      put(color, (RegistryObject<T>) block);
    }
  }

  public <S extends Block> void registerItemsFromBlocks(ColorMap<S> blockMap, CreativeModeTab tab) {
    lateRegistration.add(() -> {
      blockMap.forEach((color, block) -> {
        put(color, registerBlockItemFor(block, tab));
      });
    });
  }

  private <S extends Block> RegistryObject<T> registerBlockItemFor(RegistryObject<S> block, CreativeModeTab tab) {
    return (RegistryObject<T>) itemRegister.register(
      block.getId().getPath(),
      () -> new BlockItem(
        block.get(),
        new Item.Properties().tab(tab)
      )
    );
  }
}


package mods.railcraft.util.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import com.google.common.collect.ForwardingMap;
import mods.railcraft.api.container.manipulator.ContainerManipulator;
import mods.railcraft.util.ItemStackKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public final class ContainerManifest
    extends ForwardingMap<ItemStackKey, ContainerManifest.ManifestEntry> {

  private final Map<ItemStackKey, ManifestEntry> entries = new HashMap<>();

  private ContainerManifest() {}

  @Override
  protected Map<ItemStackKey, ManifestEntry> delegate() {
    return this.entries;
  }

  public int count(ItemStackKey key) {
    var entry = get(key);
    if (entry == null) {
      return 0;
    }
    return entry.count();
  }

  public Stream<ItemStack> streamValueStacks() {
    return this.entries.values().stream().flatMap(ManifestEntry::stream);
  }

  public Stream<ItemStack> streamKeyStacks() {
    return this.entries.keySet().stream().map(ItemStackKey::copyStack);
  }

  public List<ItemStack> keyStacks() {
    return streamKeyStacks().collect(Collectors.toList());
  }

  private static ManifestEntry compute(ItemStackKey key,
      @Nullable ContainerManifest.ManifestEntry entry, ItemStack stack) {
    if (entry == null) {
      entry = new ManifestEntry(key);
    }
    entry.stacks.add(stack.copy());
    return entry;
  }

  /**
   * Returns an {@link ContainerManifest} that lists the total number of each type of item in the
   * container.
   *
   * @param containers - the containers to generate the manifest for
   * @return A {@code Multiset} that lists how many of each item is in the inventories
   */

  public static ContainerManifest create(ContainerManipulator<?> containers) {
    var manifest = new ContainerManifest();
    containers.streamItems().forEach(stack -> {
      var key = ItemStackKey.make(stack);
      manifest.compute(key, (k, v) -> compute(k, v, stack));
    });
    return manifest;
  }

  /**
   * Returns an {@link ContainerManifest} that lists the total number of each type of item in the
   * container.
   *
   * @param containers - the containers to generate the manifest for
   * @param keys - the items to list.
   * @return A {@code Multiset} that lists how many of each item is in the inventories
   */

  public static ContainerManifest create(ContainerManipulator<?> containers,
      Collection<ItemStackKey> keys) {
    var manifest = new ContainerManifest();
    for (var filterKey : keys) {
      var filter = StackFilter.anyMatch(filterKey.stack());
      containers.streamItems()
          .filter(filter)
          .forEach(stack -> manifest.compute(filterKey, (k, v) -> compute(k, v, stack)));
    }
    return manifest;
  }

  public static ContainerManifest create(Container container, Collection<ItemStackKey> keys) {
    var manifest = new ContainerManifest();
    for (var filterKey : keys) {
      var filter = StackFilter.anyMatch(filterKey.stack());
      IntStream.range(0, container.getContainerSize())
          .mapToObj(container::getItem)
          .filter(filter)
          .forEach(stack -> manifest.compute(filterKey, (k, v) -> compute(k, v, stack)));
    }
    return manifest;
  }

  public static class ManifestEntry {

    private final ItemStackKey key;
    final List<ItemStack> stacks = new ArrayList<>();

    public ManifestEntry(ItemStackKey key) {
      this.key = key;
    }

    public ItemStackKey key() {
      return key;
    }

    public int count() {
      return stacks.stream().mapToInt(ItemStack::getCount).sum();
    }

    public List<ItemStack> stacks() {
      return Collections.unmodifiableList(stacks);
    }

    public Stream<ItemStack> stream() {
      return stacks.stream();
    }
  }
}

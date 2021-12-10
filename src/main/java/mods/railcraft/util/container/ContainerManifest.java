package mods.railcraft.util.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import com.google.common.collect.ForwardingMap;
import mods.railcraft.util.collections.StackKey;
import mods.railcraft.util.container.filters.StackFilters;
import net.minecraft.world.item.ItemStack;

/**
 * Created by CovertJaguar on 6/22/2017 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public final class ContainerManifest
    extends ForwardingMap<StackKey, ContainerManifest.ManifestEntry> {

  private final Map<StackKey, ManifestEntry> entries = new HashMap<>();

  private ContainerManifest() {}

  @Override
  protected Map<StackKey, ManifestEntry> delegate() {
    return this.entries;
  }

  public int count(StackKey key) {
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
    return this.entries.keySet().stream().map(StackKey::get);
  }

  public List<ItemStack> keyStacks() {
    return streamKeyStacks().collect(Collectors.toList());
  }

  private static ManifestEntry compute(StackKey key,
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

  public static ContainerManifest create(ContainerManipulator containers) {
    var manifest = new ContainerManifest();
    containers.streamStacks().forEach(stack -> {
      var key = StackKey.make(stack);
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

  public static ContainerManifest create(ContainerManipulator containers,
      Collection<StackKey> keys) {
    var manifest = new ContainerManifest();
    for (var filterKey : keys) {
      var filter = StackFilters.anyMatch(filterKey.get());
      containers.streamStacks()
          .filter(filter)
          .forEach(stack -> manifest.compute(filterKey, (k, v) -> compute(k, v, stack)));
    }
    return manifest;
  }

  public static class ManifestEntry {

    private final StackKey key;
    final List<ItemStack> stacks = new ArrayList<>();

    public ManifestEntry(StackKey key) {
      this.key = key;
    }

    public StackKey key() {
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

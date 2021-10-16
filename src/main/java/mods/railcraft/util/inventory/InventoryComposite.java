package mods.railcraft.util.inventory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.google.common.collect.ForwardingList;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.NonNullList;

/**
 * Primary interface for inventories of all types.
 *
 * Supports treating multiple inventories as a single object.
 *
 * Created by CovertJaguar on 5/28/2017 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public final class InventoryComposite extends ForwardingList<InventoryAdaptor>
    implements IInventoryComposite {

  private final List<InventoryAdaptor> list = NonNullList.create();

  private InventoryComposite() {}

  private InventoryComposite(InventoryAdaptor... objects) {
    addAll(Arrays.asList(objects));
  }

  @Override
  protected List<InventoryAdaptor> delegate() {
    return this.list;
  }

  @Override
  public Iterator<InventoryAdaptor> adaptors() {
    return this.iterator();
  }

  @Override
  public Iterable<InventoryAdaptor> iterable() {
    return this;
  }

  public static InventoryComposite of(InventoryAdaptor... objects) {
    return new InventoryComposite(objects);
  }

  public static InventoryComposite of(Collection<InventoryAdaptor> objects) {
    return objects.stream()
        .filter(Objects::nonNull)
        .collect(Collectors.toCollection(InventoryComposite::new));
  }

  public static InventoryComposite of(InventoryAdaptor inv) {
    Objects.requireNonNull(inv);
    return new InventoryComposite(inv);
  }

  public boolean add(IInventory inv) {
    return add(InventoryAdaptor.of(inv));
  }

  public static InventoryComposite create() {
    return new InventoryComposite();
  }

  @Override
  public Stream<InventoryAdaptor> stream() {
    return IInventoryComposite.super.stream();
  }
}

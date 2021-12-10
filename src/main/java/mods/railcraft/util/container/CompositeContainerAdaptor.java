package mods.railcraft.util.container;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.google.common.collect.ForwardingList;
import net.minecraft.world.Container;
import net.minecraft.core.NonNullList;

/**
 * Primary interface for inventories of all types.
 *
 * Supports treating multiple inventories as a single object.
 *
 * Created by CovertJaguar on 5/28/2017 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public final class CompositeContainerAdaptor extends ForwardingList<ContainerAdaptor>
    implements CompositeContainer {

  private final List<ContainerAdaptor> list = NonNullList.create();

  private CompositeContainerAdaptor() {}

  private CompositeContainerAdaptor(ContainerAdaptor... objects) {
    addAll(Arrays.asList(objects));
  }

  @Override
  protected List<ContainerAdaptor> delegate() {
    return this.list;
  }

  @Override
  public Iterator<ContainerAdaptor> adaptors() {
    return this.iterator();
  }

  @Override
  public Iterable<ContainerAdaptor> iterable() {
    return this;
  }

  public static CompositeContainerAdaptor of(ContainerAdaptor... objects) {
    return new CompositeContainerAdaptor(objects);
  }

  public static CompositeContainerAdaptor of(Collection<ContainerAdaptor> objects) {
    return objects.stream()
        .filter(Objects::nonNull)
        .collect(Collectors.toCollection(CompositeContainerAdaptor::new));
  }

  public static CompositeContainerAdaptor of(ContainerAdaptor inv) {
    Objects.requireNonNull(inv);
    return new CompositeContainerAdaptor(inv);
  }

  public boolean add(Container inv) {
    return add(ContainerAdaptor.of(inv));
  }

  public static CompositeContainerAdaptor create() {
    return new CompositeContainerAdaptor();
  }

  @Override
  public Stream<ContainerAdaptor> stream() {
    return CompositeContainer.super.stream();
  }
}

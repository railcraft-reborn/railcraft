package mods.railcraft.util.container;

import java.util.List;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.container.manipulator.ContainerManipulator;
import mods.railcraft.api.container.manipulator.ContainerSlotAccessor;
import mods.railcraft.api.container.manipulator.ModifiableSlotAccessor;
import mods.railcraft.world.module.ModuleProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * An extension of {@link SimpleContainer} with callback support, implementation of
 * {@link ContainerManipulator} and maintains item indices when persisting.
 * 
 * @author Sm0keySa1m0n
 */
public class AdvancedContainer extends SimpleContainer
    implements ContainerManipulator<ModifiableSlotAccessor> {

  private final List<ModifiableSlotAccessor> slots;

  @Nullable
  private Listener listener;
  private int maxStackSize = 64;

  public AdvancedContainer(int size) {
    super(size);
    this.slots = ContainerSlotAccessor.createSlots(this, 0, size).toList();
  }

  @Override
  public Stream<ModifiableSlotAccessor> stream() {
    return this.slots.stream();
  }

  public AdvancedContainer listener(Container container) {
    return this.listener(new ContainerCallback(container));
  }

  public AdvancedContainer listener(ModuleProvider moduleProvider) {
    return this.listener(new ModuleProviderCallback(moduleProvider));
  }

  public AdvancedContainer listener(Listener callback) {
    this.listener = callback;
    this.addListener(callback);
    return this;
  }

  public AdvancedContainer phantom() {
    this.maxStackSize = 127;
    return this;
  }

  public void setMaxStackSize(int maxStackSize) {
    this.maxStackSize = maxStackSize;
  }

  @Override
  public int getMaxStackSize() {
    return this.maxStackSize;
  }

  @Override
  public boolean stillValid(Player player) {
    return this.listener == null || this.listener.stillValid(player);
  }

  @Override
  public void startOpen(Player player) {
    if (this.listener != null) {
      this.listener.startOpen(player);
    }
  }

  @Override
  public void stopOpen(Player player) {
    if (this.listener != null) {
      this.listener.stopOpen(player);
    }
  }

  @Override
  public void fromTag(ListTag tag) {
    for (int i = 0; i < tag.size(); ++i) {
      var slotTag = tag.getCompound(i);
      this.setItem(slotTag.getInt("index"), ItemStack.of(slotTag));
    }
  }

  @Override
  public ListTag createTag() {
    var tag = new ListTag();
    for (int i = 0; i < this.getContainerSize(); ++i) {
      var slotTag = new CompoundTag();
      slotTag.putInt("index", i);
      this.getItem(i).save(slotTag);
      tag.add(slotTag);
    }
    return tag;
  }

  public static AdvancedContainer copyOf(Container original) {
    var copy = new AdvancedContainer(original.getContainerSize());
    for (int i = 0; i < original.getContainerSize(); i++) {
      var itemStack = original.getItem(i);
      if (!itemStack.isEmpty()) {
        copy.setItem(i, itemStack.copy());
      }
    }
    return copy;
  }

  public interface Listener extends ContainerListener {

    default boolean stillValid(Player player) {
      return true;
    }

    default void startOpen(Player player) {}

    default void stopOpen(Player player) {}
  }

  public static class ContainerCallback implements Listener {

    private final Container container;

    public ContainerCallback(Container container) {
      this.container = container;
    }

    @Override
    public boolean stillValid(Player player) {
      return this.container.stillValid(player);
    }

    @Override
    public void startOpen(Player player) {
      this.container.startOpen(player);
    }

    @Override
    public void stopOpen(Player player) {
      this.container.stopOpen(player);
    }

    @Override
    public void containerChanged(Container container) {
      this.container.setChanged();
    }
  }

  public static class ModuleProviderCallback implements Listener {

    private final ModuleProvider moduleProvider;

    public ModuleProviderCallback(ModuleProvider moduleProvider) {
      this.moduleProvider = moduleProvider;
    }

    @Override
    public boolean stillValid(Player player) {
      return this.moduleProvider.isStillValid(player);
    }

    @Override
    public void containerChanged(Container container) {
      this.moduleProvider.save();
    }
  }
}

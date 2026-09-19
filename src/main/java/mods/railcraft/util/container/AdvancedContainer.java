package mods.railcraft.util.container;

import java.util.List;
import java.util.stream.Stream;
import org.jspecify.annotations.Nullable;
import mods.railcraft.api.container.manipulator.ContainerManipulator;
import mods.railcraft.api.container.manipulator.ContainerSlotAccessor;
import mods.railcraft.api.container.manipulator.ModifiableSlotAccessor;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import mods.railcraft.world.module.ModuleProvider;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

/**
 * An extension of {@link SimpleContainer} with callback support, implementation of
 * {@link ContainerManipulator} and maintains item indices when persisting.
 *
 * @author Sm0keySa1m0n
 */
public class AdvancedContainer extends SimpleContainer
    implements ContainerManipulator<ModifiableSlotAccessor>, ValueIOSerializable {

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

  public AdvancedContainer listener(RailcraftBlockEntity blockEntity) {
    return this.listener(new BlockEntityCallback(blockEntity));
  }

  public AdvancedContainer listener(Listener callback) {
    this.listener = callback;
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
  public void setChanged() {
    if (this.listener != null) {
      this.listener.containerChanged(this);
    }
  }

  @Override
  public boolean stillValid(Player player) {
    return this.listener == null || this.listener.stillValid(player);
  }

  @Override
  public void startOpen(ContainerUser user) {
    if (this.listener != null) {
      this.listener.startOpen(user);
    }
  }

  @Override
  public void stopOpen(ContainerUser user) {
    if (this.listener != null) {
      this.listener.stopOpen(user);
    }
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

  @Override
  public void serialize(ValueOutput valueOutput) {
    ContainerHelper.saveAllItems(valueOutput, this.getItems(), false);
  }

  @Override
  public void deserialize(ValueInput valueInput) {
    var tempItems = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
    ContainerHelper.loadAllItems(valueInput, tempItems);
    for (int i = 0; i < tempItems.size(); i++) {
      this.setItem(i, tempItems.get(i));
    }
  }

  @FunctionalInterface
  public interface Listener {

    void containerChanged(Container container);

    default boolean stillValid(Player player) {
      return true;
    }

    default void startOpen(ContainerUser user) {}

    default void stopOpen(ContainerUser user) {}
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
    public void startOpen(ContainerUser user) {
      this.container.startOpen(user);
    }

    @Override
    public void stopOpen(ContainerUser user) {
      this.container.stopOpen(user);
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

  public static class BlockEntityCallback implements Listener {

    private final RailcraftBlockEntity blockEntity;

    public BlockEntityCallback(RailcraftBlockEntity blockEntity) {
      this.blockEntity = blockEntity;
    }

    @Override
    public boolean stillValid(Player player) {
      return this.blockEntity.isStillValid(player);
    }

    @Override
    public void containerChanged(Container container) {
      this.blockEntity.setChanged();
    }
  }
}

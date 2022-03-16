package mods.railcraft.util.container;

import javax.annotation.Nullable;
import mods.railcraft.util.container.manipulator.ContainerManipulator;
import mods.railcraft.util.container.manipulator.VanillaContainerManipulator;
import mods.railcraft.world.level.block.entity.module.ModuleProvider;
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
public class AdvancedContainer extends SimpleContainer implements VanillaContainerManipulator {

  public static final AdvancedContainer EMPTY = new AdvancedContainer(0);

  @Nullable
  private Callback callback;
  private int maxStackSize = 64;

  public AdvancedContainer(int size) {
    super(size);
  }

  @Override
  public Container getContainer() {
    return this;
  }

  public AdvancedContainer callback(Container container) {
    return this.callback(new ContainerCallback(container));
  }

  public AdvancedContainer callback(ModuleProvider moduleProvider) {
    return this.callback(new ModuleProviderCallback(moduleProvider));
  }

  public AdvancedContainer callback(Callback callback) {
    this.callback = callback;
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
    return this.callback == null || this.callback.stillValid(player);
  }

  @Override
  public void startOpen(Player player) {
    if (this.callback != null) {
      this.callback.startOpen(player);
    }
  }

  @Override
  public void stopOpen(Player player) {
    if (this.callback != null) {
      this.callback.stopOpen(player);
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

  public interface Callback extends ContainerListener {

    default boolean stillValid(Player player) {
      return true;
    }

    default void startOpen(Player player) {}

    default void stopOpen(Player player) {}
  }

  public static class ContainerCallback implements Callback {

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

  public static class ModuleProviderCallback implements Callback {

    private final ModuleProvider moduleProvider;

    public ModuleProviderCallback(ModuleProvider moduleProvider) {
      this.moduleProvider = moduleProvider;
    }

    @Override
    public boolean stillValid(Player player) {
      return this.moduleProvider.stillValid(player);
    }

    @Override
    public void containerChanged(Container container) {
      this.moduleProvider.save();
    }
  }
}

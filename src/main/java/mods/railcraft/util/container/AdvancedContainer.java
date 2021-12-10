package mods.railcraft.util.container;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.google.common.collect.Iterators;
import mods.railcraft.util.Optionals;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Creates a standalone instance of {@link Container}
 * <p/>
 * Useful for hiding parts of a container from outsiders.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class AdvancedContainer extends SimpleContainer
    implements CompositeContainer, INBTSerializable<ListTag> {

  public static final AdvancedContainer EMPTY = new AdvancedContainer(0);

  @Nullable
  private Callback callback;
  private int maxStackSize = 64;

  public AdvancedContainer(int size) {
    super(size);
  }

  public AdvancedContainer callbackContainer(Container callback) {
    return callback(new CallbackInv(callback));
  }

  public AdvancedContainer callbackBlockEntity(RailcraftBlockEntity callback) {
    return callback(new CallbackTile(() -> callback));
  }

  public AdvancedContainer callbackBlockEntity(Supplier<RailcraftBlockEntity> callback) {
    return callback(new CallbackTile(callback));
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

  @Override
  public int slotCount() {
    return getContainerSize();
  }

  public void setMaxStackSize(int maxStackSize) {
    this.maxStackSize = maxStackSize;
  }

  @Override
  public int getMaxStackSize() {
    return this.maxStackSize;
  }

  @Override
  public boolean stillValid(Player PlayerEntity) {
    return this.callback == null || callback.stillValid(PlayerEntity);
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
  public ListTag serializeNBT() {
    return ContainerTools.writeInventory(this);
  }

  @Override
  public void deserializeNBT(ListTag tag) {
    for (byte i = 0; i < tag.size(); i++) {
      CompoundTag slotTag = tag.getCompound(i);
      int slot = slotTag.getByte(ContainerTools.TAG_SLOT);
      if (slot >= 0 && slot < this.getContainerSize()) {
        this.setItem(slot, ItemStack.of(slotTag));
      }
    }
  }

  public abstract static class Callback implements ContainerListener {

    public boolean stillValid(Player player) {
      return true;
    }

    public void startOpen(Player player) {}

    public void stopOpen(Player player) {}

    public String getName() {
      return "Standalone";
    }

    public boolean hasCustomName() {
      return false;
    }
  }

  public static class CallbackInv extends Callback {

    private final Container inv;

    public CallbackInv(Container inv) {
      this.inv = inv;
    }

    @Override
    public boolean stillValid(Player player) {
      return inv.stillValid(player);
    }

    @Override
    public void startOpen(Player player) {
      inv.startOpen(player);
    }

    @Override
    public void stopOpen(Player player) {
      inv.stopOpen(player);
    }

    @Override
    public void containerChanged(Container invBasic) {
      inv.setChanged();
    }
  }

  public static class CallbackTile extends Callback {

    private final Supplier<RailcraftBlockEntity> tile;

    public CallbackTile(Supplier<RailcraftBlockEntity> tile) {
      this.tile = tile;
    }

    public Optional<RailcraftBlockEntity> tile() {
      return Optional.ofNullable(tile.get());
    }

    @Override
    public boolean stillValid(Player player) {
      return Optionals.test(tile(), t -> RailcraftBlockEntity.stillValid(t, player));
    }

    @Override
    public void containerChanged(Container invBasic) {
      tile().ifPresent(BlockEntity::setChanged);
    }
  }

  @Override
  public Iterator<ContainerAdaptor> adaptors() {
    return Iterators.singletonIterator(ContainerAdaptor.of(this));
  }
}

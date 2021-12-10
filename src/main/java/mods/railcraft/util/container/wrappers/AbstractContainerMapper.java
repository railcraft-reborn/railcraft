package mods.railcraft.util.container.wrappers;

import net.minecraft.world.entity.player.Player;
import mods.railcraft.util.container.CompositeContainer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

/**
 * Created by CovertJaguar on 3/6/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public abstract class AbstractContainerMapper implements Container, CompositeContainer {

  private final Container container;
  protected boolean checkItems;

  protected AbstractContainerMapper(Container container) {
    this(container, true);
  }

  protected AbstractContainerMapper(Container container, boolean checkItems) {
    this.container = container;
    this.checkItems = checkItems;
  }

  public Container getContainer() {
    return this.container;
  }

  @Override
  public int getContainerSize() {
    return this.container.getContainerSize();
  }

  @Override
  public ItemStack getItem(int slot) {
    return this.container.getItem(slot);
  }

  @Override
  public ItemStack removeItem(int slot, int amount) {
    return this.container.removeItem(slot, amount);
  }

  @Override
  public ItemStack removeItemNoUpdate(int slot) {
    return this.container.removeItemNoUpdate(slot);
  }

  @Override
  public void setItem(int slot, ItemStack item) {
    this.container.setItem(slot, item);
  }

  @Override
  public int getMaxStackSize() {
    return this.container.getMaxStackSize();
  }

  @Override
  public void setChanged() {
    this.container.setChanged();
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
  public boolean canPlaceItem(int slot, ItemStack item) {
    return !this.checkItems || this.container.canPlaceItem(slot, item);
  }

  @Override
  public void clearContent() {
    this.container.clearContent();
  }

  public boolean checkItems() {
    return this.checkItems;
  }

  @Override
  public int slotCount() {
    return this.getContainerSize();
  }

  @Override
  public boolean isEmpty() {
    return this.container.isEmpty();
  }
}

package mods.railcraft.util.inventory.wrappers;

import mods.railcraft.util.inventory.IInventoryComposite;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Created by CovertJaguar on 3/6/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public abstract class InvWrapperBase implements IInventory, IInventoryComposite {

  private final IInventory inv;
  protected boolean checkItems;

  protected InvWrapperBase(IInventory inv) {
    this(inv, true);
  }

  protected InvWrapperBase(IInventory inv, boolean checkItems) {
    this.inv = inv;
    this.checkItems = checkItems;
  }

  public IInventory getBaseInventory() {
    return inv;
  }

  @Override
  public int getContainerSize() {
    return inv.getContainerSize();
  }

  @Override
  public ItemStack getItem(int slot) {
    return inv.getItem(slot);
  }

  @Override
  public ItemStack removeItem(int slot, int amount) {
    return inv.removeItem(slot, amount);
  }

  @Override
  public ItemStack removeItemNoUpdate(int slot) {
    return inv.removeItemNoUpdate(slot);
  }

  @Override
  public void setItem(int slot, ItemStack itemstack) {
    inv.setItem(slot, itemstack);
  }

  @Override
  public int getMaxStackSize() {
    return inv.getMaxStackSize();
  }

  @Override
  public void setChanged() {
    inv.setChanged();
  }

  @Override
  public boolean stillValid(PlayerEntity entityplayer) {
    return inv.stillValid(entityplayer);
  }

  @Override
  public void startOpen(PlayerEntity player) {
    inv.startOpen(player);
  }

  @Override
  public void stopOpen(PlayerEntity player) {
    inv.stopOpen(player);
  }

  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    return !checkItems || inv.canPlaceItem(slot, stack);
  }

  @Override
  public void clearContent() {
    inv.clearContent();
  }

  public boolean checkItems() {
    return checkItems;
  }

  @Override
  public int slotCount() {
    return this.getContainerSize();
  }

  @Override
  public boolean isEmpty() {
    return inv.isEmpty();
  }
}

package mods.railcraft.crafting;

import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.item.ItemStack;

/**
 * Extension of CraftResultInventory implementing:
 * * blockItemPull() - Disallows the item onto being pulled out of the container
 * * unblockItemPull() - Unblocks the item
 * @author LetterN (https://github.com/LetterN)
 */
public class CraftResultHold extends CraftResultInventory{
  private boolean blocked = false;

  /**
   * Disallows the items onto being pulled out of this result inventory.
   */
  public void blockItemPull() {
    this.blocked = true;
  }

  /**
   * Allows the items onto being pulled out of this inventory.
   */
  public void unblockItemPull() {
    this.blocked = false;
  }

  @Override
  public ItemStack removeItem(int containerID, int slotID) {
    return blocked ? ItemStack.EMPTY : super.removeItem(containerID, slotID);
  }

  @Override
  public ItemStack removeItemNoUpdate(int slotID) {
    return blocked ? ItemStack.EMPTY : super.removeItemNoUpdate(slotID);
  }

}

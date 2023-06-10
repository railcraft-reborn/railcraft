package mods.railcraft.util.container.manipulator;

import java.util.function.Predicate;
import mods.railcraft.util.container.ContainerTools;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * This interface extends IInvSlot by allowing you to modify a slot directly. This is only valid on
 * inventories backed by IInventory.
 * <p/>
 * <p/>
 * Created by CovertJaguar on 3/16/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public interface ModifiableSlotAccessor extends SlotAccessor {

  /**
   * Sets the current ItemStack in the slot.
   */
  void setItem(ItemStack itemStack);

  default ItemStack clear() {
    var itemStack = this.item();
    this.setItem(ItemStack.EMPTY);
    return itemStack;
  }

  default void dropIfInvalid(Level level, BlockPos blockPos) {
    this.drop(level, blockPos, this::isValid);
  }

  default void drop(Level level, BlockPos blockPos, Predicate<ItemStack> predicate) {
    var item = this.item();
    if (!item.isEmpty() && !predicate.test(item)) {
      this.clear();
      ContainerTools.dropItem(item, level, blockPos);
    }
  }
}

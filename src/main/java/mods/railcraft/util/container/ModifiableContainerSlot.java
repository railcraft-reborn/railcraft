package mods.railcraft.util.container;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

/**
 * This interface extends IInvSlot by allowing you to modify a slot directly. This is only valid on
 * inventories backed by IInventory.
 * <p/>
 * <p/>
 * Created by CovertJaguar on 3/16/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public interface ModifiableContainerSlot extends ContainerSlot {

  /**
   * Sets the current ItemStack in the slot.
   */
  void setStack(ItemStack stack);

  default ItemStack clear() {
    ItemStack stack = getStack();
    setStack(ItemStack.EMPTY);
    return stack;
  }

  default void validate(Level world, BlockPos pos) {
    this.validate(world, pos, this::canPutStackInSlot);
  }

  default void validate(Level world, BlockPos pos, Predicate<ItemStack> filter) {
    ItemStack stack = getStack();
    if (!stack.isEmpty() && !filter.test(stack)) {
      clear();
      ContainerTools.dropItem(stack, world, pos);
    }
  }
}

package mods.railcraft.api.container.manipulator;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

public class ItemHandlerSlotAccessor<T extends IItemHandler> implements SlotAccessor {

  protected final T itemHandler;
  protected final int index;

  private ItemHandlerSlotAccessor(T itemHandler, int index) {
    this.itemHandler = itemHandler;
    this.index = index;
  }

  @Override
  public boolean isValid(ItemStack stack) {
    return this.itemHandler.isItemValid(this.index, stack);
  }

  @Override
  public ItemStack extract(int amount, boolean simulate) {
    return this.itemHandler.extractItem(this.index, amount, simulate);
  }

  @Override
  public ItemStack insert(ItemStack stack, boolean simulate) {
    return this.itemHandler.insertItem(this.index, stack, simulate);
  }

  @Override
  public ItemStack item() {
    return this.itemHandler.getStackInSlot(this.index);
  }

  @Override
  public int maxStackSize() {
    return this.itemHandler.getSlotLimit(this.index);
  }

  public static Stream<SlotAccessor> createSlots(IItemHandler itemHandler) {
    return IntStream.range(0, itemHandler.getSlots())
        .mapToObj(i -> new ItemHandlerSlotAccessor<>(itemHandler, i));
  }

  public static Stream<ModifiableSlotAccessor> createSlots(IItemHandlerModifiable itemHandler) {
    return IntStream.range(0, itemHandler.getSlots())
        .mapToObj(i -> new Modifiable(itemHandler, i));
  }

  private static class Modifiable extends ItemHandlerSlotAccessor<IItemHandlerModifiable>
      implements ModifiableSlotAccessor {

    private Modifiable(IItemHandlerModifiable itemHandler, int index) {
      super(itemHandler, index);
    }

    @Override
    public void setItem(ItemStack stack) {
      this.itemHandler.setStackInSlot(this.index, stack);
    }
  }
}

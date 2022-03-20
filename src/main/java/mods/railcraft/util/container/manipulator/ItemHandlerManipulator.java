package mods.railcraft.util.container.manipulator;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * An implementation of {@link ContainerManipulator} for Forge's {@link ItemHandler}.
 * 
 * @author Sm0keySa1m0n
 */
public abstract class ItemHandlerManipulator<T extends IItemHandler, S extends SlotAccessor>
    implements ContainerManipulator<S> {

  protected final T itemHandler;

  protected ItemHandlerManipulator(T itemHandler) {
    this.itemHandler = itemHandler;
  }

  @Override
  public int slotCount() {
    return this.itemHandler.getSlots();
  }

  protected abstract S createSlot(int index);

  @Override
  public Stream<S> stream() {
    return IntStream.range(0, this.itemHandler.getSlots())
        .mapToObj(this::createSlot);
  }

  protected class Slot implements SlotAccessor {

    protected final int index;

    public Slot(int index) {
      this.index = index;
    }

    @Override
    public boolean isValid(ItemStack stack) {
      return ItemHandlerManipulator.this.itemHandler.isItemValid(this.index, stack);
    }

    @Override
    public boolean canRemoveItem() {
      return !this.shrink(1, true).isEmpty();
    }

    @Override
    public ItemStack shrink() {
      return ItemHandlerManipulator.this.itemHandler.extractItem(this.index, 1, false);
    }

    @Override
    public ItemStack shrink(int amount, boolean simulate) {
      return ItemHandlerManipulator.this.itemHandler.extractItem(this.index, amount, simulate);
    }

    @Override
    public ItemStack grow(ItemStack stack, boolean simulate) {
      return ItemHandlerManipulator.this.itemHandler.insertItem(this.index, stack, simulate);
    }

    @Override
    public ItemStack getItem() {
      return ItemHandlerManipulator.this.itemHandler.getStackInSlot(this.index);
    }

    @Override
    public int maxStackSize() {
      return ItemHandlerManipulator.this.itemHandler.getSlotLimit(this.index);
    }
  }

  public static class Standard extends ItemHandlerManipulator<IItemHandler, SlotAccessor> {

    public Standard(IItemHandler itemHandler) {
      super(itemHandler);
    }

    @Override
    public SlotAccessor createSlot(int index) {
      return new Slot(index);
    }
  }

  public static class Modifiable
      extends ItemHandlerManipulator<IItemHandlerModifiable, ModifiableSlotAccessor> {

    public Modifiable(IItemHandlerModifiable itemHandler) {
      super(itemHandler);
    }

    @Override
    public ModifiableSlotAccessor createSlot(int index) {
      return new ExtInvSlot(index);
    }

    protected class ExtInvSlot extends Slot implements ModifiableSlotAccessor {

      public ExtInvSlot(int slot) {
        super(slot);
      }

      @Override
      public void setItem(ItemStack stack) {
        Modifiable.this.itemHandler.setStackInSlot(this.index, stack);
      }
    }
  }
}

package mods.railcraft.api.container.manipulator;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;

public class ItemHandlerSlotAccessor<T extends ResourceHandler<ItemResource>> implements SlotAccessor {

  protected final T itemHandler;
  protected final int index;

  private ItemHandlerSlotAccessor(T itemHandler, int index) {
    this.itemHandler = itemHandler;
    this.index = index;
  }

  @Override
  public boolean isValid(ItemStack stack) {
    return this.itemHandler.isValid(this.index, ItemResource.of(stack));
  }

  @Override
  public ItemStack extract(int amount, boolean simulate) {
    return ItemStack.EMPTY;
    //return this.itemHandler.extractItem(this.index, amount, simulate);
  }

  @Override
  public ItemStack insert(ItemStack stack, boolean simulate) {
    return ItemUtil.insertItemReturnRemaining(this.itemHandler, this.index, stack, simulate, null);
  }

  @Override
  public ItemStack item() {
    return ItemUtil.getStack(this.itemHandler, this.index);
  }

  @Override
  public int maxStackSize() {
    return this.itemHandler.getCapacityAsInt(this.index, ItemResource.EMPTY);
  }

  public static Stream<SlotAccessor> createSlots(ResourceHandler<ItemResource> itemHandler) {
    return IntStream.range(0, itemHandler.size())
        .mapToObj(i -> new ItemHandlerSlotAccessor<>(itemHandler, i));
  }

  /*public static Stream<ModifiableSlotAccessor> createSlots(IItemHandlerModifiable itemHandler) {
    return IntStream.range(0, itemHandler.getSlots())
        .mapToObj(i -> new Modifiable(itemHandler, i));
  }

  /*private static class Modifiable extends ItemHandlerSlotAccessor<IItemHandlerModifiable>
      implements ModifiableSlotAccessor {

    private Modifiable(IItemHandlerModifiable itemHandler, int index) {
      super(itemHandler, index);
    }

    @Override
    public void setItem(ItemStack stack) {
      this.itemHandler.setStackInSlot(this.index, stack);
    }
  }*/
}

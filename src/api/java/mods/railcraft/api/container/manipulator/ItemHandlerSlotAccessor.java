package mods.railcraft.api.container.manipulator;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;

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
    try (var tx = Transaction.openRoot()){
      var resource = this.itemHandler.getResource(this.index);
      if (resource.isEmpty()) {
        return ItemStack.EMPTY;
      }
      var extracted = this.itemHandler.extract(this.index, resource, amount, tx);
      if (!simulate) {
        tx.commit();
      }
      return resource.toStack(extracted);
    }
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
}

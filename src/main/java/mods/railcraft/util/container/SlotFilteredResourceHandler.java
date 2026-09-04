package mods.railcraft.util.container;

import java.util.function.IntPredicate;
import net.neoforged.neoforge.transfer.DelegatingResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.resource.Resource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

/**
 * Restricts the indices automation is allowed to insert into and extract from.
 *
 * <p>{@link DelegatingResourceHandler} forwards the whole-handler {@code insert} and {@code extract}
 * straight to its delegate, so overriding only the index-specific overloads leaves hoppers and pipes
 * free to touch every slot. Both are re-implemented here on top of the index-specific ones.
 */
public class SlotFilteredResourceHandler<T extends Resource> extends DelegatingResourceHandler<T> {

  private final IntPredicate insertable;
  private final IntPredicate extractable;

  public SlotFilteredResourceHandler(ResourceHandler<T> delegate, IntPredicate insertable,
      IntPredicate extractable) {
    super(delegate);
    this.insertable = insertable;
    this.extractable = extractable;
  }

  @Override
  public boolean isValid(int index, T resource) {
    return this.insertable.test(index) && super.isValid(index, resource);
  }

  @Override
  public int insert(int index, T resource, int amount, TransactionContext transaction) {
    return this.insertable.test(index)
        ? super.insert(index, resource, amount, transaction)
        : 0;
  }

  @Override
  public int insert(T resource, int amount, TransactionContext transaction) {
    TransferPreconditions.checkNonEmptyNonNegative(resource, amount);
    int inserted = 0;
    for (int index = 0, size = this.size(); index < size; index++) {
      inserted += this.insert(index, resource, amount - inserted, transaction);
      if (inserted == amount) {
        break;
      }
    }
    return inserted;
  }

  @Override
  public int extract(int index, T resource, int amount, TransactionContext transaction) {
    return this.extractable.test(index)
        ? super.extract(index, resource, amount, transaction)
        : 0;
  }

  @Override
  public int extract(T resource, int amount, TransactionContext transaction) {
    TransferPreconditions.checkNonEmptyNonNegative(resource, amount);
    int extracted = 0;
    for (int index = 0, size = this.size(); index < size; index++) {
      extracted += this.extract(index, resource, amount - extracted, transaction);
      if (extracted == amount) {
        break;
      }
    }
    return extracted;
  }
}

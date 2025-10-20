package mods.railcraft.util.container;

import net.minecraft.world.Container;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class CombinedVanillaContainerWrapper implements ResourceHandler<ItemResource> {

  private final ResourceHandler<ItemResource> inputInv;
  private final ResourceHandler<ItemResource> outputInv;

  public CombinedVanillaContainerWrapper(Container inputInv, Container outputInv) {
    this.inputInv = VanillaContainerWrapper.of(inputInv);
    this.outputInv = VanillaContainerWrapper.of(outputInv);
  }

  @Override
  public int size() {
    return this.inputInv.size() + this.outputInv.size();
  }

  @Override
  public ItemResource getResource(int index) {
    if (index < this.inputInv.size()) {
      return this.inputInv.getResource(index);
    } else {
      return this.outputInv.getResource(index - this.inputInv.size());
    }
  }

  @Override
  public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
    if (index >= this.inputInv.size()) {
      return 0;
    }
    return this.inputInv.insert(index, resource, amount, transaction);
  }

  @Override
  public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
    if (index < this.inputInv.size()) {
      return 0;
    }
    return this.outputInv.extract(index - this.inputInv.size(), resource, amount, transaction);
  }

  @Override
  public long getCapacityAsLong(int index, ItemResource resource) {
    if (index < this.inputInv.size()) {
      return this.inputInv.getCapacityAsLong(index, resource);
    } else {
      return this.outputInv.getCapacityAsLong(index - this.inputInv.size(), resource);
    }
  }

  @Override
  public long getAmountAsLong(int index) {
    if (index < this.inputInv.size()) {
      return this.inputInv.getAmountAsLong(index);
    } else {
      return this.outputInv.getAmountAsLong(index - this.inputInv.size());
    }
  }

  @Override
  public boolean isValid(int index, ItemResource resource) {
    if (index < this.inputInv.size()) {
      return this.inputInv.isValid(index, resource);
    } else {
      return false;
    }
  }
}

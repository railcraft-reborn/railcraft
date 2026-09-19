package mods.railcraft.world.level.block.entity.tank;

import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class ValveFluidHandler implements ResourceHandler<FluidResource> {

  private final TankBlockEntity blockEntity;
  private final TankBlockEntity master;

  public ValveFluidHandler(TankBlockEntity blockEntity, TankBlockEntity master) {
    this.blockEntity = blockEntity;
    this.master = master;
  }

  private ResourceHandler<FluidResource> getDelegate() {
    return this.master.getModule().getTank();
  }

  private boolean isFillable() {
    return this.blockEntity.getBlockPos().getY() - this.master.getBlockPos().getY() > 0;
  }

  private boolean isDrainable() {
    return this.blockEntity.getBlockPos().getY() - this.master.getBlockPos().getY() <= 1;
  }

  @Override
  public int size() {
    return this.getDelegate().size();
  }

  @Override
  public FluidResource getResource(int index) {
    return this.getDelegate().getResource(index);
  }

  @Override
  public long getAmountAsLong(int index) {
    return this.getDelegate().getAmountAsLong(index);
  }

  @Override
  public long getCapacityAsLong(int index, FluidResource resource) {
    return this.getDelegate().getCapacityAsLong(index, resource);
  }

  @Override
  public boolean isValid(int index, FluidResource resource) {
    return this.getDelegate().isValid(index, resource);
  }

  @Override
  public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
    if (!this.isFillable()) {
      return 0;
    }
    return this.getDelegate().insert(index, resource, amount, transaction);
  }

  @Override
  public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
    if (!this.isDrainable()) {
      return 0;
    }
    return this.getDelegate().extract(index, resource, amount, transaction);
  }
}

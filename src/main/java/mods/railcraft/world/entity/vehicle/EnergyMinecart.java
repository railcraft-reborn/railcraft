package mods.railcraft.world.entity.vehicle;

import mods.railcraft.api.carts.CartAdvanceable;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.inventory.EnergyMinecartMenu;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.component.LocomotiveEnergyComponent;
import mods.railcraft.world.item.component.RailcraftDataComponents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class EnergyMinecart extends RailcraftMinecart implements CartAdvanceable {

  public static final int MAX_CHARGE = 50000;
  private static final EntityDataAccessor<Integer> ENERGY =
      SynchedEntityData.defineId(EnergyMinecart.class, EntityDataSerializers.INT);
  private final CartStorage energyStorage = new CartStorage();

  public EnergyMinecart(EntityType<?> type, Level level) {
    super(type, level);
  }

  public EnergyMinecart(ItemStack itemStack, Level level, double x, double y, double z) {
    super(itemStack, RailcraftEntityTypes.ENERGY_MINECART.get(), level, x, y, z);
    this.loadFromItemStack(itemStack);
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.Builder builder) {
    super.defineSynchedData(builder);
    builder.define(ENERGY, 0);
  }

  @Override
  public void advanceOnTrack(ServerLevel serverLevel) {
    int drawnFromTrack = Charge.distribution
        .network(serverLevel)
        .access(this.blockPosition())
        .removeCharge(this.energyStorage.getCapacityAsInt() - this.energyStorage.getAmountAsInt(), false);
    try (var tx = Transaction.openRoot()) {
      this.energyStorage.insert(drawnFromTrack, tx);
      tx.commit();
    }
  }

  @Override
  protected void moveAlongTrack(ServerLevel serverLevel) {
    super.moveAlongTrack(serverLevel);
    this.advanceOnTrack(serverLevel);
  }

  @Override
  public int getContainerSize() {
    return 0;
  }

  public EnergyHandler getBatteryCart() {
    return this.energyStorage;
  }

  @Override
  protected void readAdditionalSaveData(ValueInput valueInput) {
    super.readAdditionalSaveData(valueInput);
    valueInput.readChild(CompoundTagKeys.BATTERY_MINECART, this.energyStorage);
  }

  @Override
  protected void addAdditionalSaveData(ValueOutput valueOutput) {
    super.addAdditionalSaveData(valueOutput);
    valueOutput.putChild(CompoundTagKeys.BATTERY_MINECART, this.energyStorage);
  }

  @Override
  protected void loadFromItemStack(ItemStack itemStack) {
    super.loadFromItemStack(itemStack);
    if (itemStack.has(RailcraftDataComponents.LOCOMOTIVE_ENERGY)) {
      this.energyStorage.setEnergyStored(itemStack.get(RailcraftDataComponents.LOCOMOTIVE_ENERGY).energy());
    }
  }

  @Override
  public ItemStack getPickResult() {
    var itemStack = super.getPickResult();
    itemStack.set(RailcraftDataComponents.LOCOMOTIVE_ENERGY, new LocomotiveEnergyComponent(this.energyStorage.getAmountAsInt()));
    return itemStack;
  }

  @Override
  public Item getDropItem() {
    return RailcraftItems.ENERGY_MINECART.get();
  }

  @Override
  protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
    return new EnergyMinecartMenu(id, playerInventory, this);
  }

  private class CartStorage implements EnergyHandler, ValueIOSerializable {

    private final EnergyJournal energyJournal = new EnergyJournal();

    @Override
    public long getAmountAsLong() {
      return EnergyMinecart.this.entityData.get(ENERGY);
    }

    @Override
    public long getCapacityAsLong() {
      return MAX_CHARGE;
    }

    private void setEnergyStored(int amount) {
      EnergyMinecart.this.entityData.set(ENERGY, Math.max(0, amount));
    }

    @Override
    public int insert(int amount, TransactionContext transaction) {
      TransferPreconditions.checkNonNegative(amount);
      int spaceAvailable = getCapacityAsInt() - getAmountAsInt();
      int inserted = Math.min(spaceAvailable, amount);
      if (inserted > 0) {
        energyJournal.updateSnapshots(transaction);
        this.setEnergyStored(getAmountAsInt() + inserted);
        return inserted;
      }
      return 0;
    }

    @Override
    public int extract(int amount, TransactionContext transaction) {
      TransferPreconditions.checkNonNegative(amount);

      int extracted = Math.min(getAmountAsInt(), amount);
      if (extracted > 0) {
        energyJournal.updateSnapshots(transaction);
        this.setEnergyStored(getAmountAsInt() - extracted);
        return extracted;
      }
      return 0;
    }

    @Override
    public void serialize(ValueOutput output) {
      output.putInt(CompoundTagKeys.ENERGY, this.getAmountAsInt());
    }

    @Override
    public void deserialize(ValueInput input) {
      this.setEnergyStored(input.getIntOr(CompoundTagKeys.ENERGY, 0));
    }

    private class EnergyJournal extends SnapshotJournal<Integer> {
      @Override
      protected Integer createSnapshot() {
        return getAmountAsInt();
      }

      @Override
      protected void revertToSnapshot(Integer snapshot) {
        setEnergyStored(snapshot);
      }
    }
  }
}

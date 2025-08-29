package mods.railcraft.world.entity.vehicle;

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
import net.neoforged.neoforge.energy.IEnergyStorage;

public class EnergyMinecart extends RailcraftMinecart {

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
  protected void moveAlongTrack(ServerLevel serverLevel) {
    super.moveAlongTrack(serverLevel);
    if (!this.level().isClientSide) {
      int drawnFromTrack = Charge.distribution
          .network((ServerLevel) this.level())
          .access(this.blockPosition())
          .removeCharge(this.energyStorage.getMaxEnergyStored() - this.energyStorage.getEnergyStored(), false);
      this.energyStorage.receiveEnergy(drawnFromTrack, false);
    }
  }

  @Override
  public int getContainerSize() {
    return 0;
  }

  public IEnergyStorage getBatteryCart() {
    return this.energyStorage;
  }

  @Override
  protected void readAdditionalSaveData(ValueInput valueInput) {
    super.readAdditionalSaveData(valueInput);
    this.energyStorage.setEnergyStored(valueInput.getIntOr(CompoundTagKeys.ENERGY, 0));
  }

  @Override
  protected void addAdditionalSaveData(ValueOutput valueOutput) {
    super.addAdditionalSaveData(valueOutput);
    valueOutput.putInt(CompoundTagKeys.ENERGY, this.energyStorage.getEnergyStored());
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
    itemStack.set(RailcraftDataComponents.LOCOMOTIVE_ENERGY, new LocomotiveEnergyComponent(this.energyStorage.getEnergyStored()));
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

  private class CartStorage implements IEnergyStorage {

    @Override
    public int getEnergyStored() {
      return EnergyMinecart.this.entityData.get(ENERGY);
    }

    public void setEnergyStored(int amount) {
      EnergyMinecart.this.entityData.set(ENERGY, amount);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
      if (!this.canReceive())
        return 0;

      int energyStored = this.getEnergyStored();
      int energyReceived = Math.min(MAX_CHARGE - energyStored, maxReceive);
      if (!simulate) {
        this.setEnergyStored(energyStored + energyReceived);
      }
      return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
      if (!this.canExtract())
        return 0;

      int energyStored = this.getEnergyStored();
      int energyExtracted = Math.min(energyStored, maxExtract);
      if (!simulate) {
        this.setEnergyStored(energyStored - energyExtracted);
      }
      return energyExtracted;
    }

    @Override
    public int getMaxEnergyStored() {
      return MAX_CHARGE;
    }

    @Override
    public boolean canExtract() {
      return true;
    }

    @Override
    public boolean canReceive() {
      return true;
    }
  }
}

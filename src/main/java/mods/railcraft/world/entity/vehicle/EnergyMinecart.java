package mods.railcraft.world.entity.vehicle;

import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.inventory.EnergyMinecartMenu;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyMinecart extends RailcraftMinecart {

  private static final int MAX_CHARGE = 50000;

  private static final EntityDataAccessor<Integer> ENERGY =
      SynchedEntityData.defineId(EnergyMinecart.class, EntityDataSerializers.INT);

  private final CartStorage energyStorage = new CartStorage();
  private final LazyOptional<IEnergyStorage> cartBattery = LazyOptional.of(() -> energyStorage);

  public EnergyMinecart(EntityType<?> type, Level level) {
    super(type, level);
  }

  public EnergyMinecart(ItemStack itemStack, double x, double y, double z, Level level) {
    super(itemStack, RailcraftEntityTypes.ENERGY_MINECART.get(), x, y, z, level);
    this.loadFromItemStack(itemStack);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(ENERGY, 0);
  }

  @Override
  protected void moveAlongTrack(BlockPos pos, BlockState state) {
    super.moveAlongTrack(pos, state);
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

  public IEnergyStorage getCartBattery() {
    return this.energyStorage;
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
    return ForgeCapabilities.ENERGY == capability
        ? this.cartBattery.cast()
        : super.getCapability(capability, facing);
  }

  @Override
  public void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
    this.energyStorage.setEnergyStored(tag.getInt(CompoundTagKeys.ENERGY));
  }

  @Override
  public void addAdditionalSaveData(CompoundTag tag) {
    super.addAdditionalSaveData(tag);
    tag.putInt(CompoundTagKeys.ENERGY, this.energyStorage.getEnergyStored());
  }

  @Override
  protected void loadFromItemStack(ItemStack itemStack) {
    super.loadFromItemStack(itemStack);
    var tag = itemStack.getTag();
    if (tag != null && tag.contains(CompoundTagKeys.ENERGY)) {
      this.energyStorage.setEnergyStored(tag.getInt(CompoundTagKeys.ENERGY));
    }
  }

  @Override
  public ItemStack getPickResult() {
    var itemStack = super.getPickResult();
    var tag = itemStack.getOrCreateTag();
    tag.putInt(CompoundTagKeys.ENERGY, this.energyStorage.getEnergyStored());
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

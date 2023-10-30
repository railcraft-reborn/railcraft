package mods.railcraft.world.entity.vehicle;

import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.energy.EnergyStorage;

public class EnergyMinecart extends RailcraftMinecart {

  public static final int MAX_CHARGE = 100000;
  public static final int CHARGE_LIMIT = 1000;
  private final LazyOptional<EnergyStorage> cartBattery =
      LazyOptional.of(() -> new EnergyStorage(MAX_CHARGE));

  protected EnergyMinecart(EntityType<?> type, Level level) {
    super(type, level);
  }

  protected EnergyMinecart(EntityType<?> type, double x, double y, double z, Level level) {
    super(type, x, y, z, level);
  }

  @Override
  public int getContainerSize() {
    return 0;
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
    if (Capabilities.ENERGY == capability) {
      return this.cartBattery.cast();
    }
    return super.getCapability(capability, facing);
  }

  @Override
  public void readAdditionalSaveData(CompoundTag data) {
    super.readAdditionalSaveData(data);
    this.cartBattery.ifPresent(cell -> cell.deserializeNBT(data.getCompound("battery")));
  }

  @Override
  public void addAdditionalSaveData(CompoundTag data) {
    super.addAdditionalSaveData(data);
    this.cartBattery.ifPresent(cell -> data.put("battery", cell.serializeNBT()));
  }

  // @Override
  // protected void loadFromItemStack(ItemStack itemStack) {
  // super.loadFromItemStack(itemStack);
  // CompoundNBT tag = itemStack.getTag();

  // if (tag.contains("batteryEnergy")) {
  // this.cartBattery.ifPresent(cell ->
  // cell.receiveEnergy(tag.getInt("batteryEnergy"), false));
  // }
  // }

  @Override
  public ItemStack getPickResult() {
    ItemStack itemStack = super.getPickResult();
    this.cartBattery.ifPresent(cell -> {
      itemStack.getOrCreateTag().putInt("batteryEnergy", cell.getEnergyStored());
    });
    return itemStack;
  }

  @Override
  public Item getDropItem() {
    throw new NotImplementedException();
  }

  @Override
  protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
    // TODO Auto-generated method stub
    return null;
  }
}

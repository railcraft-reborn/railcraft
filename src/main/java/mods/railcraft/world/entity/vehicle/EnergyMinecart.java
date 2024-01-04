package mods.railcraft.world.entity.vehicle;

import org.apache.commons.lang3.NotImplementedException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.energy.EnergyStorage;

public class EnergyMinecart extends RailcraftMinecart {

  public static final int MAX_CHARGE = 100000;
  public static final int CHARGE_LIMIT = 1000;
  private final EnergyStorage cartBattery = new EnergyStorage(MAX_CHARGE);

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
  public void readAdditionalSaveData(CompoundTag data) {
    super.readAdditionalSaveData(data);
    this.cartBattery.deserializeNBT(data.getCompound("battery"));
  }

  @Override
  public void addAdditionalSaveData(CompoundTag data) {
    super.addAdditionalSaveData(data);
    data.put("battery", this.cartBattery.serializeNBT());
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
    var itemStack = super.getPickResult();
    itemStack.getOrCreateTag().putInt("batteryEnergy", this.cartBattery.getEnergyStored());
    return itemStack;
  }

  @Override
  protected Item getDropItem() {
    throw new NotImplementedException();
  }

  @Override
  protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
    // TODO Auto-generated method stub
    return null;
  }
}

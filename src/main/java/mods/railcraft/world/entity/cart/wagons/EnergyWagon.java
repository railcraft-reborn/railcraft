package mods.railcraft.world.entity.cart.wagons;

import javax.annotation.Nullable;

import mods.railcraft.util.RailcraftNBTUtil;
import mods.railcraft.util.inventory.InvTools;
import mods.railcraft.world.entity.cart.RailcraftMinecartEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyWagon extends RailcraftMinecartEntity {

  public static final int MAX_CHARGE = 100000;
  public static final int CHARGE_LIMIT = 1000;
  private final LazyOptional<IEnergyStorage> cartBattery =
      LazyOptional.of(() -> new EnergyStorage(MAX_CHARGE));

  protected EnergyWagon(EntityType<?> type, World world) {
    super(type, world);
  }

  protected EnergyWagon(EntityType<?> type, double x, double y, double z,
      World world) {
    super(type, x, y, z, world);
  }

  @Override
  public int getContainerSize() {
    return 0;
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
    if (CapabilityEnergy.ENERGY == capability) {
      return this.cartBattery.cast();
    }
    return super.getCapability(capability, facing);
  }

  @Override
  public void readAdditionalSaveData(CompoundNBT data) {
    super.readAdditionalSaveData(data);
    this.cartBattery.ifPresent(cell ->
        RailcraftNBTUtil.loadEnergyCell(data.getCompound("battery"), cell));
  }

  @Override
  public void addAdditionalSaveData(CompoundNBT data) {
    super.addAdditionalSaveData(data);
    this.cartBattery.ifPresent(cell ->
        data.put("battery", RailcraftNBTUtil.saveEnergyCell(cell)));
  }

  // @Override
  // protected void loadFromItemStack(ItemStack itemStack) {
  //   super.loadFromItemStack(itemStack);
  //   CompoundNBT tag = itemStack.getTag();

  //   if (tag.contains("batteryEnergy")) {
  //     this.cartBattery.ifPresent(cell ->
  //         cell.receiveEnergy(tag.getInt("batteryEnergy"), false));
  //   }
  // }

  @Override
  public ItemStack getCartItem() {
    ItemStack itemStack = super.getCartItem();
    this.cartBattery.ifPresent(cell -> {
      InvTools.getItemData(itemStack).putInt("batteryEnergy",  cell.getEnergyStored());
    });
    return itemStack;
  }

  @Override
  protected Container createMenu(int id, PlayerInventory playerInventory) {
    // TODO Auto-generated method stub
    return null;
  }
}

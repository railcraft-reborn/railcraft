package mods.railcraft.world.entity.cart.wagons;

import javax.annotation.Nullable;

import mods.railcraft.api.carts.IEnergyTransfer;
import mods.railcraft.api.charge.CapabilityCharge;
import mods.railcraft.api.charge.IBatteryCart;
import mods.railcraft.battery.CartBattery;
import mods.railcraft.world.entity.cart.RailcraftMinecartEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class EnergyWagon extends RailcraftMinecartEntity
    implements IEnergyTransfer {

  public static final int MAX_CHARGE = 100000;
  public static final int CHARGE_LIMIT = 1000;
  private final LazyOptional<IBatteryCart> cartBattery =
      LazyOptional.of(() -> new CartBattery(IBatteryCart.Type.USER, MAX_CHARGE));

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
  public double injectEnergy(Object source, double amount, int tier,
      boolean ignoreTransferLimit, boolean simulate, boolean passAlong) {
    if (!simulate) {
      cartBattery.map(cart -> cart.addCharge((int)amount, false));
    }
    return amount;
  }

  @Override
  public double extractEnergy(Object source, double amount, int tier,
      boolean ignoreTransferLimit, boolean simulate, boolean passAlong) {
    if (!simulate) {
      cartBattery.map(cart -> cart.removeCharge((int)amount, false));
    }
    return amount;
  }

  @Override
  public boolean canInjectEnergy() {
    return cartBattery.map(cart -> {
      return (cart.getCharge() < (double)MAX_CHARGE);
    }).orElse(false);
  }

  @Override
  public boolean canExtractEnergy() {
    return true;
  }

  @Override
  public int getCapacity() {
    return MAX_CHARGE;
  }

  @Override
  public double getEnergy() {
    return cartBattery.map(cart -> {
      return cart.getCharge();
    }).orElse(0);
  }

  @Override
  public int getTransferLimit() {
    return CHARGE_LIMIT;
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
    super.getCapability(capability, facing);
    return CapabilityCharge.CART_BATTERY.orEmpty(capability, this.cartBattery.cast());
  }

  @Override
  protected Container createMenu(int id, PlayerInventory playerInventory) {
    // TODO Auto-generated method stub
    return null;
  }
}

package mods.railcraft.world.entity.vehicle;

import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.util.container.StackFilter;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.inventory.CargoMinecartMenu;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CargoMinecart extends FilteredMinecart {

  private static final EntityDataAccessor<Integer> SLOTS_FILLED =
      SynchedEntityData.defineId(CargoMinecart.class, EntityDataSerializers.INT);

  public CargoMinecart(EntityType<?> type, Level level) {
    super(type, level);
  }

  public CargoMinecart(ItemStack itemStack, double x, double y, double z,
      Level level) {
    super(itemStack, RailcraftEntityTypes.CARGO_MINECART.get(), x, y, z, level);
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.Builder builder) {
    super.defineSynchedData(builder);
    builder.define(SLOTS_FILLED, 0);
  }

  public int getSlotsFilled() {
    return this.entityData.get(SLOTS_FILLED);
  }

  private void setSlotsFilled(int slot) {
    this.entityData.set(SLOTS_FILLED, slot);
  }

  @Override
  public void tick() {
    super.tick();
    if (this.level().isClientSide()) {
      return;
    }
    this.setSlotsFilled((int) this.getItemStacks().stream().filter(StackFilter.ALL).count());
  }

  @Override
  public int getContainerSize() {
    return 18;
  }

  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    var filter = this.getFilterItem();
    if (!ItemStack.isSameItem(stack, filter)) {
      return false;
    }
    return StackFilter.CARGO.test(stack);
  }

  @Override
  public int getDefaultDisplayOffset() {
    return 8;
  }

  @Override
  public boolean canPassItemRequests(ItemStack stack) {
    return true;
  }

  @Override
  public boolean canAcceptPushedItem(RollingStock requester, ItemStack stack) {
    return true;
  }

  @Override
  public boolean canProvidePulledItem(RollingStock requester, ItemStack stack) {
    return true;
  }

  @Override
  protected Item getDropItem() {
    return RailcraftItems.CARGO_MINECART.get();
  }

  @Override
  protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
    return new CargoMinecartMenu(id, playerInventory, this);
  }
}

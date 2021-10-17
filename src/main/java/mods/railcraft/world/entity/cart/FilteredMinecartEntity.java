package mods.railcraft.world.entity.cart;

import mods.railcraft.api.item.PrototypedItem;
import mods.railcraft.util.inventory.InventoryAdvanced;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public abstract class FilteredMinecartEntity extends RailcraftMinecartEntity {

  private static final DataParameter<ItemStack> FILTER =
      EntityDataManager.defineId(FilteredMinecartEntity.class, DataSerializers.ITEM_STACK);
  private final InventoryAdvanced invFilter = new InventoryAdvanced(1).callbackInv(this).phantom();

  protected FilteredMinecartEntity(EntityType<?> type, World level) {
    super(type, level);
  }

  protected FilteredMinecartEntity(ItemStack itemStack, EntityType<?> type, double x, double y, double z,
      World level) {
    super(type, x, y, z, level);
    this.setFilter(getFilterFromCartItem(itemStack));
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(FILTER, ItemStack.EMPTY);
  }

  public static ItemStack getFilterFromCartItem(ItemStack cartStack) {
    return cartStack.getItem() instanceof PrototypedItem
        ? ((PrototypedItem) cartStack.getItem()).getPrototype(cartStack)
        : ItemStack.EMPTY;
  }

  public static ItemStack addFilterToCartItem(ItemStack cartStack, ItemStack filterStack) {
    if (!filterStack.isEmpty() && cartStack.getItem() instanceof PrototypedItem) {
      ((PrototypedItem) cartStack.getItem()).setPrototype(cartStack, filterStack);
    }
    return cartStack;
  }

  public ItemStack getFilteredCartItem(ItemStack filterStack) {
    ItemStack cartStack = this.getItem().getDefaultInstance();
    return cartStack.isEmpty() ? ItemStack.EMPTY : addFilterToCartItem(cartStack, filterStack);
  }

  protected abstract Item getItem();

  @Override
  public ItemStack getCartItem() {
    ItemStack stack = this.getFilteredCartItem(this.getFilterItem());
    if (!stack.isEmpty() && this.hasCustomName()) {
      stack.setHoverName(this.getDisplayName());
    }
    return stack;
  }

  @Override
  public boolean canBeRidden() {
    return false;
  }

  @Override
  protected void readAdditionalSaveData(CompoundNBT data) {
    super.readAdditionalSaveData(data);
    this.invFilter.deserializeNBT(data.getList("filterInventory", Constants.NBT.TAG_COMPOUND));
    this.entityData.set(FILTER, this.getFilterInv().getItem(0));
  }

  @Override
  protected void addAdditionalSaveData(CompoundNBT data) {
    super.addAdditionalSaveData(data);
    data.put("filterInventory", this.invFilter.serializeNBT());
  }

  public boolean hasFilter() {
    return !this.getFilterItem().isEmpty();
  }

  public ItemStack getFilterItem() {
    return this.entityData.get(FILTER);
  }

  public InventoryAdvanced getFilterInv() {
    return this.invFilter;
  }

  public void setFilter(ItemStack filter) {
    this.getFilterInv().setItem(0, filter);
  }

  @Override
  public void setChanged() {
    super.setChanged();
    this.entityData.set(FILTER, this.getFilterInv().getItem(0));
  }
}

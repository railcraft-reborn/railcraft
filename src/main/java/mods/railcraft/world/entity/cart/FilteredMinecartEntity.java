package mods.railcraft.world.entity.cart;

import mods.railcraft.api.item.PrototypedItem;
import mods.railcraft.util.container.AdvancedContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.Tag;

public abstract class FilteredMinecartEntity extends RailcraftMinecartEntity {

  private static final EntityDataAccessor<ItemStack> FILTER =
      SynchedEntityData.defineId(FilteredMinecartEntity.class, EntityDataSerializers.ITEM_STACK);
  private final AdvancedContainer invFilter = new AdvancedContainer(1).callbackContainer(this).phantom();

  protected FilteredMinecartEntity(EntityType<?> type, Level level) {
    super(type, level);
  }

  protected FilteredMinecartEntity(ItemStack itemStack, EntityType<?> type, double x, double y, double z,
      Level level) {
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
  protected void readAdditionalSaveData(CompoundTag data) {
    super.readAdditionalSaveData(data);
    this.invFilter.deserializeNBT(data.getList("filterInventory", Tag.TAG_COMPOUND));
    this.entityData.set(FILTER, this.getFilterInv().getItem(0));
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag data) {
    super.addAdditionalSaveData(data);
    data.put("filterInventory", this.invFilter.serializeNBT());
  }

  public boolean hasFilter() {
    return !this.getFilterItem().isEmpty();
  }

  public ItemStack getFilterItem() {
    return this.entityData.get(FILTER);
  }

  public AdvancedContainer getFilterInv() {
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

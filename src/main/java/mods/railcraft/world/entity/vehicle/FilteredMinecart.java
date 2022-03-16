package mods.railcraft.world.entity.vehicle;

import mods.railcraft.api.item.PrototypedItem;
import mods.railcraft.util.container.AdvancedContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class FilteredMinecart extends RailcraftMinecart {

  private static final EntityDataAccessor<ItemStack> FILTER =
      SynchedEntityData.defineId(FilteredMinecart.class, EntityDataSerializers.ITEM_STACK);
  private final AdvancedContainer filterContainer =
      new AdvancedContainer(1).callback((Container) this).phantom();

  protected FilteredMinecart(EntityType<?> type, Level level) {
    super(type, level);
  }

  protected FilteredMinecart(ItemStack itemStack, EntityType<?> type, double x, double y, double z,
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
    this.filterContainer.fromTag(data.getList("filter", Tag.TAG_COMPOUND));
    this.entityData.set(FILTER, this.getFilterInv().getItem(0));
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag data) {
    super.addAdditionalSaveData(data);
    data.put("filter", this.filterContainer.createTag());
  }

  public boolean hasFilter() {
    return !this.getFilterItem().isEmpty();
  }

  public ItemStack getFilterItem() {
    return this.entityData.get(FILTER);
  }

  public AdvancedContainer getFilterInv() {
    return this.filterContainer;
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

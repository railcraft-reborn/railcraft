package mods.railcraft.world.entity;

import javax.annotation.Nullable;
import mods.railcraft.api.carts.IMinecart;
import mods.railcraft.api.items.IPrototypedItem;
import mods.railcraft.util.inventory.InvTools;
import mods.railcraft.util.inventory.InventoryAdvanced;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public abstract class AbstractFilteredMinecartEntity extends AbstractRailcraftMinecartEntity implements IMinecart {

  private static final DataParameter<ItemStack> FILTER =
      EntityDataManager.defineId(AbstractFilteredMinecartEntity.class, DataSerializers.ITEM_STACK);
  private final InventoryAdvanced invFilter = new InventoryAdvanced(1).callbackInv(this).phantom();

  protected AbstractFilteredMinecartEntity(EntityType<?> type, World world) {
    super(type, world);
  }

  protected AbstractFilteredMinecartEntity(EntityType<?> type, double x, double y, double z, World world) {
    super(type, x, y, z, world);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(FILTER, ItemStack.EMPTY);
  }

  public static ItemStack getFilterFromCartItem(ItemStack cart) {
    if (cart.getItem() instanceof IPrototypedItem)
      return ((IPrototypedItem) cart.getItem()).getPrototype(cart);
    return InvTools.emptyStack();
  }

  public static ItemStack addFilterToCartItem(ItemStack cart, @Nullable ItemStack filter) {
    if (!InvTools.isEmpty(filter) && cart.getItem() instanceof IPrototypedItem) {
      ((IPrototypedItem) cart.getItem()).setPrototype(cart, filter);
    }
    return cart;
  }

  public ItemStack getFilteredCartItem(@Nullable ItemStack filter) {
    ItemStack stack = getItem().getDefaultInstance();
    if (InvTools.isEmpty(stack))
      return InvTools.emptyStack();
    return addFilterToCartItem(stack, filter);
  }

  @Override
  public void initEntityFromItem(ItemStack stack) {
    super.initEntityFromItem(stack);
    ItemStack filter = getFilterFromCartItem(stack);
    setFilter(filter);
  }

  @Override
  public ItemStack createCartItem(AbstractMinecartEntity cart) {
    ItemStack stack = getFilteredCartItem(getFilterItem());
    if (!InvTools.isEmpty(stack) && hasCustomName())
      stack.setHoverName(getName());
    return stack;
  }

  @Override
  public boolean canBeRidden() {
    return false;
  }

  @Override
  protected void readAdditionalSaveData(CompoundNBT data) {
    super.readAdditionalSaveData(data);
    invFilter.readFromNBT("invFilter", data);
    entityData.set(FILTER, getFilterInv().getItem(0));
  }

  @Override
  protected void addAdditionalSaveData(CompoundNBT data) {
    super.addAdditionalSaveData(data);
    invFilter.writeToNBT("invFilter", data);
  }

  public boolean hasFilter() {
    return !getFilterItem().isEmpty();
  }

  public ItemStack getFilterItem() {
    return this.entityData.get(FILTER);
  }

  public InventoryAdvanced getFilterInv() {
    return invFilter;
  }

  public void setFilter(ItemStack filter) {
    // dataManager.set(FILTER_DATA_ID, filter);
    getFilterInv().setItem(0, filter);
  }

  @Override
  public boolean doesCartMatchFilter(ItemStack stack) {
    return stack.getItem() == this.getItem();
  }

  @Override
  public void setChanged() {
    super.setChanged();
    this.entityData.set(FILTER, getFilterInv().getItem(0));
  }
}

package mods.railcraft.world.entity.vehicle;

import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.container.AdvancedContainer;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public abstract class FilteredMinecart extends RailcraftMinecart {

  private static final EntityDataAccessor<ItemStack> FILTER =
      SynchedEntityData.defineId(FilteredMinecart.class, EntityDataSerializers.ITEM_STACK);
  private final AdvancedContainer filterContainer =
      new AdvancedContainer(1).listener(this).phantom();

  protected FilteredMinecart(EntityType<?> type, Level level) {
    super(type, level);
  }

  protected FilteredMinecart(ItemStack itemStack, EntityType<?> type, Level level,
      double x, double y, double z) {
    super(itemStack, type, level, x, y, z);
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.Builder builder) {
    super.defineSynchedData(builder);
    builder.define(FILTER, ItemStack.EMPTY);
  }

  @Override
  protected void readAdditionalSaveData(ValueInput valueInput) {
    super.readAdditionalSaveData(valueInput);
    valueInput.readChild(CompoundTagKeys.FILTER, this.filterContainer);
    this.entityData.set(FILTER, this.getFilterInv().getItem(0));
  }

  @Override
  protected void addAdditionalSaveData(ValueOutput valueOutput) {
    super.addAdditionalSaveData(valueOutput);
    valueOutput.putChild(CompoundTagKeys.FILTER, this.filterContainer);
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

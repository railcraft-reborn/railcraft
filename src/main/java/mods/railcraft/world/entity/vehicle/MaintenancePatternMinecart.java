package mods.railcraft.world.entity.vehicle;

import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.util.container.ContainerTools;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class MaintenancePatternMinecart extends MaintenanceMinecart
    implements WorldlyContainer {

  protected final AdvancedContainer patternContainer =
      new AdvancedContainer(6).listener(this);

  protected MaintenancePatternMinecart(EntityType<?> type, Level level) {
    super(type, level);
  }

  protected MaintenancePatternMinecart(EntityType<?> type, double x, double y, double z,
      Level level) {
    super(type, x, y, z, level);
  }

  public Container getPattern() {
    return this.patternContainer;
  }

  @Override
  public int getContainerSize() {
    return 1;
  }

  @Override
  public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
    return false;
  }

  @Override
  public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) {
    return canPlaceItem(slot, stack);
  }

  protected void stockItems(int slotReplace, int slotStock) {
    ItemStack stackReplace = this.patternContainer.getItem(slotReplace);

    ItemStack stackStock = getItem(slotStock);

    var rollingStock = RollingStock.getOrThrow(this);

    if (!stackStock.isEmpty() && !ItemStack.isSameItem(stackReplace, stackStock)) {
      rollingStock.offerOrDropItem(stackStock);
      this.setItem(slotStock, ItemStack.EMPTY);
      stackStock = ItemStack.EMPTY;
    }

    if (stackReplace.isEmpty()) {
      return;
    }

    if (!ContainerTools.isStackFull(stackStock) && stackStock.getCount() < this.getMaxStackSize())
      this.setItem(slotStock,
          stackReplace.copyWithCount(stackStock.getCount() + rollingStock
              .pullItem(x -> ItemStack.isSameItem(stackReplace, x)).getCount()));
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag data) {
    super.addAdditionalSaveData(data);
    data.put("pattern", this.patternContainer.createTag());
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag data) {
    super.readAdditionalSaveData(data);
    this.patternContainer.fromTag(data.getList("pattern", Tag.TAG_COMPOUND));
  }
}

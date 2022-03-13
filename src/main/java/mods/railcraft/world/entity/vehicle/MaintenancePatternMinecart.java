package mods.railcraft.world.entity.vehicle;

import mods.railcraft.api.carts.CartUtil;
import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.util.container.StackFilter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.Tag;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public abstract class MaintenancePatternMinecart extends MaintenanceMinecart
    implements WorldlyContainer {

  protected final AdvancedContainer patternInventory = new AdvancedContainer(6).callbackContainer(this);

  protected MaintenancePatternMinecart(EntityType<?> type, Level world) {
    super(type, world);
  }

  protected MaintenancePatternMinecart(EntityType<?> type, double x, double y, double z,
      Level world) {
    super(type, x, y, z, world);
  }

  public Container getPattern() {
    return patternInventory;
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
    ItemStack stackReplace = this.patternInventory.getItem(slotReplace);

    ItemStack stackStock = getItem(slotStock);

    if (!stackStock.isEmpty() && !ContainerTools.isItemEqual(stackReplace, stackStock)) {
      CartUtil.transferHelper().offerOrDropItem(this, stackStock);
      this.setItem(slotStock, ItemStack.EMPTY);
      stackStock = ItemStack.EMPTY;
    }

    if (stackReplace.isEmpty())
      return;

    if (!ContainerTools.isStackFull(stackStock) && stackStock.getCount() < getMaxStackSize())
      this.setItem(slotStock,
          ContainerTools.copy(stackReplace, stackStock.getCount() + CartUtil.transferHelper()
              .pullStack(this, StackFilter.of(stackReplace)).getCount()));
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag data) {
    super.addAdditionalSaveData(data);
    data.put("patternInventory", this.patternInventory.serializeNBT());
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag data) {
    super.readAdditionalSaveData(data);
    this.patternInventory.deserializeNBT(
        data.getList("patternInventory", Tag.TAG_COMPOUND));
  }
}

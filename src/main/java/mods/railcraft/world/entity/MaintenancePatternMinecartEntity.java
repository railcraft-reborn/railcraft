package mods.railcraft.world.entity;

import mods.railcraft.api.carts.CartUtil;
import mods.railcraft.util.inventory.InvTools;
import mods.railcraft.util.inventory.InventoryAdvanced;
import mods.railcraft.util.inventory.filters.StackFilters;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public abstract class MaintenancePatternMinecartEntity extends MaintenanceMinecartEntity
    implements ISidedInventory {

  protected final InventoryAdvanced patternInv = new InventoryAdvanced(6).callbackInv(this);

  protected MaintenancePatternMinecartEntity(EntityType<?> type, World world) {
    super(type, world);
  }

  protected MaintenancePatternMinecartEntity(EntityType<?> type, double x, double y, double z,
      World world) {
    super(type, x, y, z, world);
  }

  public IInventory getPattern() {
    return patternInv;
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
    ItemStack stackReplace = patternInv.getItem(slotReplace);

    ItemStack stackStock = getItem(slotStock);

    if (!stackStock.isEmpty() && !InvTools.isItemEqual(stackReplace, stackStock)) {
      CartUtil.transferHelper().offerOrDropItem(this, stackStock);
      setItem(slotStock, ItemStack.EMPTY);
      stackStock = ItemStack.EMPTY;
    }

    if (stackReplace.isEmpty())
      return;

    if (!InvTools.isStackFull(stackStock) && stackStock.getCount() < getMaxStackSize())
      setItem(slotStock,
          InvTools.copy(stackReplace, stackStock.getCount() + CartUtil.transferHelper()
              .pullStack(this, StackFilters.of(stackReplace)).getCount()));
  }

  @Override
  protected void addAdditionalSaveData(CompoundNBT data) {
    super.addAdditionalSaveData(data);
    patternInv.writeToNBT("patternInv", data);
  }

  @Override
  protected void readAdditionalSaveData(CompoundNBT data) {
    super.readAdditionalSaveData(data);
    patternInv.readFromNBT("patternInv", data);
  }
}

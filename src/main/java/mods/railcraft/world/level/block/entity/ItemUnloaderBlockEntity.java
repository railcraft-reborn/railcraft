package mods.railcraft.world.level.block.entity;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import mods.railcraft.util.inventory.IInventoryManipulator;
import mods.railcraft.util.inventory.InventoryAdaptor;
import mods.railcraft.util.inventory.InventoryComposite;
import mods.railcraft.world.inventory.SlotOutput;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.items.CapabilityItemHandler;

public class ItemUnloaderBlockEntity extends ItemManipulatorBlockEntity {

  private static final Set<RedstoneMode> SUPPORTED_REDSTONE_MODES =
      Collections.unmodifiableSet(
          EnumSet.of(RedstoneMode.IMMEDIATE, RedstoneMode.COMPLETE, RedstoneMode.MANUAL));

  public ItemUnloaderBlockEntity() {
    super(RailcraftBlockEntityTypes.ITEM_UNLOADER.get());
  }

  @Override
  public Set<RedstoneMode> getSupportedRedstoneModes() {
    return SUPPORTED_REDSTONE_MODES;
  }

  @Override
  public IInventoryManipulator getSource() {
    return this.cart;
  }

  @Override
  public IInventoryManipulator getDestination() {
    return this.chests;
  }

  @Override
  public Slot getBufferSlot(int id, int x, int y) {
    return new SlotOutput(this, id, x, y);
  }

  @Override
  protected void upkeep() {
    super.upkeep();
    this.clearInventory();
  }

  @Override
  public boolean canHandleCart(AbstractMinecartEntity cart) {
    return super.canHandleCart(cart)
        && cart
            .getCapability(
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, this.getFacing().getOpposite())
            .map(InventoryAdaptor::of)
            .map(InventoryAdaptor::hasItems)
            .orElse(false);
  }

  private void clearInventory() {
    if (this.bufferInventory.hasItems()) {
      this.bufferInventory.moveOneItemTo(InventoryComposite.of(this.getAdjacentInventories()));
    }
  }
}

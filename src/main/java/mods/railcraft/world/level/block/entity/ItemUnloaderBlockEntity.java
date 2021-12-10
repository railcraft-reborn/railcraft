package mods.railcraft.world.level.block.entity;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import mods.railcraft.util.container.CompositeContainerAdaptor;
import mods.railcraft.util.container.ContainerAdaptor;
import mods.railcraft.util.container.ContainerManipulator;
import mods.railcraft.world.inventory.SlotOutput;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.CapabilityItemHandler;

public class ItemUnloaderBlockEntity extends ItemManipulatorBlockEntity {

  private static final Set<RedstoneMode> SUPPORTED_REDSTONE_MODES =
      Collections.unmodifiableSet(
          EnumSet.of(RedstoneMode.IMMEDIATE, RedstoneMode.COMPLETE, RedstoneMode.MANUAL));

  public ItemUnloaderBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.ITEM_UNLOADER.get(), blockPos, blockState);
  }

  @Override
  public Set<RedstoneMode> getSupportedRedstoneModes() {
    return SUPPORTED_REDSTONE_MODES;
  }

  @Override
  public ContainerManipulator getSource() {
    return this.cart;
  }

  @Override
  public ContainerManipulator getDestination() {
    return this.chests;
  }

  @Override
  public Slot getBufferSlot(int id, int x, int y) {
    return new SlotOutput(this, id, x, y);
  }

  @Override
  protected void upkeep() {
    super.upkeep();
    this.clearContainer();
  }

  @Override
  public boolean canHandleCart(AbstractMinecart cart) {
    return super.canHandleCart(cart)
        && cart
            .getCapability(
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, this.getFacing().getOpposite())
            .map(ContainerAdaptor::of)
            .map(ContainerAdaptor::hasItems)
            .orElse(false);
  }

  private void clearContainer() {
    if (this.bufferContainer.hasItems()) {
      this.bufferContainer.moveOneItemTo(CompositeContainerAdaptor.of(this.getAdjacentContainers()));
    }
  }
}

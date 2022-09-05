package mods.railcraft.world.level.block.entity.manipulator;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import mods.railcraft.util.container.manipulator.ContainerManipulator;
import mods.railcraft.world.inventory.slots.OutputSlot;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

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
  public ContainerManipulator<?> getSource() {
    return this.cart;
  }

  @Override
  public ContainerManipulator<?> getDestination() {
    return this.chests;
  }

  @Override
  public Slot getBufferSlot(int id, int x, int y) {
    return new OutputSlot(this, id, x, y);
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
                ForgeCapabilities.ITEM_HANDLER, this.getFacing().getOpposite())
            .map(ContainerManipulator::of)
            .map(ContainerManipulator::hasItems)
            .orElse(false);
  }

  private void clearContainer() {
    if (this.bufferContainer.hasItems()) {
      this.bufferContainer.moveOneItemTo(this.getAdjacentContainers());
    }
  }
}

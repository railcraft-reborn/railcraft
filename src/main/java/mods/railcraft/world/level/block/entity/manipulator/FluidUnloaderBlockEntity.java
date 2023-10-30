package mods.railcraft.world.level.block.entity.manipulator;

import java.util.stream.Stream;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.util.FluidTools;
import mods.railcraft.util.Predicates;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class FluidUnloaderBlockEntity extends FluidManipulatorBlockEntity {

  private static final int TRANSFER_RATE = 80;
  private static final Direction[] PUSH_TO = Stream.of(Direction.values())
      .filter(direction -> direction != Direction.UP)
      .toArray(Direction[]::new);

  public FluidUnloaderBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.FLUID_UNLOADER.get(), blockPos, blockState);
  }

  @Override
  protected void upkeep() {
    super.upkeep();
    this.tankManager.push(FluidTools.findNeighbors(this.level, this.getBlockPos(),
        Predicates.notOfType(FluidUnloaderBlockEntity.class), PUSH_TO), 0, TRANSFER_RATE);
  }

  @Override
  protected FluidTools.ProcessType getProcessType() {
    return FluidTools.ProcessType.FILL_ONLY;
  }


  @Override
  protected void processCart(AbstractMinecart cart) {
    IFluidHandler tankCart = getFluidHandler(cart, Direction.DOWN);
    if (tankCart != null) {
      FluidStack moved = FluidUtil.tryFluidTransfer(tank, tankCart,
          RailcraftConfig.SERVER.tankCartFluidTransferRate.get(), true);
      this.setProcessing(!moved.isEmpty());
    }
  }

  @Override
  protected boolean hasWorkForCart(AbstractMinecart cart) {
    IFluidHandler cartFluidHandler = getFluidHandler(cart, Direction.DOWN);
    if (cartFluidHandler == null) {
      return false;
    }

    if (this.getRedstoneMode() == RedstoneMode.IMMEDIATE) {
      return false;
    }

    if (this.getFilterFluid()
        .map(fluid -> cartFluidHandler.drain(fluid, IFluidHandler.FluidAction.SIMULATE).isEmpty())
        .orElse(false)) {
      return false;
    }

    return !cartFluidHandler.drain(1, IFluidHandler.FluidAction.SIMULATE).isEmpty();
  }
}

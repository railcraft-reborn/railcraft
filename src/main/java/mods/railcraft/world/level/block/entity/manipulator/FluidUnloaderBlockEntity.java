package mods.railcraft.world.level.block.entity.manipulator;

import java.util.stream.Stream;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.util.Predicates;
import mods.railcraft.util.fluids.FluidTools;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

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
    ResourceHandler<FluidResource> tankCart = getCartFluidHandler(cart, Direction.DOWN);
    if (tankCart != null) {
      final var transferRate = RailcraftConfig.SERVER.tankCartFluidTransferRate.get();
      int moved = ResourceHandlerUtil.move(tankCart, tank, __ -> true, transferRate, null);
      this.setProcessing(moved > 0);
    }
  }

  @Override
  protected boolean hasWorkForCart(AbstractMinecart cart) {
    ResourceHandler<FluidResource> cartFluidHandler = getCartFluidHandler(cart, Direction.DOWN);
    if (cartFluidHandler == null) {
      return false;
    }

    if (this.getRedstoneMode() == RedstoneMode.IMMEDIATE) {
      return false;
    }

    try (var tx = Transaction.openRoot()){
      if (this.getFilterFluid()
          .map(fluid -> cartFluidHandler.extract(FluidResource.of(fluid), fluid.getAmount(), tx) == 0)
          .orElse(false)) {
        return false;
      }

      return cartFluidHandler.extract(cartFluidHandler.getResource(0), 1, tx) > 0;
    }
  }
}

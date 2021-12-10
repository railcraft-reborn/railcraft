package mods.railcraft.gui.widget;

import mods.railcraft.world.level.material.fluid.tank.StandardTank;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;


/**
 * @author CovertJaguar (https://www.railcraft.info)
 */
public class FluidGaugeWidget extends Widget {

  public final StandardTank tank;
  private FluidStack lastSyncedFluidStack = FluidStack.EMPTY;
  private int syncCounter;

  public FluidGaugeWidget(StandardTank tank, int x, int y, int u, int v, int w, int h) {
    super(x, y, u, v, w, h);
    this.tank = tank;
  }

  @Override
  public boolean hasServerSyncData(ServerPlayer listener) {
    syncCounter++;
    return (syncCounter % 16) == 0
        || (!this.lastSyncedFluidStack.isEmpty()
            && !this.lastSyncedFluidStack.isFluidStackIdentical(tank.getFluid()));
  }

  @Override
  public void writeServerSyncData(ServerPlayer listener, FriendlyByteBuf data) {
    super.writeServerSyncData(listener, data);
    FluidStack fluidStack = tank.getFluid();
    this.lastSyncedFluidStack = fluidStack.isEmpty() ? FluidStack.EMPTY : fluidStack.copy();
    data.writeInt(tank.getCapacity());
    data.writeFluidStack(fluidStack);
  }

  @Override
  public void readServerSyncData(FriendlyByteBuf data) {
    super.readServerSyncData(data);
    tank.setCapacity(data.readInt());
    tank.setFluid(data.readFluidStack());
  }
}

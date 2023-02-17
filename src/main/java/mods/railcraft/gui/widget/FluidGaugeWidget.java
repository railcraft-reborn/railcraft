package mods.railcraft.gui.widget;

import java.util.List;
import mods.railcraft.world.level.material.fluid.StandardTank;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
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

  public List<Component> getTooltip() {
    return this.tank.getTooltip();
  }

  @Override
  public boolean requiresSync(ServerPlayer player) {
    syncCounter++;
    return (syncCounter % 16) == 0
        || (!this.lastSyncedFluidStack.isEmpty()
            && !this.lastSyncedFluidStack.isFluidStackIdentical(tank.getFluid()));
  }

  @Override
  public void writeToBuf(ServerPlayer player, FriendlyByteBuf data) {
    super.writeToBuf(player, data);
    FluidStack fluidStack = tank.getFluid();
    this.lastSyncedFluidStack = fluidStack.isEmpty() ? FluidStack.EMPTY : fluidStack.copy();
    data.writeInt(tank.getCapacity());
    data.writeFluidStack(fluidStack);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    super.readFromBuf(data);
    tank.setCapacity(data.readInt());
    tank.setFluid(data.readFluidStack());
  }
}

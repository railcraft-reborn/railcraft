package mods.railcraft.gui.widget;

import java.util.List;
import mods.railcraft.world.level.material.StandardTank;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.fluids.FluidStack;

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
            && !FluidStack.matches(this.lastSyncedFluidStack, tank.getFluidStack()));
  }

  @Override
  public void writeToBuf(ServerPlayer player, RegistryFriendlyByteBuf data) {
    super.writeToBuf(player, data);
    var fluidStack = tank.getFluidStack();
    this.lastSyncedFluidStack = fluidStack.copy();
    data.writeInt(tank.getCapacity());
    FluidStack.OPTIONAL_STREAM_CODEC.encode(data, fluidStack);
  }

  @Override
  public void readFromBuf(RegistryFriendlyByteBuf data) {
    super.readFromBuf(data);
    tank.setCapacity(data.readInt());
    tank.setFluid(FluidStack.OPTIONAL_STREAM_CODEC.decode(data));
  }
}

package mods.railcraft.world.level.material.fluid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import com.google.common.collect.ForwardingList;
import mods.railcraft.world.level.material.fluid.tank.StandardTank;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class TankManager extends ForwardingList<StandardTank>
    implements IFluidHandler, INBTSerializable<ListNBT> {

  public static final TankManager NIL = new TankManager() {
    @Override
    protected List<StandardTank> delegate() {
      return Collections.emptyList();
    }
  };

  public static final BiFunction<TileEntity, Direction, Boolean> TANK_FILTER =
      (t, f) -> t.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, f).isPresent();
  private final List<StandardTank> tanks = new ArrayList<>();

  public TankManager(StandardTank... tanks) {
    for (StandardTank tank : tanks) {
      this.add(tank);
    }
  }

  @Override
  protected List<StandardTank> delegate() {
    return this.tanks;
  }

  @Override
  public boolean add(StandardTank tank) {
    this.tanks.add(tank);
    int index = this.tanks.indexOf(tank);
    tank.setTankIndex(index);
    return true;
  }

  @Override
  public ListNBT serializeNBT() {
    ListNBT tanksTag = new ListNBT();
    for (byte i = 0; i < this.tanks.size(); i++) {
      StandardTank tank = this.tanks.get(i);
      CompoundNBT tankTag = new CompoundNBT();
      tankTag.putByte("index", i);
      tank.writeToNBT(tankTag);
      tanksTag.add(tankTag);
    }
    return tanksTag;
  }

  @Override
  public void deserializeNBT(ListNBT tanksTag) {
    for (INBT tankTag : tanksTag) {
      int index = ((CompoundNBT) tankTag).getByte("index");
      if (index >= 0 && index < this.tanks.size()) {
        this.tanks.get(index).readFromNBT(((CompoundNBT) tankTag));
      }
    }
  }

  public void writePacketData(PacketBuffer data) {
    for (StandardTank tank : tanks) {
      data.writeFluidStack(tank.getFluid());
    }
  }

  public void readPacketData(PacketBuffer data) {
    for (StandardTank tank : tanks) {
      tank.setFluid(data.readFluidStack());
    }
  }

  @Override
  public int fill(FluidStack resource, FluidAction doFill) {
    return this.tanks.stream()
        .mapToInt(tank -> tank.fill(resource, doFill))
        .filter(filled -> filled > 0)
        .findFirst()
        .orElse(0);
  }

  public int fill(int tankIndex, FluidStack resource, FluidAction doFill) {
    return this.tanks.get(tankIndex).fill(resource, doFill);
  }

  @Override
  public FluidStack drain(FluidStack resource, FluidAction doDrain) {
    return this.tanks.stream()
        .map(tank -> tank.drain(resource, doDrain))
        .findFirst()
        .orElse(null);
  }

  @Override
  public FluidStack drain(int maxDrain, FluidAction doDrain) {
    return this.tanks.stream()
        .map(tank -> tank.drain(maxDrain, doDrain))
        .findFirst()
        .orElse(null);
  }

  public FluidStack drain(int tankIndex, FluidStack resource, FluidAction doDrain) {
    return this.tanks.get(tankIndex).drain(resource, doDrain);
  }

  public FluidStack drain(int tankIndex, int maxDrain, FluidAction doDrain) {
    return this.tanks.get(tankIndex).drain(maxDrain, doDrain);
  }

  @Override
  public StandardTank get(int tankIndex) {
    return this.tanks.get(tankIndex);
  }

  public void setCapacity(int tankIndex, int capacity) {
    StandardTank tank = this.get(tankIndex);
    tank.setCapacity(capacity);
    FluidStack fluidStack = tank.getFluid();
    if (fluidStack != null && fluidStack.getAmount() > capacity) {
      fluidStack.setAmount(capacity);
    }
  }

  public void pull(Collection<IFluidHandler> targets, int tankIndex, int amount) {
    this.transfer(targets, tankIndex,
        (me, them) -> FluidUtil.tryFluidTransfer(me, them, amount, true));
  }

  public void push(Collection<IFluidHandler> targets, int tankIndex, int amount) {
    this.transfer(targets, tankIndex,
        (me, them) -> FluidUtil.tryFluidTransfer(them, me, amount, true));
  }

  public void transfer(Collection<IFluidHandler> targets, int tankIndex,
      BiConsumer<IFluidHandler, IFluidHandler> transfer) {
    targets.forEach(them -> transfer.accept(this.get(tankIndex), them));
  }

  @Override
  public int getTanks() {
    return this.tanks.size();
  }

  @Override
  public FluidStack getFluidInTank(int tank) {
    return this.tanks.get(tank).getFluid();
  }

  @Override
  public int getTankCapacity(int tank) {
    return this.tanks.get(tank).getCapacity();
  }

  @Override
  public boolean isFluidValid(int tank, FluidStack stack) {
    return this.tanks.get(tank).isFluidValid(stack);
  }
}

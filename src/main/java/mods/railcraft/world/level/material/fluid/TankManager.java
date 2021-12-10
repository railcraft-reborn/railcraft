package mods.railcraft.world.level.material.fluid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import com.google.common.collect.ForwardingList;
import mods.railcraft.world.level.material.fluid.tank.StandardTank;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class TankManager extends ForwardingList<StandardTank>
    implements IFluidHandler, INBTSerializable<ListTag> {

  public static final TankManager EMPTY = new TankManager() {
    @Override
    protected List<StandardTank> delegate() {
      return Collections.emptyList();
    }
  };

  public static final BiFunction<BlockEntity, Direction, Boolean> TANK_FILTER =
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
  public ListTag serializeNBT() {
    ListTag tanksTag = new ListTag();
    for (byte i = 0; i < this.tanks.size(); i++) {
      StandardTank tank = this.tanks.get(i);
      CompoundTag tankTag = new CompoundTag();
      tankTag.putByte("index", i);
      tank.writeToNBT(tankTag);
      tanksTag.add(tankTag);
    }
    return tanksTag;
  }

  @Override
  public void deserializeNBT(ListTag tanksTag) {
    for (Tag tankTag : tanksTag) {
      int index = ((CompoundTag) tankTag).getByte("index");
      if (index >= 0 && index < this.tanks.size()) {
        this.tanks.get(index).readFromNBT(((CompoundTag) tankTag));
      }
    }
  }

  public void writePacketData(FriendlyByteBuf data) {
    for (StandardTank tank : tanks) {
      data.writeFluidStack(tank.getFluid());
    }
  }

  public void readPacketData(FriendlyByteBuf data) {
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

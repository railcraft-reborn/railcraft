package mods.railcraft.world.level.material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import org.jetbrains.annotations.NotNull;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class TankManager implements IFluidHandler, INBTSerializable<ListTag> {

  public static final TankManager EMPTY = new TankManager(List.of());

  public static final BiFunction<BlockEntity, Direction, Boolean> TANK_FILTER =
      (t, f) -> t.getCapability(Capabilities.FLUID_HANDLER, f).isPresent();
  private final List<StandardTank> tanks;

  public TankManager(StandardTank... tanks) {
    this(new ArrayList<>(Arrays.asList(tanks)));
  }

  private TankManager(List<StandardTank> tanks) {
    this.tanks = tanks;
  }

  public boolean add(StandardTank tank) {
    return this.tanks.add(tank);
  }

  @Override
  public ListTag serializeNBT() {
    var tanksTag = new ListTag();
    for (byte i = 0; i < this.tanks.size(); i++) {
      var tank = this.tanks.get(i);
      var tankTag = new CompoundTag();
      tankTag.putByte("index", i);
      tank.writeToNBT(tankTag);
      tanksTag.add(tankTag);
    }
    return tanksTag;
  }

  @Override
  public void deserializeNBT(ListTag tanksTag) {
    for (int i = 0; i < tanksTag.size(); i++) {
      var tag = tanksTag.getCompound(i);
      int index = tag.getByte("index");
      if (index >= 0 && index < this.tanks.size()) {
        this.tanks.get(index).readFromNBT(tag);
      }
    }
  }

  public void writePacketData(FriendlyByteBuf data) {
    for (var tank : this.tanks) {
      data.writeFluidStack(tank.getFluid());
    }
  }

  public void readPacketData(FriendlyByteBuf data) {
    for (var tank : this.tanks) {
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
        .filter(fluid -> !fluid.isEmpty())
        .findFirst()
        .orElse(FluidStack.EMPTY);
  }

  @NotNull
  @Override
  public FluidStack drain(int maxDrain, FluidAction doDrain) {
    return this.tanks.stream()
        .map(tank -> tank.drain(maxDrain, doDrain))
        .filter(fluid -> !fluid.isEmpty())
        .findFirst()
        .orElse(FluidStack.EMPTY);
  }

  public FluidStack drain(int tankIndex, FluidStack resource, FluidAction doDrain) {
    return this.tanks.get(tankIndex).drain(resource, doDrain);
  }

  public FluidStack drain(int tankIndex, int maxDrain, FluidAction doDrain) {
    return this.tanks.get(tankIndex).drain(maxDrain, doDrain);
  }

  public StandardTank get(int tankIndex) {
    return this.tanks.get(tankIndex);
  }

  public void setCapacity(int tankIndex, int capacity) {
    var tank = this.get(tankIndex);
    tank.setCapacity(capacity);
    var fluidStack = tank.getFluid();
    if (fluidStack.getAmount() > capacity) {
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

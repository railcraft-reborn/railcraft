package mods.railcraft.world.level.material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import org.jetbrains.annotations.NotNull;
import mods.railcraft.api.core.CompoundTagKeys;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class TankManager implements IFluidHandler, ValueIOSerializable {

  public static final TankManager EMPTY = new TankManager(List.of());

  public static final BiFunction<BlockEntity, Direction, Boolean> TANK_FILTER = (be, dir) ->
      be.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, be.getBlockPos(), dir) != null;
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
  public void serialize(ValueOutput valueOutput) {
    var list = valueOutput.childrenList(CompoundTagKeys.TANK);
    for (int i = 0; i < this.tanks.size(); i++) {
      var tankOutput = list.addChild();
      tankOutput.putInt(CompoundTagKeys.INDEX, i);
      this.tanks.get(i).serialize(tankOutput);
    }
  }

  @Override
  public void deserialize(ValueInput valueInput) {
    var list = valueInput.childrenListOrEmpty(CompoundTagKeys.TANK);
    list.forEach(input -> {
      var index = input.getIntOr(CompoundTagKeys.INDEX, -1);
      if (index >= 0 && index < this.tanks.size()) {
        this.tanks.get(index).deserialize(input);
      }
    });
  }

  public void writePacketData(RegistryFriendlyByteBuf data) {
    for (var tank : this.tanks) {
      FluidStack.OPTIONAL_STREAM_CODEC.encode(data, tank.getFluid());
    }
  }

  public void readPacketData(RegistryFriendlyByteBuf data) {
    for (var tank : this.tanks) {
      tank.setFluid(FluidStack.OPTIONAL_STREAM_CODEC.decode(data));
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

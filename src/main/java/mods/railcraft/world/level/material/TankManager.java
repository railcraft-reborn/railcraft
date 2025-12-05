package mods.railcraft.world.level.material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import com.google.common.base.Predicates;
import mods.railcraft.api.core.CompoundTagKeys;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class TankManager implements ResourceHandler<FluidResource>, ValueIOSerializable {

  public static final TankManager EMPTY = new TankManager(List.of());

  public static final BiFunction<BlockEntity, Direction, Boolean> TANK_FILTER = (be, dir) ->
      be.getLevel().getCapability(Capabilities.Fluid.BLOCK, be.getBlockPos(), dir) != null;
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
      FluidStack.OPTIONAL_STREAM_CODEC.encode(data, tank.getFluidStack());
    }
  }

  public void readPacketData(RegistryFriendlyByteBuf data) {
    for (var tank : this.tanks) {
      tank.setFluid(FluidStack.OPTIONAL_STREAM_CODEC.decode(data));
    }
  }

  @Override
  public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
    return this.tanks.get(index).insert(0, resource, amount, transaction);
  }

  @Override
  public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
    return this.tanks.get(index).extract(0, resource, amount, transaction);
  }

  public StandardTank get(int tankIndex) {
    return this.tanks.get(tankIndex);
  }

  public void setCapacity(int tankIndex, int capacity) {
    var tank = this.get(tankIndex);
    tank.setCapacity(capacity);
    var fluidStack = tank.getFluidStack();
    if (fluidStack.getAmount() > capacity) {
      fluidStack.setAmount(capacity);
    }
  }

  public void pull(Collection<ResourceHandler<FluidResource>> targets, int tankIndex, int amount) {
    this.transfer(targets, tankIndex,
        (me, them) -> ResourceHandlerUtil.move(them, me, Predicates.alwaysTrue(), amount, null));
  }

  public void push(Collection<ResourceHandler<FluidResource>> targets, int tankIndex, int amount) {
    this.transfer(targets, tankIndex,
        (me, them) -> ResourceHandlerUtil.move(me, them, Predicates.alwaysTrue(), amount, null));
  }

  public void transfer(Collection<ResourceHandler<FluidResource>> targets, int tankIndex,
      BiConsumer<ResourceHandler<FluidResource>, ResourceHandler<FluidResource>> transfer) {
    targets.forEach(them -> transfer.accept(this.get(tankIndex), them));
  }

  @Override
  public int size() {
    return this.tanks.size();
  }

  @Override
  public FluidResource getResource(int index) {
    return this.tanks.get(index).getResource(0);
  }

  @Override
  public long getAmountAsLong(int index) {
    return this.tanks.get(index).getAmountAsLong(0);
  }

  @Override
  public long getCapacityAsLong(int index, FluidResource resource) {
    return this.tanks.get(index).getCapacityAsLong(0, resource);
  }

  @Override
  public boolean isValid(int index, FluidResource resource) {
    return this.tanks.get(index).isValid(0, resource);
  }
}

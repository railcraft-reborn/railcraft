package mods.railcraft.world.level.material.fluid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import com.google.common.collect.ForwardingList;
import mods.railcraft.util.AdjacentBlockEntityCache;
import mods.railcraft.world.level.material.fluid.tank.StandardTank;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class TankManager extends ForwardingList<StandardTank> implements IFluidHandler {

  public static final TankManager NIL = new TankManager() {
    @Override
    protected List<StandardTank> delegate() {
      return Collections.emptyList();
    }
  };
  public static final BiFunction<TileEntity, Direction, Boolean> TANK_FILTER =
      (t, f) -> t.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, f).isPresent();
  private final List<StandardTank> tanks = new ArrayList<>();

  @Override
  protected List<StandardTank> delegate() {
    return tanks;
  }

  @Override
  public boolean add(StandardTank tank) {
    tanks.add(tank);
    int index = tanks.indexOf(tank);
    tank.setTankIndex(index);
    return true;
  }

  public void writeTanksToNBT(CompoundNBT data) {
    ListNBT tagList = new ListNBT();
    for (byte slot = 0; slot < tanks.size(); slot++) {
      StandardTank tank = tanks.get(slot);
      if (tank.getFluid() != null) {
        CompoundNBT tag = new CompoundNBT();
        tag.putByte("tank", slot);
        tank.writeToNBT(tag);
        tagList.add(tag);
      }
    }
    data.put("tanks", tagList);
  }

  public void readTanksFromNBT(CompoundNBT data) {
    ListNBT tagList = data.getList("tanks", Constants.NBT.TAG_COMPOUND);
    for (INBT tag : tagList) {
      int slot = ((CompoundNBT) tag).getByte("tank");
      if (slot >= 0 && slot < tanks.size())
        tanks.get(slot).readFromNBT(((CompoundNBT) tag));
    }
  }

  public void writePacketData(PacketBuffer data) throws IOException {
    for (StandardTank tank : tanks) {
      data.writeFluidStack(tank.getFluid());
    }
  }

  public void readPacketData(PacketBuffer data) throws IOException {
    for (StandardTank tank : tanks) {
      tank.setFluid(data.readFluidStack());
    }
  }

  @Override
  public int fill(FluidStack resource, FluidAction doFill) {
    return tanks.stream().mapToInt(tank -> tank.fill(resource, doFill)).filter(filled -> filled > 0)
        .findFirst().orElse(0);
  }

  public int fill(int tankIndex, @Nullable FluidStack resource, FluidAction doFill) {
    if (tankIndex < 0 || tankIndex >= tanks.size() || resource == null)
      return 0;

    return tanks.get(tankIndex).fill(resource, doFill);
  }

  @Override
  public @Nullable FluidStack drain(FluidStack resource, FluidAction doDrain) {
    return tanks.stream().map(tank -> tank.drain(resource, doDrain)).filter(Objects::nonNull)
        .findFirst().orElse(null);
  }

  @Override
  public @Nullable FluidStack drain(int maxDrain, FluidAction doDrain) {
    return tanks.stream().map(tank -> tank.drain(maxDrain, doDrain)).filter(Objects::nonNull)
        .findFirst().orElse(null);
  }

  public @Nullable FluidStack drain(int tankIndex, FluidStack resource, FluidAction doDrain) {
    if (tankIndex < 0 || tankIndex >= tanks.size())
      return null;

    return tanks.get(tankIndex).drain(resource, doDrain);
  }

  public @Nullable FluidStack drain(int tankIndex, int maxDrain, FluidAction doDrain) {
    if (tankIndex < 0 || tankIndex >= tanks.size())
      return null;

    return tanks.get(tankIndex).drain(maxDrain, doDrain);
  }

  @Override
  public StandardTank get(int tankIndex) {
    if (tankIndex < 0 || tankIndex >= tanks.size())
      throw new IllegalArgumentException("No Fluid Tank exists for index " + tankIndex);
    return tanks.get(tankIndex);
  }

  public void setCapacity(int tankIndex, int capacity) {
    StandardTank tank = get(tankIndex);
    tank.setCapacity(capacity);
    FluidStack fluidStack = tank.getFluid();
    if (fluidStack != null && fluidStack.getAmount() > capacity)
      fluidStack.setAmount(capacity);
  }

  public void pull(AdjacentBlockEntityCache cache, Predicate<? super TileEntity> filter, int tankIndex,
      int amount, Direction... sides) {
    Collection<IFluidHandler> targets = FluidTools.findNeighbors(cache, filter, sides);
    pull(targets, tankIndex, amount);
  }

  public void push(AdjacentBlockEntityCache cache, Predicate<? super TileEntity> filter, int tankIndex,
      int amount, Direction... sides) {
    Collection<IFluidHandler> targets = FluidTools.findNeighbors(cache, filter, sides);
    push(targets, tankIndex, amount);
  }

  public void pull(Collection<IFluidHandler> targets, int tankIndex, int amount) {
    transfer(targets, tankIndex, (me, them) -> FluidUtil.tryFluidTransfer(me, them, amount, true));
  }

  public void push(Collection<IFluidHandler> targets, int tankIndex, int amount) {
    transfer(targets, tankIndex, (me, them) -> FluidUtil.tryFluidTransfer(them, me, amount, true));
  }

  public void transfer(Collection<IFluidHandler> targets, int tankIndex,
      BiConsumer<IFluidHandler, IFluidHandler> transfer) {
    targets.forEach(them -> transfer.accept(get(tankIndex), them));
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

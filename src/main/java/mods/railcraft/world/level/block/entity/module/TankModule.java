package mods.railcraft.world.level.block.entity.module;

import java.util.function.Supplier;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.world.level.material.fluid.FluidItemHelper;
import mods.railcraft.world.level.material.fluid.FluidTools;
import mods.railcraft.world.level.material.fluid.FluidTools.ProcessType;
import mods.railcraft.world.level.material.fluid.tank.FilteredTank;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

/**
 * Created by CovertJaguar on 1/28/2019 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class TankModule extends ContainerModule implements ICapabilityProvider {

  public static final int TANK_INDEX = 0;
  public static final int SLOT_INPUT = 0;
  public static final int SLOT_PROCESS = 1;
  public static final int SLOT_OUTPUT = 2;
  private final FilteredTank tank;

  private final LazyOptional<IItemHandler> itemHandler =
      LazyOptional.of(() -> new InvWrapper(this) {
        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
          if (slot == SLOT_OUTPUT)
            return ItemStack.EMPTY;
          return super.extractItem(slot, amount, simulate);
        }
      });

  private final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(this::getTank);

  private FluidTools.ProcessState processState = FluidTools.ProcessState.RESET;

  private int processTicks;

  public TankModule(ModuleProvider adapter, int capacity) {
    this(adapter, capacity, null);
  }

  public TankModule(ModuleProvider provider, int capacity, @Nullable Supplier<Fluid> filter) {
    super(provider, 3);
    this.tank = new FilteredTank(capacity);
    if (filter != null) {
      this.tank.setFilterFluid(filter);
    }
  }

  public FilteredTank getTank() {
    return this.tank;
  }

  @Override
  public void serverTick() {
    if (this.processTicks++ >= FluidTools.BUCKET_FILL_TIME) {
      this.processTicks = 0;
      this.processState =
          FluidTools.processContainer(this, this.tank, ProcessType.DRAIN_THEN_FILL,
              this.processState);
    }
  }

  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    if (!super.canPlaceItem(slot, stack)) {
      return false;
    }
    if (slot == SLOT_INPUT) {
      return (!this.tank.isEmpty()
          && FluidItemHelper.isRoomInContainer(stack, this.tank.getFluidType()))
          || FluidUtil.getFluidContained(stack).isPresent();
    }
    return false;
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
    if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return this.itemHandler.cast();
    }

    if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return this.fluidHandler.cast();
    }

    return LazyOptional.empty();
  }

  @Override
  public CompoundTag serializeNBT() {
    var tag = super.serializeNBT();
    tag.put("tank", this.tank.writeToNBT(new CompoundTag()));
    tag.putString("processState", this.processState.getSerializedName());
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag tag) {
    super.deserializeNBT(tag);
    this.tank.readFromNBT(tag.getCompound("tank"));
    this.processState = FluidTools.ProcessState.getByName(tag.getString("processState"))
        .orElse(FluidTools.ProcessState.RESET);
  }

  @Override
  public void writeToBuf(FriendlyByteBuf out) {
    super.writeToBuf(out);
    out.writeVarInt(this.tank.getCapacity());
    out.writeFluidStack(this.tank.getFluid());
  }

  @Override
  public void readFromBuf(FriendlyByteBuf in) {
    super.readFromBuf(in);
    this.tank.setCapacity(in.readVarInt());
    this.tank.setFluid(in.readFluidStack());
  }
}

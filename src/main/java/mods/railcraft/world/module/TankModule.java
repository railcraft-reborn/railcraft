package mods.railcraft.world.module;

import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.fluids.FluidTools;
import mods.railcraft.util.fluids.FluidTools.ProcessType;
import mods.railcraft.world.level.block.entity.tank.TankBlockEntity;
import mods.railcraft.world.level.material.StandardTank;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.DelegatingResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class TankModule extends ContainerModule<TankBlockEntity> {

  public static final int SLOT_INPUT = 0;
  public static final int SLOT_PROCESS = 1;
  public static final int SLOT_OUTPUT = 2;
  private final StandardTank tank;

  private final ResourceHandler<ItemResource> itemHandler =
      new DelegatingResourceHandler<>(VanillaContainerWrapper.of(this)) {
        @Override
        public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
          if (index == SLOT_OUTPUT) {
            return 0;
          }
          return super.extract(index, resource, amount, transaction);
        }
  };

  private FluidTools.ProcessState processState = FluidTools.ProcessState.RESET;
  private int processTicks;


  public TankModule(TankBlockEntity provider, StandardTank tank) {
    super(provider, 3);
    this.tank = tank;
  }

  public StandardTank getTank() {
    return this.tank;
  }

  @Override
  public void serverTick() {
    if (this.processTicks++ >= FluidTools.BUCKET_FILL_TIME) {
      this.processTicks = 0;
      this.processState = FluidTools.processContainer(this, this.tank,
          ProcessType.DRAIN_THEN_FILL, this.processState);
    }
  }

  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    return switch (slot) {
      case SLOT_INPUT -> (!this.tank.isEmpty()
          && FluidTools.isRoomInContainer(stack, this.tank.getFluidStack().getFluid()))
          || !FluidUtil.getFirstStackContained(stack).isEmpty();
      case SLOT_PROCESS, SLOT_OUTPUT -> true;
      default -> false;
    } && super.canPlaceItem(slot, stack);
  }

  public ResourceHandler<ItemResource> getItemHandler() {
    return this.itemHandler;
  }

  @Override
  public void serialize(ValueOutput valueOutput) {
    super.serialize(valueOutput);
    valueOutput.putChild(CompoundTagKeys.TANK, this.tank);
    valueOutput.store(CompoundTagKeys.PROCESS_STATE, FluidTools.ProcessState.CODEC, this.processState);
  }

  @Override
  public void deserialize(ValueInput valueInput) {
    super.deserialize(valueInput);
    valueInput.readChild(CompoundTagKeys.TANK, this.tank);
    this.processState = valueInput.read(CompoundTagKeys.PROCESS_STATE, FluidTools.ProcessState.CODEC)
        .orElse(FluidTools.ProcessState.RESET);
  }

  @Override
  public void writeToBuf(RegistryFriendlyByteBuf out) {
    super.writeToBuf(out);
    out.writeVarInt(this.tank.getCapacity());
    FluidStack.OPTIONAL_STREAM_CODEC.encode(out, this.tank.getFluidStack());
  }

  @Override
  public void readFromBuf(RegistryFriendlyByteBuf in) {
    super.readFromBuf(in);
    this.tank.setCapacity(in.readVarInt());
    this.tank.setFluid(FluidStack.OPTIONAL_STREAM_CODEC.decode(in));
  }
}

package mods.railcraft.world.level.block.entity.manipulator;

import java.util.Optional;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.carts.FluidTransferHandler;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.Predicates;
import mods.railcraft.util.fluids.FluidTools;
import mods.railcraft.world.entity.vehicle.locomotive.SteamLocomotive;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.track.LockingTrackBlockEntity;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class FluidLoaderBlockEntity extends FluidManipulatorBlockEntity {

  private static final int RESET_WAIT = 200;
  private static final int TRANSFER_RATE = SharedConstants.TICKS_PER_SECOND;
  private static final float MAX_PIPE_LENGTH = 1.25F;
  private static final float PIPE_INCREMENT = 0.01f;
  private static final Direction[] PULL_FROM = Stream.of(Direction.values())
      .filter(direction -> direction != Direction.DOWN)
      .toArray(Direction[]::new);
  private float lastPipeLength;
  private float pipeLength;

  public FluidLoaderBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.FLUID_LOADER.get(), blockPos, blockState);
  }

  private void resetPipeLength() {
    this.pipeLength = 0;
  }

  public float getPipeLength(float partialTicks) {
    return Mth.lerp(partialTicks, this.lastPipeLength, this.pipeLength);
  }

  private void setPipeLength(float pipeLength) {
    this.lastPipeLength = this.pipeLength;
    this.pipeLength = pipeLength;
    this.syncToClient();
  }

  private void extendPipe() {
    float newPipeLength =
        this.isPipeExtended() ? MAX_PIPE_LENGTH : this.pipeLength + PIPE_INCREMENT;
    this.setPipeLength(newPipeLength);
  }

  private void retractPipe() {
    float newPipeLength = this.isPipeRetracted() ? 0 : this.pipeLength - PIPE_INCREMENT;
    this.setPipeLength(newPipeLength);
  }

  private boolean isPipeExtended() {
    return this.pipeLength >= MAX_PIPE_LENGTH;
  }

  private boolean isPipeRetracted() {
    return this.pipeLength <= 0;
  }

  @Override
  protected void reset() {
    super.reset();
    if (this.currentCart instanceof FluidTransferHandler handler) {
      handler.setFilling(false);
    }
  }

  @Override
  protected void upkeep() {
    super.upkeep();
    this.tankManager.pull(FluidTools.findNeighbors(this.level, this.getBlockPos(),
        Predicates.notOfType(FluidLoaderBlockEntity.class), PULL_FROM), 0, TRANSFER_RATE);
  }

  @Override
  public Optional<AbstractMinecart> findCart() {
    return EntitySearcher.findMinecarts()
        .box(builder -> builder
            .at(this.getBlockPos().below(2))
            .raiseCeiling(1)
            .inflate(-0.1F))
        .stream(this.level)
        .findAny();
  }

  @Override
  protected void waitForReset(@Nullable AbstractMinecart cart) {
    if (isPipeRetracted() && cart != null) {
      this.sendCart(cart);
    } else {
      this.retractPipe();
    }
  }

  @Override
  protected void onNoCart() {
    if (!this.isPipeRetracted()) {
      this.retractPipe();
    }
  }

  @Override
  protected void processCart(AbstractMinecart cart) {
    if (cart instanceof SteamLocomotive locomotive) {
      if (!locomotive.isSafeToFill()) {
        this.retractPipe();
        return;
      }
    }

    IFluidHandler tankCart = getCartFluidHandler(cart, Direction.UP);
    if (tankCart == null) {
      return;
    }
    var cartNeedsFilling = this.cartNeedsFilling(tankCart);
    var needsPipe = this.getBlockPos().getY() - cart.position().y() > 1.0D;

    if (cartNeedsFilling && needsPipe) {
      this.extendPipe();
    } else {
      this.retractPipe();
    }

    if (cartNeedsFilling && (!needsPipe || this.isPipeExtended())) {
      FluidStack moved = FluidUtil.tryFluidTransfer(tankCart, this.tank,
          RailcraftConfig.SERVER.tankCartFluidTransferRate.get(), true);
      this.setProcessing(!moved.isEmpty());
    } else {
      this.setProcessing(false);
    }

    if (this.isProcessing()) {
      this.setPowered(false);
    }

    if (cart instanceof FluidTransferHandler fluidTransferHandler) {
      fluidTransferHandler.setFilling(this.isProcessing());
    }

    if (!this.tank.getFluid().isEmpty()
        && tankCart.fill(this.tank.getFluid(), IFluidHandler.FluidAction.SIMULATE) == 0) {
      this.setResetTimer(RESET_WAIT);
    }
  }

  private boolean cartNeedsFilling(IFluidHandler cartFluidHandler) {
    FluidStack fluidStack = this.tank.getFluid();
    return !fluidStack.isEmpty()
        && cartFluidHandler.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE) > 0;
  }

  @Override
  protected boolean hasWorkForCart(AbstractMinecart cart) {
    if (!this.isPipeRetracted()) {
      return true;
    }
    IFluidHandler cartFluidHandler = getCartFluidHandler(cart, Direction.UP);
    if (cartFluidHandler == null) {
      return false;
    }
    FluidStack fluid = this.getFluidHandled();
    if (fluid.isEmpty()) {
      return false;
    }
    return switch (this.getRedstoneMode()) {
      case COMPLETE -> cartFluidHandler.fill(fluid, IFluidHandler.FluidAction.SIMULATE) > 0;
      case PARTIAL -> !cartFluidHandler.drain(fluid, IFluidHandler.FluidAction.SIMULATE).isEmpty();
      default -> false;
    };
  }

  @Override
  protected void setPowered(boolean powered) {
    if (this.isManualMode()) {
      powered = false;
    }
    super.setPowered(powered);
    if (powered && this.level != null) {
      this.resetPipeLength();
      var blockEntity = this.level.getBlockEntity(this.getBlockPos().below(2));
      if (blockEntity instanceof LockingTrackBlockEntity lockingTrack) {
        lockingTrack.releaseCart();
      }
    }
  }

  @Override
  protected FluidTools.ProcessType getProcessType() {
    return FluidTools.ProcessType.DRAIN_ONLY;
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
    this.resetPipeLength();
  }

  @Override
  public void clearRemoved() {
    super.clearRemoved();
    this.resetPipeLength();
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.saveAdditional(tag, provider);
    tag.putFloat(CompoundTagKeys.PIPE_LENGTH, this.pipeLength);
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.loadAdditional(tag, provider);
    this.pipeLength = tag.getFloat(CompoundTagKeys.PIPE_LENGTH);
  }

  @Override
  public void writeToBuf(RegistryFriendlyByteBuf data) {
    super.writeToBuf(data);
    data.writeFloat(this.pipeLength);
  }

  @Override
  public void readFromBuf(RegistryFriendlyByteBuf data) {
    super.readFromBuf(data);
    this.setPipeLength(data.readFloat());
  }
}

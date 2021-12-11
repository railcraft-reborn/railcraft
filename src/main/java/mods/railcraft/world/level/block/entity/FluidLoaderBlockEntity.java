package mods.railcraft.world.level.block.entity;

import java.util.stream.Stream;
import javax.annotation.Nullable;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.carts.FluidMinecart;
import mods.railcraft.util.AABBFactory;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.Predicates;
import mods.railcraft.world.entity.vehicle.locomotive.SteamLocomotive;
import mods.railcraft.world.level.material.fluid.FluidTools;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class FluidLoaderBlockEntity extends FluidManipulatorBlockEntity {

  private static final int RESET_WAIT = 200;
  private static final int TRANSFER_RATE = 20;
  private static final float MAX_PIPE_LENGTH = 1.25F;
  private static final float PIPE_INCREMENT = 0.01f;
  private static final Direction[] PULL_FROM = Stream.of(Direction.values())
      .filter(direction -> direction != Direction.DOWN)
      .toArray(Direction[]::new);
  private float lastPipeLength;
  private float pipeLength;
  private boolean needsPipe;

  public FluidLoaderBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.FLUID_LOADER.get(),  blockPos,  blockState);
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
  public AABB getRenderBoundingBox() {
    return new AABB(this.getX(), this.getY() - 1, this.getZ(), this.getX() + 1,
        this.getY() + 1, this.getZ() + 1);
  }

  @Override
  protected void reset() {
    super.reset();
    if (this.currentCart instanceof FluidMinecart) {
      ((FluidMinecart) this.currentCart).setFilling(false);
    }
  }

  @Override
  protected void upkeep() {
    super.upkeep();
    this.tankManager.pull(FluidTools.findNeighbors(this.level, this.getBlockPos(),
        Predicates.notOfType(FluidLoaderBlockEntity.class), PULL_FROM), 0, TRANSFER_RATE);
  }

  @Nullable
  @Override
  public AbstractMinecart getCart() {
    AABBFactory factory = AABBFactory.start()
        .at(this.getBlockPos().below(2))
        .raiseCeiling(1)
        .grow(-0.1F);
    AbstractMinecart cart = EntitySearcher.findMinecarts()
        .around(factory)
        .in(this.level)
        .any();
    this.needsPipe = cart != null && this.getBlockPos().getY() - cart.position().y() > 1.0D;
    return cart;
  }

  @Override
  protected void waitForReset(@Nullable AbstractMinecart cart) {
    if (isPipeRetracted()) {
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
    if (cart instanceof SteamLocomotive) {
      SteamLocomotive loco = (SteamLocomotive) cart;
      if (!loco.isSafeToFill()) {
        this.retractPipe();
        return;
      }
    }

    IFluidHandler tankCart = getFluidHandler(cart, Direction.UP);
    if (tankCart == null) {
      return;
    }
    boolean cartNeedsFilling = this.cartNeedsFilling(tankCart);

    if (cartNeedsFilling && this.needsPipe) {
      this.extendPipe();
    } else {
      this.retractPipe();
    }

    if (cartNeedsFilling && (!this.needsPipe || this.isPipeExtended())) {
      FluidStack moved = FluidUtil.tryFluidTransfer(tankCart, this.tank,
          RailcraftConfig.server.tankCartFluidTransferRate.get(), true);
      this.setProcessing(!moved.isEmpty());
    } else {
      this.setProcessing(false);
    }

    if (this.isProcessing()) {
      this.setPowered(false);
    }

    if (cart instanceof FluidMinecart) {
      ((FluidMinecart) cart).setFilling(this.isProcessing());
    }

    if (!this.tank.getFluid().isEmpty()
        && tankCart.fill(this.tank.getFluid(), FluidAction.SIMULATE) == 0) {
      this.setResetTimer(RESET_WAIT);
    }
  }

  private boolean cartNeedsFilling(IFluidHandler cartFluidHandler) {
    FluidStack fluidStack = this.tank.getFluid();
    return !fluidStack.isEmpty()
        && cartFluidHandler.fill(fluidStack, FluidAction.SIMULATE) > 0;
  }

  @Override
  protected boolean hasWorkForCart(AbstractMinecart cart) {
    if (!this.isPipeRetracted()) {
      return true;
    }
    IFluidHandler cartFluidHandler = getFluidHandler(cart, Direction.UP);
    if (cartFluidHandler == null) {
      return false;
    }
    FluidStack fluid = this.getFluidHandled();
    if (fluid.isEmpty()) {
      return false;
    }
    switch (this.getRedstoneMode()) {
      case COMPLETE:
        return cartFluidHandler.fill(fluid, FluidAction.SIMULATE) > 0;
      case PARTIAL:
        return !cartFluidHandler.drain(fluid, FluidAction.SIMULATE).isEmpty();
      default:
        break;
    }
    return false;
  }

  @Override
  protected void setPowered(boolean powered) {
    if (this.isManualMode()) {
      powered = false;
    }
    // if (powered) {
    // this.resetPipeLength();
    // TileEntity blockEntity = this.level.getBlockEntity(this.getBlockPos().below(2));
    // if (blockEntity instanceof LockingTrack) {
    // ((LockingTrack) blockEntity).releaseCart();
    // }
    // }
    super.setPowered(powered);
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
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putFloat("pipeLength", this.pipeLength);
  }

  @Override
  public void load( CompoundTag tag) {
    super.load( tag);
    this.pipeLength = tag.getFloat("pipeLength");
  }

  @Override
  public void writeSyncData(FriendlyByteBuf data) {
    super.writeSyncData(data);
    data.writeFloat(this.pipeLength);
  }

  @Override
  public void readSyncData(FriendlyByteBuf data) {
    super.readSyncData(data);
    this.setPipeLength(data.readFloat());
  }
}

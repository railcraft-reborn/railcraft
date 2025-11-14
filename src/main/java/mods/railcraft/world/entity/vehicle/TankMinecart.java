package mods.railcraft.world.entity.vehicle;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.carts.FluidTransferHandler;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.util.fluids.FluidTools;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.inventory.TankMinecartMenu;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.component.RailcraftDataComponents;
import mods.railcraft.world.level.material.StandardTank;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.SimpleFluidContent;

public class TankMinecart extends FilteredMinecart
    implements WorldlyContainer, FluidTransferHandler {

  // Can't use FluidStack directly because its equals method doesn't consider amount so will never
  // sync if the amount is changed.
  private static final EntityDataAccessor<CompoundTag> FLUID_STACK =
      SynchedEntityData.defineId(TankMinecart.class, EntityDataSerializers.COMPOUND_TAG);
  private static final EntityDataAccessor<Boolean> FILLING =
      SynchedEntityData.defineId(TankMinecart.class, EntityDataSerializers.BOOLEAN);
  public static final int SLOT_INPUT = 0;
  public static final int SLOT_PROCESSING = 1;
  public static final int SLOT_OUTPUT = 2;
  private static final int[] SLOTS = ContainerTools.buildSlotArray(0, 3);
  private final StandardTank tank =
      StandardTank
          .ofBuckets(RailcraftConfig.SERVER.tankCartFluidCapacity.get())
          .changeCallback(this::tankChanged)
          .setValidator(fluidStack ->
              this.getFilterFluid()
                  .map(x -> FluidStack.isSameFluidSameComponents(x, fluidStack))
                  .orElse(true));
  private final ContainerMapper invLiquids = ContainerMapper.make(this).ignoreItemChecks();
  private int fluidProcessingTimer;
  private FluidTools.ProcessState processState = FluidTools.ProcessState.RESET;

  public TankMinecart(EntityType<?> type, Level level) {
    super(type, level);
  }

  public TankMinecart(ItemStack itemStack, double x, double y, double z, Level level) {
    super(itemStack, RailcraftEntityTypes.TANK_MINECART.get(), x, y, z, level);
    this.loadFromItemStack(itemStack);
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.Builder builder) {
    super.defineSynchedData(builder);
    builder.define(FLUID_STACK, new CompoundTag());
    builder.define(FILLING, false);
  }

  private void tankChanged() {
    var tag = new CompoundTag();
    tag.put(CompoundTagKeys.TANK, FluidStack.OPTIONAL_CODEC
        .encode(this.tank.getFluid(), NbtOps.INSTANCE, new CompoundTag())
        .getOrThrow());
    this.entityData.set(FLUID_STACK, tag);
  }

  @Override
  public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
    super.onSyncedDataUpdated(key);
    if (key.equals(FLUID_STACK)) {
      var fluidStack = FluidStack.OPTIONAL_CODEC
          .parse(NbtOps.INSTANCE, this.entityData.get(FLUID_STACK).get(CompoundTagKeys.TANK))
          .getOrThrow();
      this.tank.setFluid(fluidStack);
    }
  }

  public StandardTank getTankManager() {
    return this.tank;
  }

  @Override
  public void remove(RemovalReason reason) {
    super.remove(reason);
    Containers.dropContents(this.level(), this, this.invLiquids);
  }

  @Override
  public void tick() {
    super.tick();

    if (this.level().isClientSide()) {
      return;
    }

    ContainerTools.dropIfInvalid(this.level(), this.blockPosition(), this, SLOT_INPUT);
    ContainerTools.drop(this.level(), this.blockPosition(), this, SLOT_PROCESSING,
        FluidTools::isFluidHandler);
    ContainerTools.drop(this.level(), this.blockPosition(), this, SLOT_OUTPUT,
        FluidTools::isFluidHandler);

    if (this.fluidProcessingTimer++ >= FluidTools.BUCKET_FILL_TIME) {
      this.fluidProcessingTimer = 0;
      this.processState = FluidTools.processContainer(
          this.invLiquids, this.tank, FluidTools.ProcessType.DRAIN_THEN_FILL, this.processState);
    }
  }

  @Override
  public InteractionResult interact(Player player, InteractionHand hand) {
    if (FluidTools.interactWithFluidHandler(player, hand, getTankManager())) {
      return InteractionResult.SUCCESS;
    }

    return super.interact(player, hand);
  }

  @Override
  public int getContainerSize() {
    return 3;
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
    this.processState = FluidTools.ProcessState.fromTag(tag);
    this.tank.readFromNBT(this.registryAccess(), tag.getCompound(CompoundTagKeys.TANK));
    this.tankChanged();
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag tag) {
    super.addAdditionalSaveData(tag);
    tag.putString(CompoundTagKeys.PROCESS_STATE, this.processState.getSerializedName());
    var tankTag = new CompoundTag();
    this.tank.writeToNBT(this.registryAccess(), tankTag);
    tag.put(CompoundTagKeys.TANK, tankTag);
  }

  public boolean isFilling() {
    return this.entityData.get(FILLING);
  }

  @Override
  public void setFilling(boolean filling) {
    this.entityData.set(FILLING, filling);
  }

  public Optional<FluidStack> getFilterFluid() {
    return FluidUtil.getFluidContained(this.getFilterItem());
  }

  public Container getInvLiquids() {
    return this.invLiquids;
  }

  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    return slot == SLOT_INPUT && FluidTools.isFluidHandler(stack);
  }

  @Override
  public int[] getSlotsForFace(Direction side) {
    return SLOTS;
  }

  @Override
  public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
    return this.canPlaceItem(slot, stack);
  }

  @Override
  public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
    return slot == SLOT_OUTPUT;
  }

  @Override
  public boolean canPassFluidRequests(FluidStack fluid) {
    return this.getFilterFluid()
        .map(filter -> FluidStack.isSameFluidSameComponents(filter, fluid))
        .orElseGet(() -> this.tank.isEmpty() || FluidStack.isSameFluidSameComponents(this.tank.getFluid(), fluid));
  }

  @Override
  public boolean canAcceptPushedFluid(RollingStock requester, FluidStack fluid) {
    return this.canPassFluidRequests(fluid);
  }

  @Override
  public boolean canProvidePulledFluid(RollingStock requester, FluidStack fluid) {
    return this.canPassFluidRequests(fluid);
  }

  @Override
  public ItemStack getPickResult() {
    var itemStack = super.getPickResult();
    if (!this.tank.isEmpty()) {
      itemStack.set(RailcraftDataComponents.FLUID, SimpleFluidContent.copyOf(this.tank.getFluid()));
    }
    return itemStack;
  }

  @Override
  protected void loadFromItemStack(ItemStack itemStack) {
    super.loadFromItemStack(itemStack);
    if (itemStack.has(RailcraftDataComponents.FLUID)) {
      this.tank.setFluid(itemStack.get(RailcraftDataComponents.FLUID).copy());
    }
  }

  @Override
  protected Item getDropItem() {
    return RailcraftItems.TANK_MINECART.get();
  }

  @Override
  protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
    return new TankMinecartMenu(id, playerInventory, this);
  }
}

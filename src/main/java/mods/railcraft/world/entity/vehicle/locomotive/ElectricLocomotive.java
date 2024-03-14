package mods.railcraft.world.entity.vehicle.locomotive;

import java.util.Set;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.charge.ChargeCartStorageImpl;
import mods.railcraft.sounds.RailcraftSoundEvents;
import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.inventory.ElectricLocomotiveMenu;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.TicketItem;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class ElectricLocomotive extends Locomotive implements WorldlyContainer {

  // as of 2021 all the numbers have been increased due to RF/FE usage
  private static final int ACTUAL_FUEL_GAIN_PER_REQUEST = SharedConstants.TICKS_PER_SECOND; // the original value
  private static final int FUEL_PER_REQUEST = 1;
  // multiplied by 4 because rf
  private static final int CHARGE_USE_PER_REQUEST =
      (ACTUAL_FUEL_GAIN_PER_REQUEST * 4) * FUEL_PER_REQUEST;
  public static final int MAX_CHARGE = 5000;
  private static final int SLOT_TICKET = 0;
  private static final int[] SLOTS = ContainerTools.buildSlotArray(0, 1);

  private static final Set<Mode> ALLOWED_MODES = Set.of(Mode.RUNNING, Mode.SHUTDOWN);

  private final Container ticketInventory =
      new ContainerMapper(this, SLOT_TICKET, 2).ignoreItemChecks();

  private ChargeCartStorageImpl cartStorage = new ChargeCartStorageImpl(MAX_CHARGE);
  private final LazyOptional<IEnergyStorage> energyHandler;

  public ElectricLocomotive(EntityType<?> type, Level level) {
    super(type, level);
    this.energyHandler = LazyOptional.of(() -> this.cartStorage);
  }

  public ElectricLocomotive(ItemStack itemStack, double x, double y, double z,
      ServerLevel serverLevel) {
    super(itemStack, RailcraftEntityTypes.ELECTRIC_LOCOMOTIVE.get(), x, y, z, serverLevel);
    this.loadFromItemStack(itemStack);
    this.energyHandler = LazyOptional.of(() -> this.cartStorage);
  }

  @Override
  public Set<Mode> getSupportedModes() {
    return ALLOWED_MODES;
  }

  @Override
  protected DyeColor getDefaultPrimaryColor() {
    return DyeColor.YELLOW;
  }

  @Override
  protected DyeColor getDefaultSecondaryColor() {
    return DyeColor.BLACK;
  }

  @Override
  public SoundEvent getWhistleSound() {
    return RailcraftSoundEvents.ELECTRIC_WHISTLE.get();
  }

  @Override
  protected int getIdleFuelUse() {
    return 0;
  }

  @Override
  public int retrieveFuel() {
    if (this.cartStorage.getEnergyStored() > CHARGE_USE_PER_REQUEST) {
      this.cartStorage.extractEnergy(CHARGE_USE_PER_REQUEST, false);
      return ACTUAL_FUEL_GAIN_PER_REQUEST;
    }
    return 0;
  }

  @Override
  public Item getDropItem() {
    return RailcraftItems.ELECTRIC_LOCOMOTIVE.get();
  }

  @Override
  public float getOptimalDistance(RollingStock cart) {
    return 0.92F;
  }

  @Override
  protected Container ticketContainer() {
    return this.ticketInventory;
  }

  @Override
  public int getContainerSize() {
    return 2;
  }

  @Override
  public int[] getSlotsForFace(Direction side) {
    return SLOTS;
  }

  @Override
  public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) {
    return this.canPlaceItem(slot, stack);
  }

  @Override
  public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
    return slot == SLOT_TICKET;
  }

  @Override
  public boolean canPlaceItem(int slot, @Nullable ItemStack stack) {
    if (slot == SLOT_TICKET) {
      return TicketItem.FILTER.test(stack);
    }
    return false;
  }

  @Override
  public void tick() {
    super.tick();
    this.cartStorage.tick(this);
  }

  @Override
  protected void moveAlongTrack(BlockPos pos, BlockState state) {
    super.moveAlongTrack(pos, state);
    this.cartStorage.tickOnTrack(this, pos);
  }

  @Override
  public boolean needsFuel() {
    float charge =
        (float) this.cartStorage.getEnergyStored() / (float) this.cartStorage.getMaxEnergyStored();
    return charge < 0.80;
  }

  public IEnergyStorage getBatteryCart() {
    return this.cartStorage;
  }

  @Override
  public void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
    this.cartStorage.receiveEnergy(tag.getInt(CompoundTagKeys.ENERGY), false);
  }

  @Override
  public void addAdditionalSaveData(CompoundTag tag) {
    super.addAdditionalSaveData(tag);
    tag.putInt(CompoundTagKeys.ENERGY, this.cartStorage.getEnergyStored());
  }

  @Override
  protected void loadFromItemStack(ItemStack itemStack) {
    super.loadFromItemStack(itemStack);
    var tag = itemStack.getTag();
    if (tag != null && tag.contains(CompoundTagKeys.ENERGY)) {
      this.cartStorage.receiveEnergy(tag.getInt(CompoundTagKeys.ENERGY), false);
    }
  }

  @Override
  public ItemStack getPickResult() {
    var itemStack = super.getPickResult();
    itemStack.getOrCreateTag().putInt(CompoundTagKeys.ENERGY, this.cartStorage.getEnergyStored());
    return itemStack;
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
    return ForgeCapabilities.ENERGY == capability
        ? this.energyHandler.cast()
        : super.getCapability(capability, facing);
  }

  @Override
  protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
    return new ElectricLocomotiveMenu(id, playerInventory, this);
  }
}

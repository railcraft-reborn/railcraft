package mods.railcraft.world.entity.cart.locomotives;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import javax.annotation.Nullable;

import mods.railcraft.sounds.RailcraftSoundEvents;
import mods.railcraft.util.RailcraftNBTUtil;
import mods.railcraft.util.inventory.InvTools;
import mods.railcraft.util.inventory.wrappers.InventoryMapper;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.entity.cart.LocomotiveEntity;
import mods.railcraft.world.inventory.ElectricLocomotiveMenu;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.TicketItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * The electric locomotive.
 * @author CovertJaguar (https://www.railcraft.info/)
 */
public class ElectricLocomotiveEntity extends LocomotiveEntity implements ISidedInventory {

  // as of 2021 all of the numbers have been increased due to RF/FE usage
  private static final int ACTUAL_FUEL_GAIN_PER_REQUEST = 20; //the original value
  private static final int FUEL_PER_REQUEST = 1;
  // multiplied by 4 because rf
  private static final int CHARGE_USE_PER_REQUEST =
      (ACTUAL_FUEL_GAIN_PER_REQUEST * 4) * FUEL_PER_REQUEST;
  public static final int MAX_CHARGE = 20000;
  private static final int SLOT_TICKET = 0;
  private static final int[] SLOTS = InvTools.buildSlotArray(0, 1);

  private static final Set<Mode> ALLOWED_MODES =
      Collections.unmodifiableSet(EnumSet.of(Mode.RUNNING, Mode.SHUTDOWN));

  private final IInventory ticketInventory =
      new InventoryMapper(this, SLOT_TICKET, 2).ignoreItemChecks();
  private final LazyOptional<IEnergyStorage> cartBattery =
      LazyOptional.of(() -> new EnergyStorage(MAX_CHARGE));

  public ElectricLocomotiveEntity(EntityType<?> type, World world) {
    super(type, world);
  }

  public ElectricLocomotiveEntity(ItemStack itemStack, double x, double y, double z,
      ServerWorld world) {
    super(itemStack, RailcraftEntityTypes.ELECTRIC_LOCOMOTIVE.get(), x, y, z, world);
  }

  @Override
  public Set<Mode> getSupportedModes() {
    return ALLOWED_MODES;
  }

  @Override
  protected int getDefaultPrimaryColor() {
    return DyeColor.YELLOW.getColorValue();
  }

  @Override
  protected int getDefaultSecondaryColor() {
    return DyeColor.BLACK.getColorValue();
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
    return this.cartBattery
        .filter(cart -> cart.getEnergyStored() > CHARGE_USE_PER_REQUEST)
        .map(cart -> {
          cart.extractEnergy(CHARGE_USE_PER_REQUEST, false);
          return ACTUAL_FUEL_GAIN_PER_REQUEST;
        })
        .orElse(0);
  }

  @Override
  public Item getItem() {
    return RailcraftItems.ELECTRIC_LOCOMOTIVE.get();
  }

  @Override
  public float getOptimalDistance(AbstractMinecartEntity cart) {
    return 0.92F;
  }

  @Override
  public void tick() {
    super.tick();
    if (this.level.isClientSide()) {
      return;
    }
  }

  @Override
  protected void moveAlongTrack(BlockPos pos, BlockState state) {
    super.moveAlongTrack(pos, state);
    if (this.level.isClientSide()) {
      return;
    }
  }

  @Override
  protected IInventory getTicketInventory() {
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
    switch (slot) {
      case SLOT_TICKET:
        return TicketItem.FILTER.test(stack);
      default:
        return false;
    }
  }

  public IEnergyStorage getBatteryCart() {
    return this.getCapability(CapabilityEnergy.ENERGY)
        .orElseThrow(IllegalStateException::new);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
    if (CapabilityEnergy.ENERGY == capability) {
      return this.cartBattery.cast();
    }
    return super.getCapability(capability, facing);
  }

  @Override
  public void readAdditionalSaveData(CompoundNBT data) {
    super.readAdditionalSaveData(data);
    this.cartBattery.ifPresent(cell ->
        RailcraftNBTUtil.loadEnergyCell(data.getCompound("battery"), cell));
  }

  @Override
  public void addAdditionalSaveData(CompoundNBT data) {
    super.addAdditionalSaveData(data);
    this.cartBattery.ifPresent(cell ->
        data.put("battery", RailcraftNBTUtil.saveEnergyCell(cell)));
  }

  @Override
  protected void loadFromItemStack(ItemStack itemStack) {
    super.loadFromItemStack(itemStack);
    CompoundNBT tag = itemStack.getTag();

    if (tag.contains("batteryEnergy")) {
      this.cartBattery.ifPresent(cell ->
          cell.receiveEnergy(tag.getInt("batteryEnergy"), false));
    }
  }

  @Override
  public ItemStack getCartItem() {
    ItemStack itemStack = super.getCartItem();
    this.cartBattery.ifPresent(cell -> {
      InvTools.getItemData(itemStack).putInt("batteryEnergy",  cell.getEnergyStored());
    });
    return itemStack;
  }

  @Override
  protected Container createMenu(int id, PlayerInventory playerInventory) {
    return new ElectricLocomotiveMenu(id, playerInventory, this);
  }
}

package mods.railcraft.world.entity.vehicle.locomotive;

import java.util.Set;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.sounds.RailcraftSoundEvents;
import mods.railcraft.util.RailcraftNBTUtil;
import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.inventory.ElectricLocomotiveMenu;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.TicketItem;
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
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * The electric locomotive.
 * 
 * @author CovertJaguar (https://www.railcraft.info/)
 */
public class ElectricLocomotive extends Locomotive implements WorldlyContainer {

  // as of 2021 all of the numbers have been increased due to RF/FE usage
  private static final int ACTUAL_FUEL_GAIN_PER_REQUEST = 20; // the original value
  private static final int FUEL_PER_REQUEST = 1;
  // multiplied by 4 because rf
  private static final int CHARGE_USE_PER_REQUEST =
      (ACTUAL_FUEL_GAIN_PER_REQUEST * 4) * FUEL_PER_REQUEST;
  public static final int MAX_CHARGE = 20000;
  private static final int SLOT_TICKET = 0;
  private static final int[] SLOTS = ContainerTools.buildSlotArray(0, 1);

  private static final Set<Mode> ALLOWED_MODES = Set.of(Mode.RUNNING, Mode.SHUTDOWN);

  private final Container ticketInventory =
      new ContainerMapper(this, SLOT_TICKET, 2).ignoreItemChecks();
  private final LazyOptional<IEnergyStorage> cartBattery =
      LazyOptional.of(() -> new EnergyStorage(MAX_CHARGE));

  public ElectricLocomotive(EntityType<?> type, Level level) {
    super(type, level);
  }

  public ElectricLocomotive(ItemStack itemStack, double x, double y, double z,
      ServerLevel serverLevel) {
    super(itemStack, RailcraftEntityTypes.ELECTRIC_LOCOMOTIVE.get(), x, y, z, serverLevel);
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
  public float getOptimalDistance(RollingStock cart) {
    return 0.92F;
  }

  @Override
  public void tick() {
    super.tick();
  }

  @Override
  protected void moveAlongTrack(BlockPos pos, BlockState state) {
    super.moveAlongTrack(pos, state);
  }

  @Override
  protected Container getTicketInventory() {
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

  @Override
  public boolean needsFuel() {
    float charge = getBatteryCart().getEnergyStored() / getBatteryCart().getMaxEnergyStored();
    return charge < 0.80;
  }

  public IEnergyStorage getBatteryCart() {
    return this.getCapability(ForgeCapabilities.ENERGY)
        .orElseThrow(IllegalStateException::new);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
    if (ForgeCapabilities.ENERGY == capability) {
      return this.cartBattery.cast();
    }
    return super.getCapability(capability, facing);
  }

  @Override
  public void readAdditionalSaveData(CompoundTag data) {
    super.readAdditionalSaveData(data);
    this.cartBattery
        .ifPresent(cell -> RailcraftNBTUtil.loadEnergyCell(data.getCompound("battery"), cell));
  }

  @Override
  public void addAdditionalSaveData(CompoundTag data) {
    super.addAdditionalSaveData(data);
    this.cartBattery.ifPresent(cell -> data.put("battery", RailcraftNBTUtil.saveEnergyCell(cell)));
  }

  @Override
  protected void loadFromItemStack(ItemStack itemStack) {
    super.loadFromItemStack(itemStack);
    CompoundTag tag = itemStack.getTag();

    if (tag.contains("batteryEnergy")) {
      this.cartBattery.ifPresent(cell -> cell.receiveEnergy(tag.getInt("batteryEnergy"), false));
    }
  }

  @Override
  public ItemStack getPickResult() {
    ItemStack itemStack = super.getPickResult();
    this.cartBattery.ifPresent(cell -> {
      itemStack.getOrCreateTag().putInt("batteryEnergy", cell.getEnergyStored());
    });
    return itemStack;
  }

  @Override
  protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
    return new ElectricLocomotiveMenu(id, playerInventory, this);
  }
}

package mods.railcraft.world.entity.vehicle.locomotive;

import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.inventory.SteamLocomotiveMenu;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.TicketItem;
import mods.railcraft.world.level.material.FluidItemHelper;
import mods.railcraft.world.level.material.steam.SolidFuelProvider;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class SteamLocomotive extends BaseSteamLocomotive implements WorldlyContainer {

  private static final int FUEL_SLOT = 3;
  private static final int EXTRA_FUEL_SLOT_A = 4;
  private static final int EXTRA_FUEL_SLOT_B = 5;
  private static final int EXTRA_FUEL_SLOT_C = 6;
  private static final int TICKET_SLOT = 7;
  private static final int[] SLOTS = ContainerTools.buildSlotArray(0, 7);

  private final ContainerMapper fuelContainer = ContainerMapper.make(this, FUEL_SLOT, 1);
  private final ContainerMapper extraFuelContainer =
      ContainerMapper.make(this, EXTRA_FUEL_SLOT_A, 3);
  private final ContainerMapper allFuelContainer = ContainerMapper.make(this, FUEL_SLOT, 4);
  private final ContainerMapper ticketContainer =
      new ContainerMapper(this, TICKET_SLOT, 2).ignoreItemChecks();

  public SteamLocomotive(EntityType<?> type, Level level) {
    super(type, level);

    this.boiler().setFuelProvider(new SolidFuelProvider(this, FUEL_SLOT) {
      @Override
      public float consumeFuel() {
        return SteamLocomotive.this.isShutdown() ? 0.0F : super.consumeFuel();
      }
    });
  }

  public SteamLocomotive(ItemStack itemStack, double x, double y, double z,
      ServerLevel serverLevel) {
    super(itemStack, RailcraftEntityTypes.STEAM_LOCOMOTIVE.get(), x, y, z, serverLevel);

    this.boiler().setFuelProvider(new SolidFuelProvider(this, FUEL_SLOT) {
      @Override
      public float consumeFuel() {
        return SteamLocomotive.this.isShutdown() ? 0.0F : super.consumeFuel();
      }
    });
  }

  @Override
  protected DyeColor getDefaultPrimaryColor() {
    return DyeColor.LIGHT_GRAY;
  }

  @Override
  protected DyeColor getDefaultSecondaryColor() {
    return DyeColor.GRAY;
  }

  @Override
  public void tick() {
    super.tick();

    if (this.level().isClientSide() || this.isRemoved()) {
      return;
    }
    this.extraFuelContainer.moveOneItemTo(this.fuelContainer);
    // fuelInventory.moveOneItemTo(invWaterOutput,
    // (ItemStack item) -> (ForgeHooks.getBurnTime(item) > 0));

    var rollingStock = RollingStock.getOrThrow(this);
    var pulledFuel = rollingStock.pullItem(this.extraFuelContainer::canFit);
    if (!pulledFuel.isEmpty()) {
      this.extraFuelContainer.insert(pulledFuel);
    }
    if (this.isSafeToFill() && this.waterTank.getFluidAmount() < this.waterTank.getCapacity() / 2) {
      var pulledWater = rollingStock.pullFluid(new FluidStack(Fluids.WATER, 1));
      if (pulledWater != null) {
        this.waterTank.fill(pulledWater, IFluidHandler.FluidAction.EXECUTE);
      }
    }
  }

  @Override
  public boolean needsFuel() {
    var water = this.waterTank.getFluid();
    if (water.isEmpty() || water.getAmount() < this.waterTank.getCapacity() / 3) {
      return true;
    }
    int numItems = this.allFuelContainer.countItems(item -> CommonHooks.getBurnTime(item, null) > 0);
    if (numItems == 0) {
      return true;
    }
    int maxItems = this.allFuelContainer.countMaxItemStackSize();
    return (float) numItems / (float) maxItems < 0.25F;
  }

  @Override
  protected Container ticketContainer() {
    return this.ticketContainer;
  }

  @Override
  public int getContainerSize() {
    return 9;
  }

  @Override
  public int[] getSlotsForFace(Direction side) {
    return SLOTS;
  }

  @Override
  public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) {
    return canPlaceItem(slot, stack);
  }

  @Override
  public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
    return slot < TICKET_SLOT;
  }

  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    return switch (slot) {
      case FUEL_SLOT, EXTRA_FUEL_SLOT_A, EXTRA_FUEL_SLOT_B, EXTRA_FUEL_SLOT_C ->
          CommonHooks.getBurnTime(stack, null) > 0;
      case SLOT_WATER_INPUT ->
          // if (FluidItemHelper.getFluidStackInContainer(stack)
          // .filter(fluidStack -> fluidStack.getAmount() > FluidTools.BUCKET_VOLUME).isPresent()) {
          // return false;
          // } we allow tanks instafilling.
          FluidItemHelper.containsFluid(stack, Fluids.WATER);
      case TICKET_SLOT -> TicketItem.FILTER.test(stack);
      default -> false;
    };
  }

  @Override
  public boolean canAcceptPushedItem(RollingStock requester, ItemStack stack) {
    return CommonHooks.getBurnTime(stack, null) > 0;
  }

  @Override
  public boolean canProvidePulledItem(RollingStock requester, ItemStack stack) {
    return false;
  }

  @Override
  protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
    return new SteamLocomotiveMenu(id, playerInventory, this);
  }

  @Override
  protected Item getDropItem() {
    return RailcraftItems.STEAM_LOCOMOTIVE.get();
  }
}

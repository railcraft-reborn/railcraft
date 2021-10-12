package mods.railcraft.world.entity.cart;

import mods.railcraft.api.carts.CartUtil;
import mods.railcraft.util.inventory.InvTools;
import mods.railcraft.util.inventory.filters.StackFilters;
import mods.railcraft.util.inventory.wrappers.InventoryMapper;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.inventory.SteamLocomotiveMenu;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.TicketItem;
import mods.railcraft.world.level.material.fluid.FluidItemHelper;
import mods.railcraft.world.level.material.fluid.FluidTools;
import mods.railcraft.world.level.material.fluid.steam.SolidFuelProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

/**
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public class SteamLocomotiveEntity extends AbstractSteamLocomotiveEntity
    implements ISidedInventory {

  private static final int FUEL_SLOT = 3;
  private static final int EXTRA_FUEL_SLOT_A = 4;
  private static final int EXTRA_FUEL_SLOT_B = 5;
  private static final int EXTRA_SLOT_FUE_SLOTL_C = 6;
  private static final int TICKET_SLOT = 7;
  private static final int[] SLOTS = InvTools.buildSlotArray(0, 7);

  private final InventoryMapper fuelInventory = InventoryMapper.make(this, FUEL_SLOT, 1);
  private final InventoryMapper extraFuelInventory =
      InventoryMapper.make(this, EXTRA_FUEL_SLOT_A, 3);
  private final InventoryMapper invFuel = InventoryMapper.make(this, FUEL_SLOT, 4);
  private final InventoryMapper ticketInventory =
      new InventoryMapper(this, TICKET_SLOT, 2).ignoreItemChecks();

  // private boolean outOfWater = true;

  public SteamLocomotiveEntity(EntityType<?> type, World world) {
    super(type, world);
  }

  public SteamLocomotiveEntity(ItemStack itemStack, double x, double y, double z,
      ServerWorld world) {
    super(itemStack, RailcraftEntityTypes.STEAM_LOCOMOTIVE.get(), x, y, z, world);
  }

  @Override
  protected int getDefaultPrimaryColor() {
    return DyeColor.LIGHT_GRAY.getColorValue();
  }

  @Override
  protected int getDefaultSecondaryColor() {
    return DyeColor.GRAY.getColorValue();
  }

  {
    this.getBoiler().setFuelProvider(new SolidFuelProvider(this, FUEL_SLOT) {
      @Override
      public float consumeFuel() {
        return SteamLocomotiveEntity.this.isShutdown() ? 0.0F : super.consumeFuel();
      }
    });
  }

  @Override
  public void tick() {
    super.tick();

    if (!this.level.isClientSide()) {
      extraFuelInventory.moveOneItemTo(fuelInventory);
      fuelInventory.moveOneItemTo(invWaterOutput, StackFilters.FUEL.negate());
      ItemStack stack =
          CartUtil.transferHelper().pullStack(this, StackFilters.roomIn(extraFuelInventory));
      if (!stack.isEmpty())
        extraFuelInventory.addStack(stack);
      if (isSafeToFill() && waterTank.getFluidAmount() < waterTank.getCapacity() / 2) {
        FluidStack pulled =
            CartUtil.transferHelper().pullFluid(this, new FluidStack(Fluids.WATER, 1));
        if (pulled != null) {
          waterTank.fill(pulled, FluidAction.EXECUTE);
        }
      }
    }
  }

  @Override
  public boolean needsFuel() {
    FluidStack water = waterTank.getFluid();
    if (water == null || water.getAmount() < waterTank.getCapacity() / 3)
      return true;
    int numItems = invFuel.countItems(StackFilters.FUEL);
    if (numItems == 0)
      return true;
    int maxItems = invFuel.countMaxItemStackSize();
    return (float) numItems / (float) maxItems < 0.25F;
  }

  @Override
  protected IInventory getTicketInventory() {
    return ticketInventory;
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
    switch (slot) {
      case FUEL_SLOT:
      case EXTRA_FUEL_SLOT_A:
      case EXTRA_FUEL_SLOT_B:
      case EXTRA_SLOT_FUE_SLOTL_C:
        return StackFilters.FUEL.test(stack);
      case SLOT_WATER_INPUT:
        if (FluidItemHelper.getFluidStackInContainer(stack)
            .filter(fluidStack -> fluidStack.getAmount() > FluidTools.BUCKET_VOLUME).isPresent())
          return false;
        return FluidItemHelper.containsFluid(stack, Fluids.WATER);
      case TICKET_SLOT:
        return TicketItem.FILTER.test(stack);
      default:
        return false;
    }
  }

  @Override
  public boolean canAcceptPushedItem(AbstractMinecartEntity requester, ItemStack stack) {
    return StackFilters.FUEL.test(stack);
  }

  @Override
  public boolean canProvidePulledItem(AbstractMinecartEntity requester, ItemStack stack) {
    return false;
  }

  @Override
  public boolean doesCartMatchFilter(ItemStack stack) {
    return stack.getItem() == RailcraftItems.STEAM_LOCOMOTIVE.get();
  }

  @Override
  protected Container createMenu(int id, PlayerInventory playerInventory) {
    return new SteamLocomotiveMenu(id, playerInventory, this);
  }

  @Override
  public Item getItem() {
    return RailcraftItems.STEAM_LOCOMOTIVE.get();
  }
}

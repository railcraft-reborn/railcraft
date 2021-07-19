package mods.railcraft.world.entity;

import java.util.EnumSet;
import mods.railcraft.sounds.RailcraftSoundEvents;
import mods.railcraft.util.inventory.InvTools;
import mods.railcraft.util.inventory.wrappers.InventoryMapper;
import mods.railcraft.world.inventory.LocomotiveMenu;
import mods.railcraft.world.inventory.RailcraftMenuTypes;
import mods.railcraft.world.item.ItemTicket;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public class CreativeLocomotiveEntity extends LocomotiveEntity implements ISidedInventory {

  private static final int SLOT_TICKET = 0;
  private static final int[] SLOTS = InvTools.buildSlotArray(0, 1);
  private final IInventory invTicket = new InventoryMapper(this, SLOT_TICKET, 2).ignoreItemChecks();

  public CreativeLocomotiveEntity(EntityType<?> type, World world) {
    super(type, world);
  }

  public CreativeLocomotiveEntity(double x, double y, double z, World world) {
    super(RailcraftEntityTypes.CREATIVE_LOCOMOTIVE.get(), x, y, z, world);
  }

  {
    setAllowedModes(EnumSet.of(Mode.RUNNING, Mode.SHUTDOWN));
  }

  @Override
  protected int getDefaultPrimaryColor() {
    return DyeColor.BLACK.getColorValue();
  }

  @Override
  protected int getDefaultSecondaryColor() {
    return DyeColor.MAGENTA.getColorValue();
  }

  @Override
  public SoundEvent getWhistle() {
    return RailcraftSoundEvents.ELECTRIC_WHISTLE.get();
  }

  @Override
  protected int getIdleFuelUse() {
    return 0;
  }

  @Override
  public int getMoreGoJuice() {
    return 100;
  }

  @Override
  public Item getItem() {
    return RailcraftItems.CREATIVE_LOCOMOTIVE.get();
  }

  @Override
  public float getOptimalDistance(AbstractMinecartEntity cart) {
    return 0.92f;
  }

  @Override
  protected IInventory getTicketInventory() {
    return invTicket;
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
    return canPlaceItem(slot, stack);
  }

  @Override
  public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
    return slot == SLOT_TICKET;
  }

  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    switch (slot) {
      case SLOT_TICKET:
        return ItemTicket.FILTER.test(stack);
      default:
        return false;
    }
  }

  @Override
  public ItemStack[] getItemsDropped(AbstractMinecartEntity cart) {
    return new ItemStack[0]; // Prevent survival players from getting admin tools
  }

  @Override
  public boolean doesCartMatchFilter(ItemStack stack) {
    return this.getCartItem().getItem() == stack.getItem();
  }

  @Override
  public ItemStack getContents() {
    return ItemStack.EMPTY;
  }

  @Override
  protected Container createMenu(int id, PlayerInventory playerInventory) {
    return new LocomotiveMenu<>(RailcraftMenuTypes.CREATIVE_LOCOMOTIVE.get(), id,
        playerInventory, this);
  }
}

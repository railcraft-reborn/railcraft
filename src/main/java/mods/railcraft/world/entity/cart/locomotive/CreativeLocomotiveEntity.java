package mods.railcraft.world.entity.cart.locomotive;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import mods.railcraft.sounds.RailcraftSoundEvents;
import mods.railcraft.util.inventory.InvTools;
import mods.railcraft.util.inventory.wrappers.InventoryMapper;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.inventory.LocomotiveMenu;
import mods.railcraft.world.inventory.RailcraftMenuTypes;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.TicketItem;
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
import net.minecraft.world.server.ServerWorld;

/**
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public class CreativeLocomotiveEntity extends LocomotiveEntity implements ISidedInventory {

  private static final int SLOT_TICKET = 0;
  private static final int[] SLOTS = InvTools.buildSlotArray(0, 1);

  private static final Set<Mode> ALLOWED_MODES =
      Collections.unmodifiableSet(EnumSet.of(Mode.RUNNING, Mode.SHUTDOWN));

  private final IInventory invTicket = new InventoryMapper(this, SLOT_TICKET, 2).ignoreItemChecks();

  public CreativeLocomotiveEntity(EntityType<?> type, World world) {
    super(type, world);
  }

  public CreativeLocomotiveEntity(ItemStack itemStack, double x, double y, double z,
      ServerWorld level) {
    super(itemStack, RailcraftEntityTypes.CREATIVE_LOCOMOTIVE.get(), x, y, z, level);
  }

  @Override
  public Set<Mode> getSupportedModes() {
    return ALLOWED_MODES;
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
  public SoundEvent getWhistleSound() {
    return RailcraftSoundEvents.ELECTRIC_WHISTLE.get();
  }

  @Override
  protected int getIdleFuelUse() {
    return 0;
  }

  @Override
  public int retrieveFuel() {
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
        return TicketItem.FILTER.test(stack);
      default:
        return false;
    }
  }

  @Override
  protected Container createMenu(int id, PlayerInventory playerInventory) {
    return new LocomotiveMenu<>(RailcraftMenuTypes.CREATIVE_LOCOMOTIVE.get(), id,
        playerInventory, this);
  }
}

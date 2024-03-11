package mods.railcraft.world.entity.vehicle.locomotive;

import java.util.Set;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.sounds.RailcraftSoundEvents;
import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.inventory.CreativeLocomotiveMenu;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.TicketItem;
import net.minecraft.core.Direction;
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

public class CreativeLocomotive extends Locomotive implements WorldlyContainer {

  private static final int SLOT_TICKET = 0;
  private static final int[] SLOTS = ContainerTools.buildSlotArray(0, 1);

  private static final Set<Mode> ALLOWED_MODES = Set.of(Mode.RUNNING, Mode.SHUTDOWN);

  private final Container invTicket = new ContainerMapper(this, SLOT_TICKET, 2).ignoreItemChecks();

  public CreativeLocomotive(EntityType<?> type, Level level) {
    super(type, level);
  }

  public CreativeLocomotive(ItemStack itemStack, double x, double y, double z,
      ServerLevel level) {
    super(itemStack, RailcraftEntityTypes.CREATIVE_LOCOMOTIVE.get(), x, y, z, level);
  }

  @Override
  public Set<Mode> getSupportedModes() {
    return ALLOWED_MODES;
  }

  @Override
  protected DyeColor getDefaultPrimaryColor() {
    return DyeColor.BLACK;
  }

  @Override
  protected DyeColor getDefaultSecondaryColor() {
    return DyeColor.MAGENTA;
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
  public Item getDropItem() {
    return RailcraftItems.CREATIVE_LOCOMOTIVE.get();
  }

  @Override
  public float getOptimalDistance(RollingStock cart) {
    return 0.92f;
  }

  @Override
  protected Container ticketContainer() {
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
    if (slot == SLOT_TICKET) {
      return TicketItem.FILTER.test(stack);
    }
    return false;
  }

  @Override
  public boolean needsFuel() {
    return false;
  }

  @Override
  public Speed getMaxReverseSpeed() {
    return Speed.MAX;
  }

  @Override
  protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
    return new CreativeLocomotiveMenu(id, playerInventory, this);
  }
}

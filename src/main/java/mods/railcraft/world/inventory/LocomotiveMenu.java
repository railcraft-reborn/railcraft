package mods.railcraft.world.inventory;

import java.util.Collections;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import mods.railcraft.world.inventory.slot.ItemFilterSlot;
import mods.railcraft.world.inventory.slot.UnmodifiableSlot;
import mods.railcraft.world.item.TicketItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;

public abstract class LocomotiveMenu<T extends Locomotive> extends RailcraftMenu {

  public static final int DEFAULT_HEIGHT = 161;

  private final T locomotive;

  public LocomotiveMenu(@Nullable MenuType<?> type, int id, Inventory playerInv, T locomotive) {
    this(type, id, playerInv, locomotive, DEFAULT_HEIGHT);
  }

  protected LocomotiveMenu(@Nullable MenuType<?> type, int id, Inventory inventory,
      T locomotive, int guiHeight) {
    super(type, id, inventory.player, locomotive::stillValid);
    this.locomotive = locomotive;
    var slotTicket = new ItemFilterSlot(TicketItem.FILTER, locomotive,
        locomotive.getContainerSize() - 2, 116, guiHeight - 111).setStackLimit(1);
    slotTicket.setTooltip(Collections.singletonList(
            Component.translatable(Translations.Tips.LOCOMOTIVE_SLOT_TICKET)));
    this.addSlot(slotTicket);
    // TODO: make some way to clear this?
    this.addSlot(
        new UnmodifiableSlot(locomotive, locomotive.getContainerSize() - 1, 134, guiHeight - 111));
  }

  public T getLocomotive() {
    return this.locomotive;
  }

  @Override
  public boolean stillValid(Player player) {
    return true;
  }
}

package mods.railcraft.world.inventory;

import java.util.Collections;
import javax.annotation.Nullable;
import mods.railcraft.world.entity.cart.locomotive.CreativeLocomotiveEntity;
import mods.railcraft.world.entity.cart.locomotive.LocomotiveEntity;
import mods.railcraft.world.item.TicketItem;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;

public class LocomotiveMenu<T extends LocomotiveEntity> extends RailcraftMenu {

  public static final int DEFAULT_HEIGHT = 161;

  private final T locomotive;
  public String ownerName;

  public LocomotiveMenu(@Nullable MenuType<?> type, int id, Inventory playerInv,
      T locomotive) {
    this(type, id, playerInv, locomotive, DEFAULT_HEIGHT);
  }

  protected LocomotiveMenu(@Nullable MenuType<?> type, int id, Inventory playerInv,
      T locomotive, int guiHeight) {
    super(type, id, playerInv);
    this.locomotive = locomotive;
    SlotRailcraft slotTicket = new SlotStackFilter(TicketItem.FILTER, locomotive,
        locomotive.getContainerSize() - 2, 116, guiHeight - 111).setStackLimit(1);
    slotTicket.setTooltip(
        Collections.singletonList(
            new TranslatableComponent("gui.railcraft.locomotive.tips.slot.ticket")));
    this.addSlot(slotTicket);
    // TODO: make some way to clear this?
    this.addSlot(
        new SlotUntouchable(locomotive, locomotive.getContainerSize() - 1, 134, guiHeight - 111));

    this.addPlayerSlots(playerInv, guiHeight);
  }

  public static LocomotiveMenu<CreativeLocomotiveEntity> creative(int id,
      Inventory inventory, CreativeLocomotiveEntity entity) {
    return new LocomotiveMenu<>(
        RailcraftMenuTypes.CREATIVE_LOCOMOTIVE.get(), id, inventory, entity);
  }

  public T getLocomotive() {
    return this.locomotive;
  }

  @Override
  public boolean stillValid(Player playerEntity) {
    return true;
  }
}

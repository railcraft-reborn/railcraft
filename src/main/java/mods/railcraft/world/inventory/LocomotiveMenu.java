package mods.railcraft.world.inventory;

import java.util.Collections;
import javax.annotation.Nullable;
import mods.railcraft.world.entity.cart.locomotive.CreativeLocomotiveEntity;
import mods.railcraft.world.entity.cart.locomotive.LocomotiveEntity;
import mods.railcraft.world.item.TicketItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;

public class LocomotiveMenu<T extends LocomotiveEntity> extends RailcraftMenu {

  public static final int DEFAULT_HEIGHT = 161;

  private final T locomotive;
  public String ownerName;

  public LocomotiveMenu(@Nullable ContainerType<?> type, int id, PlayerInventory playerInv,
      T locomotive) {
    this(type, id, playerInv, locomotive, DEFAULT_HEIGHT);
  }

  protected LocomotiveMenu(@Nullable ContainerType<?> type, int id, PlayerInventory playerInv,
      T locomotive, int guiHeight) {
    super(type, id, playerInv);
    this.locomotive = locomotive;
    SlotRailcraft slotTicket = new SlotStackFilter(TicketItem.FILTER, locomotive,
        locomotive.getContainerSize() - 2, 116, guiHeight - 111).setStackLimit(1);
    slotTicket.setTooltip(
        Collections.singletonList(
            new TranslationTextComponent("gui.railcraft.locomotive.tips.slot.ticket")));
    this.addSlot(slotTicket);
    // TODO: make some way to clear this?
    this.addSlot(
        new SlotUntouchable(locomotive, locomotive.getContainerSize() - 1, 134, guiHeight - 111));

    this.addPlayerSlots(playerInv, guiHeight);
  }

  public static LocomotiveMenu<CreativeLocomotiveEntity> creative(int id,
      PlayerInventory playerInventory, PacketBuffer data) {
    int entityId = data.readVarInt();
    Entity entity = playerInventory.player.level.getEntity(entityId);
    if (entity instanceof CreativeLocomotiveEntity) {
      return new LocomotiveMenu<CreativeLocomotiveEntity>(
          RailcraftMenuTypes.CREATIVE_LOCOMOTIVE.get(), id,
          playerInventory, (CreativeLocomotiveEntity) entity);
    }
    throw new IllegalStateException("Cannot find locomotive with ID: " + entityId);
  }

  public T getLocomotive() {
    return this.locomotive;
  }

  @Override
  public void updateString(byte id, String data) {

  }

  @Override
  public boolean stillValid(PlayerEntity playerEntity) {
    return true;
  }
}

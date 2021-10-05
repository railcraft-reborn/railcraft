package mods.railcraft.world.inventory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import mods.railcraft.gui.widget.Widget;
import mods.railcraft.network.PacketBuilder;
import mods.railcraft.util.inventory.InvTools;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public abstract class RailcraftMenu extends Container {

  private final PlayerEntity player;

  private final Predicate<PlayerEntity> isUsableByPlayer;
  private final List<Widget> widgets = new ArrayList<>();

  private final Set<ServerPlayerEntity> syncListeners = new HashSet<>();

  protected RailcraftMenu(@Nullable ContainerType<?> type, int id,
      PlayerInventory playerInventory) {
    this(type, id, playerInventory.player, playerInventory::stillValid);
  }

  protected RailcraftMenu(@Nullable ContainerType<?> type, int id, PlayerEntity player,
      Predicate<PlayerEntity> isUsableByPlayer) {
    super(type, id);
    this.player = player;
    this.isUsableByPlayer = isUsableByPlayer;
  }

  @Override
  public void addSlotListener(IContainerListener listener) {
    super.addSlotListener(listener);
    if (listener instanceof ServerPlayerEntity) {
      this.syncListeners.add((ServerPlayerEntity) listener);
    }
  }

  @Override
  public void removeSlotListener(IContainerListener listener) {
    super.removeSlotListener(listener);
    if (listener instanceof ServerPlayerEntity) {
      this.syncListeners.remove((ServerPlayerEntity) listener);
    }
  }

  public List<Widget> getWidgets() {
    return this.widgets;
  }

  protected final void addPlayerSlots(PlayerInventory invPlayer) {
    addPlayerSlots(invPlayer, 166);
  }

  protected final void addPlayerSlots(PlayerInventory invPlayer, int guiHeight) {
    for (int i = 0; i < 3; i++) {
      for (int k = 0; k < 9; k++) {
        addSlot(new Slot(invPlayer, k + i * 9 + 9, 8 + k * 18, guiHeight - 82 + i * 18));
      }
    }
    for (int j = 0; j < 9; j++) {
      addSlot(new Slot(invPlayer, j, 8 + j * 18, guiHeight - 24));
    }
  }

  public void addWidget(Widget widget) {
    widgets.add(widget);
    widget.addToContainer(this);
  }

  @Override
  public final void broadcastChanges() {
    super.broadcastChanges();
    if (!this.player.level.isClientSide()) {
      sendUpdateToClient();
      sendWidgetsServerData();
    }
  }

  private void sendWidgetsServerData() {
    widgets.forEach(this::sendWidgetServerData);
  }

  private void sendWidgetServerData(Widget widget) {
    this.syncListeners
        .forEach(l -> PacketBuilder.instance().sendGuiWidgetPacket(l, this.containerId, widget));
  }

  public void sendUpdateToClient() {}

  public void updateString(byte id, String data) {}

  public void updateData(byte id, PacketBuffer data) {}

  @Override
  public boolean isSynched(PlayerEntity entityplayer) {
    return isUsableByPlayer.test(entityplayer);
  }

  // TODO: test new parameters
  @Override
  public ItemStack clicked(int slotId, int mouseButton, ClickType clickType,
      PlayerEntity player) {
    Slot slot = slotId < 0 ? null : this.slots.get(slotId);
    if (slot instanceof SlotRailcraft && ((SlotRailcraft) slot).isPhantom())
      return slotClickPhantom((SlotRailcraft) slot, mouseButton, clickType, player);
    return super.clicked(slotId, mouseButton, clickType, player);
  }

  protected ItemStack slotClickPhantom(SlotRailcraft slot, int mouseButton, ClickType clickType,
      PlayerEntity player) {
    ItemStack stack = ItemStack.EMPTY;

    if (mouseButton == 2) {
      if (slot.canAdjustPhantom())
        slot.set(ItemStack.EMPTY);
    } else if (mouseButton == 0 || mouseButton == 1) {
      PlayerInventory playerInv = player.inventory;
      slot.setChanged();
      ItemStack stackSlot = slot.getItem();
      ItemStack stackHeld = playerInv.getCarried();

      if (!stackSlot.isEmpty())
        stack = stackSlot.copy();

      if (stackSlot.isEmpty()) {
        if (!stackHeld.isEmpty() && slot.mayPlace(stackHeld))
          fillPhantomSlot(slot, stackHeld, mouseButton);
      } else if (stackHeld.isEmpty()) {
        adjustPhantomSlot(slot, mouseButton, clickType);
        slot.onTake(player, playerInv.getCarried());
      } else if (slot.mayPlace(stackHeld))
        if (InvTools.isItemEqual(stackSlot, stackHeld))
          adjustPhantomSlot(slot, mouseButton, clickType);
        else
          fillPhantomSlot(slot, stackHeld, mouseButton);
    }
    return stack;
  }

  protected void adjustPhantomSlot(SlotRailcraft slot, int mouseButton, ClickType clickType) {
    if (!slot.canAdjustPhantom())
      return;
    ItemStack stackSlot = slot.getItem();
    if (stackSlot.isEmpty())
      return;
    int stackSize;
    if (clickType == ClickType.QUICK_MOVE)
      stackSize =
          mouseButton == 0 ? (stackSlot.getCount() + 1) / 2 : stackSlot.getCount() * 2;
    else
      stackSize =
          mouseButton == 0 ? stackSlot.getCount() - 1 : stackSlot.getCount() + 1;

    if (stackSize > slot.getMaxStackSize())
      stackSize = slot.getMaxStackSize();

    InvTools.setSize(stackSlot, stackSize);

    if (stackSlot.isEmpty())
      slot.set(ItemStack.EMPTY);
  }

  protected void fillPhantomSlot(SlotRailcraft slot, ItemStack stackHeld, int mouseButton) {
    if (!slot.canAdjustPhantom())
      return;
    int stackSize = mouseButton == 0 ? stackHeld.getCount() : 1;
    if (stackSize > slot.getMaxStackSize())
      stackSize = slot.getMaxStackSize();
    ItemStack phantomStack = stackHeld.copy();
    InvTools.setSize(phantomStack, stackSize);

    slot.set(phantomStack);
  }

  protected boolean shiftItemStack(ItemStack stackToShift, int start, int end) {
    boolean changed = false;
    if (stackToShift.isStackable())
      for (int slotIndex = start; !stackToShift.isEmpty() && slotIndex < end; slotIndex++) {
        Slot slot = this.slots.get(slotIndex);
        ItemStack stackInSlot = slot.getItem();
        if (!stackInSlot.isEmpty() && InvTools.isItemEqual(stackInSlot, stackToShift)) {
          int resultingStackSize = stackInSlot.getCount() + stackToShift.getCount();
          int max = Math.min(stackToShift.getMaxStackSize(), slot.getMaxStackSize());
          if (resultingStackSize <= max) {
            InvTools.setSize(stackToShift, 0);
            InvTools.setSize(stackInSlot, resultingStackSize);
            slot.setChanged();
            changed = true;
          } else if (stackInSlot.getCount() < max) {
            InvTools.decSize(stackToShift, max - stackInSlot.getCount());
            InvTools.setSize(stackInSlot, max);
            slot.setChanged();
            changed = true;
          }
        }
      }
    if (!stackToShift.isEmpty())
      for (int slotIndex = start; !stackToShift.isEmpty() && slotIndex < end; slotIndex++) {
        Slot slot = this.slots.get(slotIndex);
        ItemStack stackInSlot = slot.getItem();
        if (stackInSlot.isEmpty()) {
          int max = Math.min(stackToShift.getMaxStackSize(), slot.getMaxStackSize());
          stackInSlot = stackToShift.copy();
          InvTools.setSize(stackInSlot, Math.min(stackToShift.getCount(), max));
          InvTools.decSize(stackToShift, stackInSlot.getCount());
          slot.set(stackInSlot);
          slot.setChanged();
          changed = true;
        }
      }
    return changed;
  }

  protected boolean tryShiftItem(ItemStack stackToShift, int numSlots) {
    for (int machineIndex = 0; machineIndex < numSlots - 9 * 4; machineIndex++) {
      Slot slot = this.slots.get(machineIndex);
      if (slot instanceof SlotRailcraft) {
        SlotRailcraft slotRailcraft = (SlotRailcraft) slot;
        if (slotRailcraft.isPhantom())
          continue;
        if (!slotRailcraft.canShift())
          continue;
      }
      if (!slot.mayPlace(stackToShift))
        continue;
      if (shiftItemStack(stackToShift, machineIndex, machineIndex + 1))
        return true;
    }
    return false;
  }

  @Override
  public ItemStack quickMoveStack(PlayerEntity player, int slotIndex) {
    ItemStack originalStack = ItemStack.EMPTY;
    Slot slot = this.slots.get(slotIndex);
    int numSlots = this.slots.size();
    if (slot != null && slot.hasItem()) {
      ItemStack stackInSlot = slot.getItem();
      assert !stackInSlot.isEmpty();
      originalStack = stackInSlot.copy();
      if (!(slotIndex >= numSlots - 9 * 4 && tryShiftItem(stackInSlot, numSlots))) {
        if (slotIndex >= numSlots - 9 * 4 && slotIndex < numSlots - 9) {
          if (!shiftItemStack(stackInSlot, numSlots - 9, numSlots))
            return ItemStack.EMPTY;
        } else if (slotIndex >= numSlots - 9) {
          if (!shiftItemStack(stackInSlot, numSlots - 9 * 4, numSlots - 9))
            return ItemStack.EMPTY;
        } else if (!shiftItemStack(stackInSlot, numSlots - 9 * 4, numSlots))
          return ItemStack.EMPTY;
      }
      slot.onQuickCraft(stackInSlot, originalStack);
      if (stackInSlot.isEmpty())
        slot.set(ItemStack.EMPTY);
      else
        slot.setChanged();
      if (stackInSlot.getCount() == originalStack.getCount())
        return ItemStack.EMPTY;
      slot.onTake(player, stackInSlot);
    }
    return originalStack;
  }
}

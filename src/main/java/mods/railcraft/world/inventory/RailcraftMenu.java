package mods.railcraft.world.inventory;

import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.gui.widget.Widget;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.SyncWidgetMessage;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.world.inventory.slot.RailcraftSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public abstract class RailcraftMenu extends AbstractContainerMenu {

  private final Player player;

  private final Predicate<Player> validator;
  private final List<Widget> widgets = new ArrayList<>();

  protected RailcraftMenu(@Nullable MenuType<?> type, int id, Player player,
      Predicate<Player> validator) {
    super(type, id);
    this.player = player;
    this.validator = validator;
  }

  @Override
  public boolean stillValid(Player player) {
    return this.validator.test(player);
  }

  public List<Widget> getWidgets() {
    return this.widgets;
  }

  protected final void addInventorySlots(Inventory inventory) {
    this.addInventorySlots(inventory, 166);
  }

  protected final void addInventorySlots(Inventory inventory, int guiHeight) {
    for (int i = 0; i < 3; i++) {
      for (int k = 0; k < 9; k++) {
        this.addSlot(new Slot(inventory, k + i * 9 + 9, 8 + k * 18, guiHeight - 82 + i * 18));
      }
    }
    for (int j = 0; j < 9; j++) {
      this.addSlot(new Slot(inventory, j, 8 + j * 18, guiHeight - 24));
    }
  }

  public void addWidget(Widget widget) {
    this.widgets.add(widget);
    widget.added(this);
  }

  @Override
  public final void broadcastChanges() {
    super.broadcastChanges();
    if (this.player instanceof ServerPlayer serverPlayer) {
      this.widgets.forEach(widget -> this.sendWidgetPacket(serverPlayer, widget));
    }
  }

  private void sendWidgetPacket(ServerPlayer player, Widget widget) {
    if (widget.requiresSync(player)) {
      var byteBuf = new FriendlyByteBuf(Unpooled.buffer());
      try {
        widget.writeToBuf(player, byteBuf);
        var message = new SyncWidgetMessage(this.containerId, widget.getId(), byteBuf);
        NetworkChannel.GAME.sendTo(message, player);
      } finally {
        byteBuf.release();
      }
    }
  }

  @Override
  public void clicked(int slotId, int mouseButton, ClickType clickType, Player player) {
    Slot slot = slotId < 0 ? null : this.slots.get(slotId);
    if (slot instanceof RailcraftSlot railcraftSlot && railcraftSlot.isPhantom()) {
      this.slotClickPhantom(railcraftSlot, mouseButton, clickType, player);
    }
    super.clicked(slotId, mouseButton, clickType, player);
  }

  protected void slotClickPhantom(RailcraftSlot slot, int mouseButton,
      ClickType clickType, Player player) {

    if (mouseButton == 2) {
      if (slot.canAdjustPhantom()) {
        slot.set(ItemStack.EMPTY);
      }
    } else if (mouseButton == 0 || mouseButton == 1) {
      Inventory playerInv = player.getInventory();
      slot.setChanged();
      ItemStack stackSlot = slot.getItem();
      ItemStack stackHeld = playerInv.getSelected();

      if (stackSlot.isEmpty()) {
        if (!stackHeld.isEmpty() && slot.mayPlace(stackHeld)) {
          fillPhantomSlot(slot, stackHeld, mouseButton);
        }
      } else if (stackHeld.isEmpty()) {
        adjustPhantomSlot(slot, mouseButton, clickType);
        slot.onTake(player, playerInv.getSelected());
      } else if (slot.mayPlace(stackHeld)) {
        if (ContainerTools.isItemEqual(stackSlot, stackHeld)) {
          adjustPhantomSlot(slot, mouseButton, clickType);
        } else {
          fillPhantomSlot(slot, stackHeld, mouseButton);
        }
      }
    }
  }

  protected void adjustPhantomSlot(RailcraftSlot slot, int mouseButton, ClickType clickType) {
    if (!slot.canAdjustPhantom()) {
      return;
    }
    ItemStack stackSlot = slot.getItem();
    if (stackSlot.isEmpty()) {
      return;
    }
    int stackSize;
    if (clickType == ClickType.QUICK_MOVE) {
      stackSize = mouseButton == 0 ? (stackSlot.getCount() + 1) / 2 : stackSlot.getCount() * 2;
    } else {
      stackSize = mouseButton == 0 ? stackSlot.getCount() - 1 : stackSlot.getCount() + 1;
    }

    if (stackSize > slot.getMaxStackSize()) {
      stackSize = slot.getMaxStackSize();
    }

    stackSlot.setCount(stackSize);

    if (stackSlot.isEmpty()) {
      slot.set(ItemStack.EMPTY);
    }
  }

  protected void fillPhantomSlot(RailcraftSlot slot, ItemStack stackHeld, int mouseButton) {
    if (!slot.canAdjustPhantom()) {
      return;
    }
    int stackSize = mouseButton == 0 ? stackHeld.getCount() : 1;
    if (stackSize > slot.getMaxStackSize()) {
      stackSize = slot.getMaxStackSize();
    }
    var phantomStack = stackHeld.copy();
    phantomStack.setCount(stackSize);

    slot.set(phantomStack);
  }

  protected boolean shiftItemStack(ItemStack stackToShift, int start, int end) {
    boolean changed = false;
    if (stackToShift.isStackable()) {
      for (int slotIndex = start; !stackToShift.isEmpty() && slotIndex < end; slotIndex++) {
        Slot slot = this.slots.get(slotIndex);
        ItemStack stackInSlot = slot.getItem();
        if (!stackInSlot.isEmpty() && ContainerTools.isItemEqual(stackInSlot, stackToShift)) {
          int resultingStackSize = stackInSlot.getCount() + stackToShift.getCount();
          int max = Math.min(stackToShift.getMaxStackSize(), slot.getMaxStackSize());
          if (resultingStackSize <= max) {
            stackToShift.setCount(0);
            stackInSlot.setCount(resultingStackSize);
            slot.setChanged();
            changed = true;
          } else if (stackInSlot.getCount() < max) {
            stackToShift.shrink(max - stackInSlot.getCount());
            stackInSlot.setCount(max);
            slot.setChanged();
            changed = true;
          }
        }
      }
    }
    if (!stackToShift.isEmpty()) {
      for (int slotIndex = start; !stackToShift.isEmpty() && slotIndex < end; slotIndex++) {
        Slot slot = this.slots.get(slotIndex);
        ItemStack stackInSlot = slot.getItem();
        if (stackInSlot.isEmpty()) {
          int max = Math.min(stackToShift.getMaxStackSize(), slot.getMaxStackSize());
          stackInSlot = stackToShift.copy();
          stackInSlot.setCount(Math.min(stackToShift.getCount(), max));
          stackToShift.shrink(stackInSlot.getCount());
          slot.set(stackInSlot);
          slot.setChanged();
          changed = true;
        }
      }
    }
    return changed;
  }

  protected boolean tryShiftItem(ItemStack stackToShift, int numSlots) {
    for (int machineIndex = 0; machineIndex < numSlots - 9 * 4; machineIndex++) {
      Slot slot = this.slots.get(machineIndex);
      if (slot instanceof RailcraftSlot slotRailcraft) {
        if (slotRailcraft.isPhantom()) {
          continue;
        }
        if (!slotRailcraft.canShift()) {
          continue;
        }
      }
      if (!slot.mayPlace(stackToShift)) {
        continue;
      }
      if (shiftItemStack(stackToShift, machineIndex, machineIndex + 1)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public ItemStack quickMoveStack(Player player, int slotIndex) {
    ItemStack originalStack = ItemStack.EMPTY;
    Slot slot = this.slots.get(slotIndex);
    int numSlots = this.slots.size();
    if (slot != null && slot.hasItem()) {
      ItemStack stackInSlot = slot.getItem();
      assert !stackInSlot.isEmpty();
      originalStack = stackInSlot.copy();
      if (!(slotIndex >= numSlots - 9 * 4 && tryShiftItem(stackInSlot, numSlots))) {
        if (slotIndex >= numSlots - 9 * 4 && slotIndex < numSlots - 9) {
          if (!shiftItemStack(stackInSlot, numSlots - 9, numSlots)) {
            return ItemStack.EMPTY;
          }
        } else if (slotIndex >= numSlots - 9) {
          if (!shiftItemStack(stackInSlot, numSlots - 9 * 4, numSlots - 9)) {
            return ItemStack.EMPTY;
          }
        } else if (!shiftItemStack(stackInSlot, numSlots - 9 * 4, numSlots)) {
          return ItemStack.EMPTY;
        }
      }
      slot.onQuickCraft(stackInSlot, originalStack); // we should not call this?
      if (stackInSlot.isEmpty()) {
        slot.set(ItemStack.EMPTY);
      } else {
        slot.setChanged();
      }
      if (stackInSlot.getCount() == originalStack.getCount()) {
        return ItemStack.EMPTY;
      }
      slot.onTake(player, stackInSlot);
    }
    return originalStack;
  }
}

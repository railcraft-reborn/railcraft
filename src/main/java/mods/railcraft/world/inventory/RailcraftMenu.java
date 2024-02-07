package mods.railcraft.world.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import io.netty.buffer.Unpooled;
import mods.railcraft.gui.widget.Widget;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.SyncWidgetMessage;
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

public abstract class RailcraftMenu extends AbstractContainerMenu {

  private final Player player;

  private final Predicate<Player> validator;
  private final List<Widget> widgets = new ArrayList<>();

  protected RailcraftMenu(MenuType<?> type, int id, Player player, Predicate<Player> validator) {
    super(type, id);
    this.player = player;
    this.validator = validator;
  }

  public Player getPlayer() {
    return player;
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
    System.out.println(slotId);
    if (slotId >= 0) {
      var slot = this.slots.get(slotId);
      if (slot instanceof RailcraftSlot railcraftSlot && railcraftSlot.isPhantom()) {
        this.slotClickPhantom(railcraftSlot, mouseButton, clickType, player);
      }
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
      var containerMenu = player.containerMenu;
      slot.setChanged();
      ItemStack stackSlot = slot.getItem();
      ItemStack stackHeld = containerMenu.getCarried();

      if (stackSlot.isEmpty() && !stackHeld.isEmpty() && slot.mayPlace(stackHeld)) {
        fillPhantomSlot(slot, stackHeld, mouseButton);
      } else if (stackHeld.isEmpty()) {
        adjustPhantomSlot(slot, mouseButton, clickType);
        slot.onTake(player, containerMenu.getCarried());
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

  protected boolean tryMoveItemStackTo(ItemStack stack, int endIndex) {
    boolean flag = false;
    int x = 0;

    Slot slot;
    ItemStack itemstack;
    if (stack.isStackable()) {
      while(!stack.isEmpty() && x < endIndex) {
        slot = this.slots.get(x);
        itemstack = slot.getItem();
        if (!itemstack.isEmpty() && ItemStack.isSameItemSameTags(stack, itemstack)) {
          int j = itemstack.getCount() + stack.getCount();
          int maxSize = Math.min(slot.getMaxStackSize(), stack.getMaxStackSize());
          if (j <= maxSize) {
            stack.setCount(0);
            itemstack.setCount(j);
            slot.setChanged();
            flag = true;
          } else if (itemstack.getCount() < maxSize) {
            stack.shrink(maxSize - itemstack.getCount());
            itemstack.setCount(maxSize);
            slot.setChanged();
            flag = true;
          }
        }
        ++x;
      }
    }

    if (!stack.isEmpty()) {
      for (int i = 0; i < endIndex; i++) {
        slot = this.slots.get(i);
        itemstack = slot.getItem();
        if (itemstack.isEmpty() && mayPlace(slot, stack)) {
          if (stack.getCount() > slot.getMaxStackSize()) {
            slot.setByPlayer(stack.split(slot.getMaxStackSize()));
          } else {
            slot.setByPlayer(stack.split(stack.getCount()));
          }

          slot.setChanged();
          flag = true;
          break;
        }
      }
    }

    return flag;
  }

  private static boolean mayPlace(Slot slot, ItemStack itemStack) {
    if (slot instanceof RailcraftSlot slotRailcraft) {
      if (slotRailcraft.isPhantom()) {
        return false;
      }
      if (!slotRailcraft.canShift()) {
        return false;
      }
    }
    return slot.mayPlace(itemStack);
  }

  @Override
  public ItemStack quickMoveStack(Player player, int slotIndex) {
    ItemStack originalStack = ItemStack.EMPTY;
    Slot slot = this.slots.get(slotIndex);
    final int numSlots = this.slots.size();//47
    final int slotsAdded = numSlots - 9 * 4;
    if (slot.hasItem()) {
      ItemStack stackInSlot = slot.getItem();
      originalStack = stackInSlot.copy();
      if (slotIndex < slotsAdded) { // Custom slots to vanilla inventory slots
        if (!this.moveItemStackTo(stackInSlot, slotsAdded, numSlots, false)) {
          return ItemStack.EMPTY;
        }
      } else { // Vanilla inventory slots to custom slots
        if (!this.tryMoveItemStackTo(stackInSlot, slotsAdded)) {
          return ItemStack.EMPTY;
        }
      }

      if (stackInSlot.isEmpty()) {
        slot.set(ItemStack.EMPTY);
      } else {
        slot.setChanged();
      }
    }
    return originalStack;
  }
}

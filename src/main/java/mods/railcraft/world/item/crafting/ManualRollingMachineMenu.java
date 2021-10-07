package mods.railcraft.world.item.crafting;

import java.util.Optional;

import javax.annotation.Nullable;

import mods.railcraft.world.inventory.RailcraftMenuTypes;
import mods.railcraft.world.level.block.entity.ManualRollingMachineBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.world.World;

/**
 * cannot extend using WorkbenchContainerClass, all of the good vars are privated AND our table is
 * locked
 */
public class ManualRollingMachineMenu extends Container {

  private final CraftingInventory craftSlots = new CraftingInventory(this, 3, 3);
  private final CraftResultInventory resultSlots = new CraftResultInventory();
  private final CraftResultInventory resultSlotClickyBox = new CraftResultInventory();
  private final RollingResultSlot craftingResultSlot;
  private final World level;
  private final PlayerEntity player;
  private ItemStack cachedFinishItem;
  // KEY INFO:
  // 1. required time | 2. currentTick (UNSETTABLE)
  // 3. shouldFire - 1 == true
  private final IIntArray data;

  public ManualRollingMachineMenu(int containerID, PlayerInventory playerInventory) {
    this(containerID, playerInventory, new IntArray(3), null);
  }

  public ManualRollingMachineMenu(int containerID, PlayerInventory playerInventory, IIntArray data,
      @Nullable ManualRollingMachineBlockEntity rollingTable) {
    super(RailcraftMenuTypes.MANUAL_ROLLING_MACHINE.get(), containerID);
    this.player = playerInventory.player;
    this.data = data;
    this.level = playerInventory.player.level;
    checkContainerDataCount(data, 3);
    this.craftingResultSlot = new RollingResultSlot(playerInventory.player, this.craftSlots,
        this.resultSlots, 0, 124, 35);
    this.addSlot(craftingResultSlot);
    this.addSlot(new CraftingResultSlot(playerInventory.player, this.craftSlots,
        this.resultSlotClickyBox, 1, 93, 27) {
      @Override
      public ItemStack onTake(PlayerEntity player, ItemStack itemStack) {
        return ItemStack.EMPTY;
      }
    });

    for (int y = 0; y < 3; ++y) {
      for (int x = 0; x < 3; ++x) {
        // kwarg: INVENTORY, SLOTID, X, Y
        this.addSlot(new Slot(this.craftSlots, x + y * 3, 30 + (x * 18), 17 + (y * 18)));
      }
    }

    // USER INVENTORY, 3x9; 9 = hotbar slot count,84 = player inv ypos offset
    for (int y = 0; y < 3; ++y) {
      for (int x = 0; x < 9; ++x) {
        this.addSlot(new Slot(playerInventory, 9 + y * 9 + x, 8 + (x * 18), 84 + (y * 18)));
      }
    }
    // USER HOTBAR; 8 = hotbar x pos, 18 = spacing,142 = ypos
    for (int slot = 0; slot < 9; ++slot) {
      this.addSlot(new Slot(playerInventory, slot, 8 + (slot * 18), 142));
    }
    this.addDataSlots(data);

    // assign callback
    if (rollingTable != null) {
      rollingTable.setOnFinishedCallback(this::onFinishedCallback);
    }
  }

  /**
   * Progress of the current recipie, in "float percent" ie: 10% == 0.1, 50% = 0.5%
   *
   * @return The progress, used by
   *         {@link mods.railcraft.client.gui.screen.inventory.ManualRollingMachineScreen
   *         RollingTableScreen}
   */
  public float rollingProgress() {
    int requiredTime = this.data.get(0);
    int currentTick = this.data.get(1);
    return Math.max(Math.min((float) currentTick / (float) requiredTime, 1F), 0.0F);
  }

  /**
   * Callback when RollingTableEntity finished ticking away.
   * does:
   *
   * <p>- Adds the item to the resultSlots
   *
   * <p>- Resets the TileEntity's timings
   * @param devnull This Parameter does Not Exist
   */
  private void onFinishedCallback(Void devnull) {
    if (!this.cachedFinishItem.equals(ItemStack.EMPTY, false) && !this.level.isClientSide) {
      ItemStack item = this.resultSlots.getItem(0);
      if (item.equals(this.cachedFinishItem, false)) {
        this.cachedFinishItem.setCount(this.cachedFinishItem.getCount() + item.getCount());
      }
      ItemStack finishedItem = this.cachedFinishItem.copy();
      this.resultSlots.setItem(0, finishedItem);
      this.craftingResultSlot.takeCraftingItems(this.player);
      this.setData(2, 0); // reset timings onfinish, keep the time
      ((ServerPlayerEntity) this.player).connection
          .send(new SSetSlotPacket(this.containerId, 0, finishedItem));
    }
  }

  @Override
  public void slotsChanged(IInventory inventory) {
    if (!this.level.isClientSide) {
      ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)this.player;
      ItemStack itemstack = ItemStack.EMPTY;
      Optional<RollingRecipe> optional = this.level.getServer()
          .getRecipeManager()
          .getRecipeFor(RailcraftRecipeTypes.ROLLING, this.craftSlots, this.level);

      this.setData(0, 10000); // nuke the clock
      this.setData(2, 0);
      if (optional.isPresent()) {
        RollingRecipe icraftingrecipe = optional.get();
        if (this.resultSlotClickyBox.setRecipeUsed(
              this.level, serverplayerentity, icraftingrecipe)) {
          itemstack = icraftingrecipe.assemble(this.craftSlots);
          this.setData(0, icraftingrecipe.getTickCost());
        }
      }
      this.resultSlotClickyBox.setItem(0, itemstack);
      serverplayerentity.connection.send(new SSetSlotPacket(this.containerId, 1, itemstack));
      this.cachedFinishItem = itemstack;
    }
  }

  @Override
  public void removed(PlayerEntity playerEntity) {
    super.removed(playerEntity);
    this.setData(0, 10000); // nuke the clock
    this.setData(2, 0);
    this.cachedFinishItem = null;
    this.clearContainer(playerEntity, this.level, this.craftSlots);
    this.clearContainer(playerEntity, this.level, this.resultSlots);
  }

  @Override
  public boolean stillValid(PlayerEntity user) {
    return user.isAlive();
  }

  @Override
  public ItemStack clicked(int slotID, int quickCraft, ClickType clickType, PlayerEntity user) {
    if (slotID == 1) {
      Slot slot = this.slots.get(slotID);
      if (slot != null && slot.hasItem()) {
        if (this.data == null) {
          return ItemStack.EMPTY;
        }
        // not equal? DO NOT.
        if (this.cachedFinishItem != null
            && !this.resultSlots.getItem(0).equals(this.cachedFinishItem, false)
            && !this.resultSlots.getItem(0).equals(ItemStack.EMPTY, false)) {
          return ItemStack.EMPTY;
        }
        if (this.data.get(2) == 0) {
          setData(2, 1);
        }
      }
      return ItemStack.EMPTY;
    }
    return super.clicked(slotID, quickCraft, clickType, user);
  }

  @Override
  public ItemStack quickMoveStack(PlayerEntity user, int slotID) {
    Slot slot = this.slots.get(slotID);

    if (slot == null || !slot.hasItem() || slotID == 1) {
      return ItemStack.EMPTY;
    }

    ItemStack itemstack1 = slot.getItem();
    ItemStack itemstack = itemstack1.copy();

    if (slotID == 0) {
      if (this.rollingProgress() != 1.0F) {
        return ItemStack.EMPTY;
      }
      itemstack1.getItem().onCraftedBy(itemstack1, this.level, user);
      if (!this.moveItemStackTo(itemstack1, 11, 46, true)) {
        return ItemStack.EMPTY;
      }

      slot.onQuickCraft(itemstack1, itemstack);
      // CRAFT SLOTS - 2-10 hotbar, 11-47 inv
    } else if (slotID >= 11 && slotID < 47) {
      if (!this.moveItemStackTo(itemstack1, 2, 11, false)) {
        if (slotID < 37 && !this.moveItemStackTo(itemstack1, 38, 47, false)) {
          return ItemStack.EMPTY;
        }
        if (!this.moveItemStackTo(itemstack1, 11, 38, false)) {
          return ItemStack.EMPTY;
        }
      }
    } else if (!this.moveItemStackTo(itemstack1, 11, 47, false)) {
      return ItemStack.EMPTY;
    }
    // cleanup
    if (itemstack1.isEmpty()) {
      slot.set(ItemStack.EMPTY);
    } else {
      slot.setChanged();
    }

    if (itemstack1.getCount() == itemstack.getCount()) {
      return ItemStack.EMPTY;
    }

    ItemStack itemstack2 = slot.onTake(user, itemstack1);
    if (slotID == 0 || slotID == 1) {
      user.drop(itemstack2, false);
    }

    return itemstack;
  }

  @Override
  public boolean canTakeItemForPickAll(ItemStack itemStack, Slot slot) {
    return ((slot.container != this.resultSlots && slot.container != this.resultSlotClickyBox)
        && super.canTakeItemForPickAll(itemStack, slot));
  }
}

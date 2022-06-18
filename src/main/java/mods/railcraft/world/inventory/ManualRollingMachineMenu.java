package mods.railcraft.world.inventory;

import java.util.Optional;
import javax.annotation.Nullable;
import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import mods.railcraft.world.item.crafting.RollingRecipe;
import mods.railcraft.world.level.block.entity.ManualRollingMachineBlockEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * cannot extend using WorkbenchContainerClass, all of the good vars are privated AND our table is
 * locked
 */
public class ManualRollingMachineMenu extends AbstractContainerMenu {

  private final CraftingContainer craftSlots = new CraftingContainer(this, 3, 3);
  private final ResultContainer resultSlots = new ResultContainer();
  private final ResultContainer resultSlotClickyBox = new ResultContainer();
  private final RollingResultSlot craftingResultSlot;
  private final Level level;
  private final Player player;
  private ItemStack cachedFinishItem;
  // KEY INFO:
  // 1. required time | 2. currentTick (UNSETTABLE)
  // 3. shouldFire - 1 == true
  private final ContainerData data;

  public ManualRollingMachineMenu(int containerID, Inventory playerInventory) {
    this(containerID, playerInventory, new SimpleContainerData(3), null);
  }

  public ManualRollingMachineMenu(int containerID, Inventory playerInventory, ContainerData data,
      @Nullable ManualRollingMachineBlockEntity rollingTable) {
    super(RailcraftMenuTypes.MANUAL_ROLLING_MACHINE.get(), containerID);
    this.player = playerInventory.player;
    this.data = data;
    this.level = playerInventory.player.level;
    checkContainerDataCount(data, 3);
    this.craftingResultSlot = new RollingResultSlot(playerInventory.player, this.craftSlots,
        this.resultSlots, 0, 124, 35);
    this.addSlot(craftingResultSlot);
    this.addSlot(new ResultSlot(playerInventory.player, this.craftSlots, this.resultSlotClickyBox,
        1, 93, 27));

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
   * Callback when RollingTableEntity finished ticking away. does:
   *
   * <p>
   * - Adds the item to the resultSlots
   *
   * <p>
   * - Resets the TileEntity's timings
   * 
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
    }
  }

  @Override
  public void slotsChanged(Container inventory) {
    if (!this.level.isClientSide) {
      ServerPlayer serverplayerentity = (ServerPlayer) this.player;
      ItemStack itemstack = ItemStack.EMPTY;
      Optional<RollingRecipe> optional = this.level.getServer()
          .getRecipeManager()
          .getRecipeFor(RailcraftRecipeTypes.ROLLING.get(), this.craftSlots, this.level);

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

      this.cachedFinishItem = itemstack;
    }
  }

  @Override
  public void removed(Player playerEntity) {
    super.removed(playerEntity);
    this.setData(0, 10000); // nuke the clock
    this.setData(2, 0);
    this.cachedFinishItem = null;
    this.clearContainer(playerEntity, this.craftSlots);
    this.clearContainer(playerEntity, this.resultSlots);
  }

  @Override
  public boolean stillValid(Player user) {
    return user.isAlive();
  }

  @Override
  public void clicked(int slotID, int quickCraft, ClickType clickType, Player user) {
    if (slotID == 1) {
      Slot slot = this.slots.get(slotID);
      if (slot != null && slot.hasItem()) {
        if (this.data == null) {
          return;
        }
        // not equal? DO NOT.
        if (this.cachedFinishItem != null
            && !this.resultSlots.getItem(0).equals(this.cachedFinishItem, false)
            && !this.resultSlots.getItem(0).equals(ItemStack.EMPTY, false)) {
          return;
        }
        if (this.data.get(2) == 0) {
          setData(2, 1);
        }
      }
      return;
    }
    super.clicked(slotID, quickCraft, clickType, user);
  }

  @Override
  public ItemStack quickMoveStack(Player user, int slotID) {
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

    slot.onTake(user, itemstack1);
    if (slotID == 0 || slotID == 1) {
      user.drop(itemstack1, false);
    }

    return itemstack;
  }

  @Override
  public boolean canTakeItemForPickAll(ItemStack itemStack, Slot slot) {
    return ((slot.container != this.resultSlots && slot.container != this.resultSlotClickyBox)
        && super.canTakeItemForPickAll(itemStack, slot));
  }
}

package mods.railcraft.world.inventory;

import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import mods.railcraft.world.item.crafting.RollingRecipe;
import mods.railcraft.world.level.block.entity.ManualRollingMachineBlockEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
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
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ManualRollingMachineMenu extends AbstractContainerMenu {

  private final CraftingContainer craftSlots;
  private final ResultContainer resultSlots, resultSlotClickyBox;
  private final RollingResultSlot craftingResultSlot;
  private final Level level;
  private final Player player;
  private ItemStack cachedFinishItem;
  // KEY INFO:
  // 1. required time | 2. currentTick (UNSETTABLE)
  // 3. shouldFire - 1 == true
  private final ContainerData data;

  private final RecipeManager.CachedCheck<CraftingContainer, RollingRecipe> quickCheck;

  public ManualRollingMachineMenu(int containerID, Inventory playerInventory) {
    this(containerID, playerInventory, new SimpleContainerData(3), null);
  }

  public ManualRollingMachineMenu(int containerID, Inventory inventory, ContainerData data,
      @Nullable ManualRollingMachineBlockEntity manualRollingMachine) {
    super(RailcraftMenuTypes.MANUAL_ROLLING_MACHINE.get(), containerID);
    this.player = inventory.player;
    this.level = player.level;
    this.data = data;
    this.quickCheck = RecipeManager.createCheck(RailcraftRecipeTypes.ROLLING.get());

    craftSlots = new CraftingContainer(this, 3, 3);
    resultSlots = new ResultContainer();
    resultSlotClickyBox = new ResultContainer();
    this.craftingResultSlot = new RollingResultSlot(player, this.craftSlots, this.resultSlots, 0, 124, 35);

    this.addSlot(craftingResultSlot);

    this.addSlot(new ResultSlot(player, this.craftSlots, this.resultSlotClickyBox, 1, 93, 27));

    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        this.addSlot(new Slot(this.craftSlots, x + y * 3, 30 + (x * 18), 17 + (y * 18)));
      }
    }

    this.addPlayerSlots(inventory);
    this.addDataSlots(data);

    // assign callback
    if (manualRollingMachine != null) {
      manualRollingMachine.setOnFinishedCallback(this::onFinishedCallback);
    }
  }

  private void addPlayerSlots(Inventory inventory) {
    for (int i = 0; i < 3; i++) {
      for (int k = 0; k < 9; k++) {
        this.addSlot(new Slot(inventory, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
      }
    }
    for (int j = 0; j < 9; j++) {
      this.addSlot(new Slot(inventory, j, 8 + j * 18, 142));
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
    return Mth.clamp((float) currentTick / (float) requiredTime, 0, 1);
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
  private void onFinishedCallback() {
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
  public void slotsChanged(Container container) {
    if (this.level.isClientSide) {
      return;
    }
    ItemStack itemstack = ItemStack.EMPTY;
    var optionalRollingRecipe = this.quickCheck.getRecipeFor(craftSlots, level);
    this.setData(0, 10000);
    this.setData(2, 0);
    if (optionalRollingRecipe.isPresent()) {
      var recipe = optionalRollingRecipe.get();
      if (this.resultSlotClickyBox.setRecipeUsed(this.level, (ServerPlayer) player, recipe)) {
        itemstack = recipe.assemble(this.craftSlots);
        this.setData(0, recipe.getTickCost());
      }
    }
    balanceSlots();

    this.resultSlotClickyBox.setItem(0, itemstack);
    this.cachedFinishItem = itemstack;
  }

  private void balanceSlots() {
    int size = craftSlots.getContainerSize();
    for(int i = 0; i < size; i++) {
      var stackA = craftSlots.getItem(i);
      if(stackA.isEmpty())
        continue;
      for(int j = 0; j < size; j++) {
        if(i == j)
          continue;
        var stackB = craftSlots.getItem(j);
        if(stackB.isEmpty())
          continue;

        if(stackA.sameItem(stackB)) {
          if(stackA.getCount() > stackB.getCount() + 1) {
            stackA.shrink(1);
            stackB.grow(1);
            craftSlots.setItem(i, stackA);
            craftSlots.setItem(j, stackB);
            return;
          }
        }
      }
    }
  }

  @Override
  public void removed(Player player) {
    super.removed(player);
    this.setData(0, 10000); // nuke the clock
    this.setData(2, 0);
    this.cachedFinishItem = null;
    this.clearContainer(player, this.craftSlots);
    this.clearContainer(player, this.resultSlots);
  }

  @Override
  public boolean stillValid(Player player) {
    return player.isAlive();
  }

  @Override
  public void clicked(int slotIndex, int quickCraft, ClickType clickType, Player player) {
    if (slotIndex == 1) {
      Slot slot = this.slots.get(slotIndex);
      if (slot.hasItem()) {
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
    super.clicked(slotIndex, quickCraft, clickType, player);
  }

  @Override
  public ItemStack quickMoveStack(Player player, int slotIndex) {
    Slot slot = this.slots.get(slotIndex);

    if (!slot.hasItem() || slotIndex == 1) {
      return ItemStack.EMPTY;
    }

    ItemStack itemstack1 = slot.getItem();
    ItemStack itemstack = itemstack1.copy();

    if (slotIndex == 0) {
      if (this.rollingProgress() != 1.0F) {
        return ItemStack.EMPTY;
      }
      itemstack1.getItem().onCraftedBy(itemstack1, this.level, player);
      if (!this.moveItemStackTo(itemstack1, 11, 46, true)) {
        return ItemStack.EMPTY;
      }

      slot.onQuickCraft(itemstack1, itemstack);
      // CRAFT SLOTS - 2-10 hotbar, 11-47 inv
    } else if (slotIndex >= 11 && slotIndex < 47) {
      if (!this.moveItemStackTo(itemstack1, 2, 11, false)) {
        if (slotIndex < 37 && !this.moveItemStackTo(itemstack1, 38, 47, false)) {
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

    slot.onTake(player, itemstack1);
    if (slotIndex == 0) {
      player.drop(itemstack1, false);
    }

    return itemstack;
  }

  @Override
  public boolean canTakeItemForPickAll(ItemStack itemStack, Slot slot) {
    return ((slot.container != this.resultSlots && slot.container != this.resultSlotClickyBox)
        && super.canTakeItemForPickAll(itemStack, slot));
  }
}

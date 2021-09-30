package mods.railcraft.crafting;

import java.util.Optional;

import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;

/**
 * cannot extend using WorkbenchContainerClass, all of the good vars are privated AND our table is locked
 */
public class RollingTableContainer extends Container {

  private final CraftingInventory craftSlots = new CraftingInventory(this, 3, 3);
  private final CraftResultHold resultSlots = new CraftResultHold();
  private final IWorldPosCallable access;
  private final PlayerEntity player;
  private int tickCost;
  private int tickCurrently = 0;

  public RollingTableContainer(int containerID, PlayerInventory playerInventory) {
    this(containerID, playerInventory, IWorldPosCallable.NULL);
  }

  public RollingTableContainer(int containerID, PlayerInventory playerInventory, IWorldPosCallable worldPos) {
    super(RailcraftContainers.ROLLING.get(), containerID);
    this.access = worldPos;
    this.player = playerInventory.player;
    this.addSlot(new CraftingResultSlot(playerInventory.player, this.craftSlots, this.resultSlots, 0, 124, 35));

    for(int y = 0; y < 3; ++y) {
      for(int x = 0; x < 3; ++x) {
        //kwarg: INVENTORY, SLOTID, X, Y
        this.addSlot(new Slot(this.craftSlots, x + y * 3, 30 + (x * 18), 17 + (y * 18)));
      }
    }

    // USER INVENTORY, 3x9; 9 = hotbar slot count,84 = player inv ypos offset
    for(int y = 0; y < 3; ++y) {
      for(int x = 0; x < 9; ++x) {
        this.addSlot(new Slot(playerInventory, 9 + y * 9 + x, 8 + (x * 18), 84 + (y * 18)));
      }
    }
    // USER HOTBAR; 8 = hotbar x pos, 18 = spacing,142 = ypos
    for(int slot = 0; slot < 9; ++slot) {
      this.addSlot(new Slot(playerInventory, slot, 8 + (slot * 18), 142));
    }
  }

  // todo: make this static again,
  private void slotChangedCraftingGrid(int slotID, World world, PlayerEntity user, CraftingInventory craftingInventory, CraftResultHold resultInventory) {
    if (!world.isClientSide) {
      ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)user;
      ItemStack itemstack = ItemStack.EMPTY;
      Optional<RollingRecipe> optional = world.getServer().getRecipeManager().getRecipeFor(RailcraftRecipies.ROLLING_RECIPIE, craftingInventory, world);
      if (optional.isPresent()) {
        RollingRecipe icraftingrecipe = optional.get();
        this.tickCurrently = 0; // moving anything resets it
        if (resultInventory.setRecipeUsed(world, serverplayerentity, icraftingrecipe)) {
          itemstack = icraftingrecipe.assemble(craftingInventory);
          this.setTickCost(icraftingrecipe.getTickCost());
        }
      }
      resultInventory.setItem(0, itemstack);
      serverplayerentity.connection.send(new SSetSlotPacket(slotID, 0, itemstack));
    }
  }

  /**
   * Progress of the current recipie, in "float percent" ie: 10% == 0.1, 50% = 0.5%
   * @return The progress, used by {@link mods.railcraft.client.gui.screen.inventory.RollingTableScreen RollingTableScreen}
   */
  public float rollingProgress() {
    return Math.min((float)tickCurrently / (float)tickCost, 1F);
  }

  public void setTickCost(int cost) {
    this.tickCost = cost;
  }

  /**
   * Updates the resultSlot status. Returns TRUE when unlocked, FALSE when not
   */
  public boolean updateRollingStatus() {
    if (this.rollingProgress() == 1F) {
      this.resultSlots.unblockItemPull();
      return true;
    }
    this.resultSlots.blockItemPull();
    return false;
  }

  public void tick(){
  }

  @Override
  public void slotsChanged(IInventory inventory) {
    this.access.execute((block, pos) ->
      slotChangedCraftingGrid(this.containerId, block, this.player, this.craftSlots, this.resultSlots)
    );
  }

  @Override
  public void removed(PlayerEntity playerEntity) {
    super.removed(playerEntity);
    this.setTickCost(0); // clear it.
    this.access.execute((block, pos) ->
      this.clearContainer(playerEntity, block, this.craftSlots) // todo: when extending this class, make sure to override clearContainer
    );
  }

  @Override
  public boolean stillValid(PlayerEntity user) {
    return stillValid(this.access, user, RailcraftBlocks.ROLLING_TABLE.get());
  }

  @Override
  public ItemStack quickMoveStack(PlayerEntity user, int slotID) {
    Slot slot = this.slots.get(slotID);

    if (slot == null || !slot.hasItem()) {
      return ItemStack.EMPTY;
    }

    ItemStack itemstack1 = slot.getItem();
    ItemStack itemstack = itemstack1.copy();
    if (slotID == 0) {
      if (!this.updateRollingStatus()) {
        return ItemStack.EMPTY;
      }
      this.access.execute((world, pos) ->
        itemstack1.getItem().onCraftedBy(itemstack1, world, user)
      );
      if (!this.moveItemStackTo(itemstack1, 10, 46, true)) {
        return ItemStack.EMPTY;
      }

      slot.onQuickCraft(itemstack1, itemstack);
    // INVENTORY SLOTS - 0-9 hotbar, 10-46 inv
    } else if (slotID >= 10 && slotID < 46) {
      if (!this.moveItemStackTo(itemstack1, 1, 10, false)) {
        if (slotID < 37 && !this.moveItemStackTo(itemstack1, 37, 46, false)) {
          return ItemStack.EMPTY;
        }
        if (!this.moveItemStackTo(itemstack1, 10, 37, false)) {
          return ItemStack.EMPTY;
        }
      }
    } else if (!this.moveItemStackTo(itemstack1, 10, 46, false)) {
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
    if (slotID == 0) {
      user.drop(itemstack2, false);
    }

    return itemstack;
  }

  @Override
  public boolean canTakeItemForPickAll(ItemStack itemStack, Slot slot) {
    return slot.container != this.resultSlots && super.canTakeItemForPickAll(itemStack, slot);
  }

}

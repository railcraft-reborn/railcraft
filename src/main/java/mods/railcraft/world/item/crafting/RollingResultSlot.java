package mods.railcraft.world.item.crafting;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * Alternate to CraftingResultSlot
 */
public class RollingResultSlot extends Slot {
  private final CraftingInventory craftSlots;
  private final PlayerEntity player;

  public RollingResultSlot(PlayerEntity user, CraftingInventory craftingInventory,
      IInventory outputInventory, int slotID, int x, int y) {
    super(outputInventory, slotID, x, y);
    this.player = user;
    this.craftSlots = craftingInventory;
  }

  @Override
  public boolean mayPlace(ItemStack itemStack) {
    return false;
  }

  @Override
  protected void onQuickCraft(ItemStack itemStack, int count) {
    this.checkTakeAchievements(itemStack);
  }

  @Override
  protected void checkTakeAchievements(ItemStack itemStack) {
    // if (this.removeCount > 0) {
    // itemStack.onCraftedBy(this.player.level, this.player, this.removeCount);
    // net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerCraftingEvent(this.player, itemStack,
    // this.craftSlots);
    // }

    // if (this.container instanceof IRecipeHolder) {
    // ((IRecipeHolder)this.container).awardUsedRecipes(this.player);
    // }
    // TODO: achievments
  }

  @Override
  public ItemStack onTake(PlayerEntity playerEntity, ItemStack itemStack) {
    this.checkTakeAchievements(itemStack);
    return itemStack;
  }

  public void takeCraftingItems(PlayerEntity playerEntity) {
    NonNullList<ItemStack> recipieRemaningItems =
        playerEntity.level.getRecipeManager().getRemainingItemsFor(RailcraftRecipeTypes.ROLLING,
            this.craftSlots, playerEntity.level);
    for (int i = 0; i < recipieRemaningItems.size(); ++i) {
      ItemStack itemstack = this.craftSlots.getItem(i);
      ItemStack itemstack1 = recipieRemaningItems.get(i);
      if (!itemstack.isEmpty()) {
        this.craftSlots.removeItem(i, 1);
        itemstack = this.craftSlots.getItem(i);
      }

      if (!itemstack1.isEmpty()) {
        if (itemstack.isEmpty()) {
          this.craftSlots.setItem(i, itemstack1);
        } else if (ItemStack.isSame(itemstack, itemstack1)
            && ItemStack.tagMatches(itemstack, itemstack1)) {
          itemstack1.grow(itemstack.getCount());
          this.craftSlots.setItem(i, itemstack1);
        } else if (!this.player.inventory.add(itemstack1)) {
          this.player.drop(itemstack1, false);
        }
      }
    }
  }
}

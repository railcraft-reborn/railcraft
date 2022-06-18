package mods.railcraft.world.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import net.minecraft.core.NonNullList;

/**
 * Alternate to CraftingResultSlot.
 */
public class RollingResultSlot extends Slot {
  private final CraftingContainer craftSlots;
  private final Player player;

  public RollingResultSlot(Player user, CraftingContainer craftingInventory,
      Container outputInventory, int slotID, int x, int y) {
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
  public void onTake(Player playerEntity, ItemStack itemStack) {
    this.checkTakeAchievements(itemStack);
  }

  public void takeCraftingItems(Player playerEntity) {
    NonNullList<ItemStack> recipieRemaningItems =
        playerEntity.level.getRecipeManager().getRemainingItemsFor(
            RailcraftRecipeTypes.ROLLING.get(),
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
        } else if (!this.player.getInventory().add(itemstack1)) {
          this.player.drop(itemstack1, false);
        }
      }
    }
  }
}

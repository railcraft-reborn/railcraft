package mods.railcraft.world.inventory.slot;

import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;

public class RollingResultSlot extends ResultSlot {

  private final CraftingContainer craftSlots;
  private final Player player;

  public RollingResultSlot(Player player, CraftingContainer craftSlots, Container container,
      int slot, int xPos, int yPos) {
    super(player, craftSlots, container, slot, xPos, yPos);
    this.player = player;
    this.craftSlots = craftSlots;
  }

  @Override
  public void onTake(Player player, ItemStack stack) {
    this.checkTakeAchievements(stack);
    var recipeRemainingItems = player.level().getRecipeManager()
        .getRemainingItemsFor(RailcraftRecipeTypes.ROLLING.get(), this.craftSlots, player.level());
    for (int i = 0; i < recipeRemainingItems.size(); ++i) {
      var itemstack = this.craftSlots.getItem(i);
      var itemstack1 = recipeRemainingItems.get(i);
      if (!itemstack.isEmpty()) {
        this.craftSlots.removeItem(i, 1);
        itemstack = this.craftSlots.getItem(i);
      }

      if (!itemstack1.isEmpty()) {
        if (itemstack.isEmpty()) {
          this.craftSlots.setItem(i, itemstack1);
        } else if (ItemStack.isSameItem(itemstack, itemstack1)
            && ItemStack.isSameItemSameTags(itemstack, itemstack1)) {
          itemstack1.grow(itemstack.getCount());
          this.craftSlots.setItem(i, itemstack1);
        } else if (!this.player.getInventory().add(itemstack1)) {
          this.player.drop(itemstack1, false);
        }
      }
    }
  }
}

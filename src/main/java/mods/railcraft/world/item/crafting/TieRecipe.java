package mods.railcraft.world.item.crafting;

import java.util.Objects;
import org.jspecify.annotations.Nullable;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public abstract class TieRecipe extends CustomRecipe {

  private final TagKey<Fluid> fluidTag;
  private final ItemStack result;
  @Nullable
  private PlacementInfo placementInfo;

  public TieRecipe(CraftingBookCategory category, TagKey<Fluid> fluidTag,
      ItemStack result) {
    super(category);
    this.fluidTag = fluidTag;
    this.result = result;
  }

  @Override
  public final boolean matches(CraftingInput craftingInput, Level level) {
    if (craftingInput.width() != 3 || craftingInput.height() != 2) {
      return false;
    }

    if (!craftingInput.getItem(0).isEmpty() || !craftingInput.getItem(2).isEmpty()) {
      return false;
    }

    var item = craftingInput.getItem(1);
    if (item.isEmpty()) {
      return false;
    }
    var cap = item.getCapability(Capabilities.Fluid.ITEM, null);
    if (cap == null || !FluidUtil.getStack(cap, 0).is(this.fluidTag)) {
      return false;
    }

    return testIngredient(craftingInput.getItem(3), 0) &&
        testIngredient(craftingInput.getItem(4), 1) &&
        testIngredient(craftingInput.getItem(5), 2);
  }

  protected abstract boolean testIngredient(ItemStack itemPresent, int index);

  @Override
  public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
    var fluidHandler = Objects.requireNonNull(
        craftingInput.getItem(1).getCapability(Capabilities.Fluid.ITEM, null));

    if (fluidHandler.getAmountAsInt(0) >= 1000) {
      return result.copy();
    }
    return ItemStack.EMPTY;
  }

  @Override
  public final NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
    var remainingItems = NonNullList.withSize(input.size(), ItemStack.EMPTY);
    for(int i = 0; i < remainingItems.size(); ++i) {
      ItemStack item = input.getItem(i);
      if (!item.getCraftingRemainder().isEmpty()) {
        remainingItems.set(i, item.getCraftingRemainder());
      } else {
        var itemAccess = ItemAccess.forStack(item);
        var cap = itemAccess.getCapability(Capabilities.Fluid.ITEM);
        if (cap != null) {
          try (var tx = Transaction.openRoot()) {
            var resource = cap.getResource(0);
            if (!resource.isEmpty()) {
              var extracted = cap.extract(resource, 1000, tx);
              if (extracted == 1000) {
                tx.commit();
              }
            }
          }
          remainingItems.set(i, item.copy());
        }
      }
    }
    return remainingItems;
  }
}

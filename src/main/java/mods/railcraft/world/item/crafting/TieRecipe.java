package mods.railcraft.world.item.crafting;

import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.tags.TagKey;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public abstract class TieRecipe extends CustomRecipe {

  private final TagKey<Fluid> fluidTag;
  private final ItemStack result;
  protected final NonNullList<Optional<Ingredient>> ingredients;
  @Nullable
  private PlacementInfo placementInfo;

  public TieRecipe(CraftingBookCategory category, TagKey<Fluid> fluidTag, ItemStack result) {
    super(category);
    this.fluidTag = fluidTag;
    this.result = result;
    this.ingredients = NonNullList.withSize(6, Optional.empty());
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
    if (!FluidUtil.getFirstStackContained(item).is(this.fluidTag)) {
      return false;
    }

    for (int i = 3; i < 6; i++) {
      if (!testBottomIngredients(craftingInput, i)) {
        return false;
      }
    }
    return true;
  }

  protected boolean testBottomIngredients(CraftingInput craftingInput, int index) {
    return this.ingredients.get(index)
        .map(ingredient -> ingredient.test(craftingInput.getItem(index)))
        .orElse(false);
  }

  @Override
  public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
    var fluidHandler = FluidUtil.getFirstStackContained(craftingInput.getItem(1));
    if (fluidHandler.getAmount() >= 1000) {
      return result.copy();
    }
    return ItemStack.EMPTY;
  }

  @Override
  public final NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
    var remainingItems = NonNullList.withSize(input.size(), ItemStack.EMPTY);
    ItemStack[] containerItems = input.items().toArray(new ItemStack[0]);
    var container = VanillaContainerWrapper.of(new SimpleContainer(containerItems) {
      // Override to avoid clamping oversized stacks to their max stack size, just in case.
      @Override
      public void setItem(int slot, ItemStack stack, boolean performSideEffects) {
        getItems().set(slot, stack);
      }
    });

    for(int i = 0; i < remainingItems.size(); ++i) {
      ItemStack item = input.getItem(i);
      if (!item.getCraftingRemainder().isEmpty()) {
        remainingItems.set(i, item.getCraftingRemainder());
      } else {
        var itemAccess = ItemAccess.forHandlerIndex(container, i);
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

  @Override
  public PlacementInfo placementInfo() {
    if (this.placementInfo == null) {
      this.placementInfo = PlacementInfo.createFromOptionals(this.ingredients);
    }
    return this.placementInfo;
  }

  @Override
  public List<RecipeDisplay> display() {
    return List.of(
        new ShapedCraftingRecipeDisplay(
            3,
            2,
            this.ingredients.stream()
                .map(i -> i.map(Ingredient::display)
                    .orElse(SlotDisplay.Empty.INSTANCE))
                .toList(),
            new SlotDisplay.ItemStackSlotDisplay(this.result),
            new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)
        )
    );
  }
}

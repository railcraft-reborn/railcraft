package mods.railcraft.world.item.crafting;

import java.util.List;
import org.jspecify.annotations.Nullable;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;

public abstract class CartDisassemblyRecipe extends CustomRecipe {

  private final Item ingredient;
  private final Item result;
  @Nullable
  private PlacementInfo placementInfo;

  public CartDisassemblyRecipe(Item ingredient, Item result) {
    super();
    this.ingredient = ingredient;
    this.result = result;
  }

  @Override
  public boolean matches(CraftingInput craftingInput, Level level) {
    boolean ingredientsMatch = false;
    for (int i = 0; i < craftingInput.size(); i++) {
      var stack = craftingInput.getItem(i);
      if (!ingredientsMatch && stack.is(this.ingredient)) {
        ingredientsMatch = true;
      }
    }
    return craftingInput.ingredientCount() == 1 && ingredientsMatch;
  }

  @Override
  public ItemStack assemble(CraftingInput craftingInput) {
    return new ItemStack(this.result);
  }

  @Override
  public PlacementInfo placementInfo() {
    if (this.placementInfo == null) {
      this.placementInfo = PlacementInfo.create(Ingredient.of(this.ingredient));
    }
    return this.placementInfo;
  }

  @Override
  public List<RecipeDisplay> display() {
    return List.of(new ShapelessCraftingRecipeDisplay(
        List.of(new SlotDisplay.ItemSlotDisplay(this.ingredient)),
        new SlotDisplay.ItemSlotDisplay(this.result),
        new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
  }

  @Override
  public NonNullList<ItemStack> getRemainingItems(CraftingInput craftingInput) {
    var grid = NonNullList.withSize(craftingInput.size(), ItemStack.EMPTY);
    for (int i = 0; i < craftingInput.size(); i++) {
      var itemStack = craftingInput.getItem(i);
      if (itemStack.is(this.ingredient)) {
        grid.set(i, new ItemStack(Items.MINECART));
      }
    }
    return grid;
  }
}

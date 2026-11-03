package mods.railcraft.world.item.crafting;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.TagFilterItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class TagFilterRecipe extends CustomRecipe {

  private static final List<Item> EXAMPLES = List.of(Items.COPPER_INGOT, Items.IRON_INGOT,
      Items.GOLD_INGOT, Items.RAW_IRON, Items.COAL, Items.REDSTONE, Items.OAK_LOG,
      Items.COBBLESTONE);

  public TagFilterRecipe(CraftingBookCategory category) {
    super(category);
  }

  @Override
  public boolean matches(CraftingInput craftingInput, Level level) {
    return findPrototypeSlot(craftingInput) >= 0;
  }

  @Override
  public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
    var slot = findPrototypeSlot(craftingInput);
    if (slot < 0) {
      return ItemStack.EMPTY;
    }
    var result = new ItemStack(RailcraftItems.TAG_FILTER.get());
    return TagFilterItem.setPrototype(result, craftingInput.getItem(slot).getItem())
        ? result
        : ItemStack.EMPTY;
  }

  @Override
  public NonNullList<ItemStack> getRemainingItems(CraftingInput craftingInput) {
    var remaining = NonNullList.withSize(craftingInput.size(), ItemStack.EMPTY);
    var slot = findPrototypeSlot(craftingInput);
    if (slot >= 0) {
      remaining.set(slot, craftingInput.getItem(slot).copyWithCount(1));
    }
    return remaining;
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return NonNullList.of(Ingredient.EMPTY, Ingredient.of(RailcraftItems.TAG_FILTER.get()),
        Ingredient.of(getExamplePrototypes().stream()));
  }

  @Override
  public ItemStack getResultItem(HolderLookup.Provider provider) {
    return new ItemStack(RailcraftItems.TAG_FILTER.get());
  }

  public static List<ItemStack> getExamplePrototypes() {
    return EXAMPLES.stream()
        .filter(item -> !TagFilterItem.getTags(item).isEmpty())
        .map(ItemStack::new)
        .toList();
  }

  public static List<ItemStack> getExampleResults() {
    return getExamplePrototypes().stream()
        .map(prototype -> {
          var result = new ItemStack(RailcraftItems.TAG_FILTER.get());
          TagFilterItem.setPrototype(result, prototype.getItem());
          return result;
        })
        .toList();
  }

  /**
   * Find the prototype in a grid holding exactly one tag filter and one item with tags.
   *
   * @return the prototype's slot, or <code>-1</code> if the grid doesn't match
   */
  private static int findPrototypeSlot(CraftingInput craftingInput) {
    int filters = 0;
    int prototypeSlot = -1;
    for (int slot = 0; slot < craftingInput.size(); slot++) {
      var stack = craftingInput.getItem(slot);
      if (stack.isEmpty()) {
        continue;
      }
      if (stack.is(RailcraftItems.TAG_FILTER.get())) {
        filters++;
      } else if (prototypeSlot < 0) {
        prototypeSlot = slot;
      } else {
        return -1;
      }
    }
    return filters == 1 && prototypeSlot >= 0 && hasTags(craftingInput.getItem(prototypeSlot))
        ? prototypeSlot
        : -1;
  }

  private static boolean hasTags(@Nullable ItemStack stack) {
    return stack != null && !TagFilterItem.getTags(stack.getItem()).isEmpty();
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width * height >= 2;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return RailcraftRecipeSerializers.TAG_FILTER.get();
  }
}

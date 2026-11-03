package mods.railcraft.world.item.crafting;

import java.util.List;
import com.mojang.serialization.MapCodec;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.TagFilterItem;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;

public class TagFilterRecipe extends CustomRecipe {

  private static final TagFilterRecipe INSTANCE = new TagFilterRecipe();
  private static final MapCodec<TagFilterRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
  private static final StreamCodec<RegistryFriendlyByteBuf, TagFilterRecipe> STREAM_CODEC =
      StreamCodec.unit(INSTANCE);
  public static final RecipeSerializer<TagFilterRecipe> SERIALIZER =
      new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

  /**
   * Prototypes shown by recipe viewers, any item with tags works.
   */
  private static final List<Item> EXAMPLES = List.of(Items.COPPER_INGOT, Items.IRON_INGOT,
      Items.GOLD_INGOT, Items.RAW_IRON, Items.COAL, Items.REDSTONE, Items.OAK_LOG,
      Items.COBBLESTONE);

  @Override
  public boolean matches(CraftingInput craftingInput, Level level) {
    return findPrototypeSlot(craftingInput) >= 0;
  }

  @Override
  public ItemStack assemble(CraftingInput craftingInput) {
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
  public List<RecipeDisplay> display() {
    return List.of(new ShapelessCraftingRecipeDisplay(
        List.of(new SlotDisplay.ItemSlotDisplay(RailcraftItems.TAG_FILTER.get()),
            new SlotDisplay.Composite(getExamplePrototypes().stream()
                .<SlotDisplay>map(stack -> new SlotDisplay.ItemSlotDisplay(stack.getItem()))
                .toList())),
        new SlotDisplay.ItemSlotDisplay(RailcraftItems.TAG_FILTER.get()),
        new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
  }

  /**
   * Example prototypes for recipe viewers, each paired by index with
   * {@link #getExampleResults()}.
   */
  public static List<ItemStack> getExamplePrototypes() {
    return EXAMPLES.stream()
        .filter(item -> !TagFilterItem.getTags(item).isEmpty())
        .map(ItemStack::new)
        .toList();
  }

  /**
   * The filters crafted with {@link #getExamplePrototypes()}.
   */
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
    if (craftingInput.width() * craftingInput.height() < 2) {
      return -1;
    }
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
    return filters == 1 && prototypeSlot >= 0
        && !TagFilterItem.getTags(craftingInput.getItem(prototypeSlot).getItem()).isEmpty()
        ? prototypeSlot
        : -1;
  }

  @Override
  public RecipeSerializer<TagFilterRecipe> getSerializer() {
    return SERIALIZER;
  }
}

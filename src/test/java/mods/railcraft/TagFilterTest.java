package mods.railcraft;

import java.util.List;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.util.container.StackFilter;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.TagFilterItem;
import mods.railcraft.world.item.component.RailcraftDataComponents;
import mods.railcraft.world.item.crafting.TagFilterRecipe;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(RailcraftConstants.ID)
@PrefixGameTestTemplate(false)
public class TagFilterTest {

  /**
   * A filter crafted with a copper ingot starts on its most specific tag, c:ingots/copper, and
   * only matches items in that tag. A blank filter matches nothing.
   */
  @GameTest(template = "platform_7x7")
  public static void tagFilterMatchesSelectedTag(GameTestHelper helper) {
    var blank = new ItemStack(RailcraftItems.TAG_FILTER.get());
    helper.assertFalse(ContainerTools.matchesFilter(blank, new ItemStack(Items.COPPER_INGOT)),
        "A blank tag filter must not match anything");

    var filter = new ItemStack(RailcraftItems.TAG_FILTER.get());
    helper.assertTrue(TagFilterItem.setPrototype(filter, Items.COPPER_INGOT),
        "A copper ingot has tags");
    var tag = filter.get(RailcraftDataComponents.TAG_FILTER).tag();
    helper.assertTrue(tag.equals(Tags.Items.INGOTS_COPPER),
        "The filter must start on c:ingots/copper, got " + tag.location());

    var matches = StackFilter.anyMatch(filter);
    helper.assertTrue(matches.test(new ItemStack(Items.COPPER_INGOT)),
        "c:ingots/copper must match a copper ingot");
    helper.assertFalse(matches.test(new ItemStack(Items.IRON_INGOT)),
        "c:ingots/copper must not match an iron ingot");
    helper.assertFalse(matches.test(new ItemStack(Items.RAW_COPPER)),
        "c:ingots/copper must not match raw copper");
    helper.succeed();
  }

  /**
   * Using the filter goes through every tag of its prototype and wraps around.
   */
  @GameTest(template = "platform_7x7")
  public static void tagFilterCyclesTags(GameTestHelper helper) {
    var filter = new ItemStack(RailcraftItems.TAG_FILTER.get());
    TagFilterItem.setPrototype(filter, Items.COPPER_INGOT);
    var tags = TagFilterItem.getTags(Items.COPPER_INGOT);
    helper.assertTrue(tags.contains(Tags.Items.INGOTS),
        "A copper ingot must be in c:ingots, its tags are " + tags);

    for (int i = 1; i <= tags.size(); i++) {
      helper.assertTrue(TagFilterItem.cycleTag(filter), "The filter must change tag");
      var tag = filter.get(RailcraftDataComponents.TAG_FILTER).tag();
      helper.assertTrue(tag.equals(tags.get(i % tags.size())),
          "Expected %s after %s uses, got %s".formatted(tags.get(i % tags.size()), i, tag));
      if (tag.equals(Tags.Items.INGOTS)) {
        helper.assertTrue(ContainerTools.matchesFilter(filter, new ItemStack(Items.IRON_INGOT)),
            "c:ingots must match an iron ingot");
      }
    }
    helper.assertFalse(TagFilterItem.cycleTag(new ItemStack(RailcraftItems.TAG_FILTER.get())),
        "A blank filter has no tags to change to");
    helper.succeed();
  }

  /**
   * Crafting a tag filter with an item sets it as the prototype and gives the item back.
   */
  @GameTest(template = "platform_7x7")
  public static void tagFilterRecipeSetsPrototype(GameTestHelper helper) {
    var level = helper.getLevel();
    var input = CraftingInput.of(2, 1, List.of(
        new ItemStack(RailcraftItems.TAG_FILTER.get()), new ItemStack(Items.COPPER_INGOT, 5)));
    var recipe = level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, level)
        .orElse(null);
    helper.assertTrue(recipe != null && recipe.value() instanceof TagFilterRecipe,
        "A tag filter and a copper ingot must match the tag filter recipe");

    var result = recipe.value().assemble(input, level.registryAccess());
    var component = result.get(RailcraftDataComponents.TAG_FILTER);
    helper.assertTrue(component != null && component.prototype().value() == Items.COPPER_INGOT
        && component.tag().equals(Tags.Items.INGOTS_COPPER),
        "The crafted filter must use the copper ingot on c:ingots/copper, got " + component);

    var remaining = recipe.value().getRemainingItems(input);
    helper.assertTrue(remaining.get(1).is(Items.COPPER_INGOT) && remaining.get(1).getCount() == 1,
        "One copper ingot must be given back, got " + remaining);

    var twoItems = CraftingInput.of(3, 1, List.of(new ItemStack(RailcraftItems.TAG_FILTER.get()),
        new ItemStack(Items.COPPER_INGOT), new ItemStack(Items.IRON_INGOT)));
    helper.assertFalse(level.getRecipeManager()
        .getRecipeFor(RecipeType.CRAFTING, twoItems, level)
        .filter(holder -> holder.value() instanceof TagFilterRecipe)
        .isPresent(), "A tag filter takes a single item");
    helper.succeed();
  }
}

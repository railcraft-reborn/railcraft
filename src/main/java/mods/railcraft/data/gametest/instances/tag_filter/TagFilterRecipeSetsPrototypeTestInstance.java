package mods.railcraft.data.gametest.instances.tag_filter;

import java.util.List;
import com.mojang.serialization.MapCodec;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.component.RailcraftDataComponents;
import mods.railcraft.world.item.crafting.TagFilterRecipe;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.Tags;

/**
 * Crafting a tag filter with an item sets it as the prototype and gives the item back.
 */
public class TagFilterRecipeSetsPrototypeTestInstance extends GameTestInstance {

  public static final MapCodec<TagFilterRecipeSetsPrototypeTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(TagFilterRecipeSetsPrototypeTestInstance::new);

  public TagFilterRecipeSetsPrototypeTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    var level = helper.getLevel();
    var recipes = level.recipeAccess();
    var input = CraftingInput.of(2, 1, List.of(
        new ItemStack(RailcraftItems.TAG_FILTER.get()), new ItemStack(Items.COPPER_INGOT, 5)));
    var recipe = recipes.getRecipeFor(RecipeType.CRAFTING, input, level).orElse(null);
    helper.assertTrue(recipe != null && recipe.value() instanceof TagFilterRecipe,
        "A tag filter and a copper ingot must match the tag filter recipe");

    var result = recipe.value().assemble(input);
    var component = result.get(RailcraftDataComponents.TAG_FILTER);
    helper.assertTrue(component != null && component.prototype().value() == Items.COPPER_INGOT
        && component.tag().equals(Tags.Items.INGOTS_COPPER),
        "The crafted filter must use the copper ingot on c:ingots/copper, got " + component);

    var remaining = recipe.value().getRemainingItems(input);
    helper.assertTrue(remaining.get(1).is(Items.COPPER_INGOT) && remaining.get(1).getCount() == 1,
        "One copper ingot must be given back, got " + remaining);

    var twoItems = CraftingInput.of(3, 1, List.of(new ItemStack(RailcraftItems.TAG_FILTER.get()),
        new ItemStack(Items.COPPER_INGOT), new ItemStack(Items.IRON_INGOT)));
    helper.assertFalse(recipes.getRecipeFor(RecipeType.CRAFTING, twoItems, level)
        .filter(holder -> holder.value() instanceof TagFilterRecipe)
        .isPresent(), "A tag filter takes a single item");
    helper.succeed();
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Tag Filter Recipe Sets Prototype");
  }
}

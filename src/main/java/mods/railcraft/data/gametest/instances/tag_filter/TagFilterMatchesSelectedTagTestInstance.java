package mods.railcraft.data.gametest.instances.tag_filter;

import com.mojang.serialization.MapCodec;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.util.container.StackFilter;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.TagFilterItem;
import mods.railcraft.world.item.component.RailcraftDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

/**
 * A filter crafted with a copper ingot starts on its most specific tag, c:ingots/copper, and only matches items in that tag. A blank filter matches nothing.
 */
public class TagFilterMatchesSelectedTagTestInstance extends GameTestInstance {

  public static final MapCodec<TagFilterMatchesSelectedTagTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(TagFilterMatchesSelectedTagTestInstance::new);

  public TagFilterMatchesSelectedTagTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
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

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Tag Filter Matches Selected Tag");
  }
}

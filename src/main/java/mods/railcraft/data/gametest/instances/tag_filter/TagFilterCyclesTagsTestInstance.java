package mods.railcraft.data.gametest.instances.tag_filter;

import com.mojang.serialization.MapCodec;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import mods.railcraft.util.container.ContainerTools;
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
 * Using the filter goes through every tag of its prototype and wraps around.
 */
public class TagFilterCyclesTagsTestInstance extends GameTestInstance {

  public static final MapCodec<TagFilterCyclesTagsTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(TagFilterCyclesTagsTestInstance::new);

  public TagFilterCyclesTagsTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
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

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Tag Filter Cycles Tags");
  }
}

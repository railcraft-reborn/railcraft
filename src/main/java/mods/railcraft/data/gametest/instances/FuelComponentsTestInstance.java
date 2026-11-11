package mods.railcraft.data.gametest.instances;

import java.util.Optional;
import com.mojang.serialization.MapCodec;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import mods.railcraft.util.FuelUtil;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;
import net.neoforged.neoforge.common.loot.NeoForgeLootContextParams;

public class FuelComponentsTestInstance extends GameTestInstance {

  public static final MapCodec<FuelComponentsTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(FuelComponentsTestInstance::new);

  public FuelComponentsTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    assertFuel(helper, new ItemStack(Items.COAL), 1600);
    assertFuel(helper, new ItemStack(RailcraftItems.COAL_COKE.get()), 3200);
    assertFuel(helper, new ItemStack(RailcraftItems.COAL_COKE_BLOCK.get()), 32000);
    assertFuel(helper, new ItemStack(RailcraftItems.CREOSOTE_BUCKET.get()), 800);
    assertFuel(helper, new ItemStack(Items.STONE), 0);
    for (var item : new net.minecraft.world.item.Item[]{
        RailcraftItems.REFINED_FIRESTONE.get(), RailcraftItems.CRACKED_FIRESTONE.get()}) {
      var stack = new ItemStack(item);
      assertFuel(helper, stack, item == RailcraftItems.REFINED_FIRESTONE.get() ? 250 : 100);
      stack.setDamageValue(stack.getMaxDamage());
      assertFuel(helper, stack, 0);
    }
    helper.succeed();
  }

  private static void assertFuel(GameTestHelper helper, ItemStack stack, int expected) {
    helper.assertTrue(FuelUtil.burnTime(stack, helper.getLevel()) == expected,
        "Machine burn time should match for " + stack);
    // Query the data component independently, as a vanilla furnace does. This also
    // verifies that exhausted firestones cannot burn in vanilla containers.
    var keys = new ContextKeySet.Builder().required(NeoForgeLootContextParams.QUERIED_STACK).build();
    var params = new LootParams.Builder(helper.getLevel())
        .withParameter(NeoForgeLootContextParams.QUERIED_STACK, stack).create(keys);
    var context = new LootContext.Builder(params).create(Optional.empty());
    int actual = ResolvableInt.getFromItem(stack, DataComponents.COOKING_FUEL,
        CookingFuel::burnTime, context, 0);
    helper.assertTrue(actual == expected, "Vanilla burn time should match for " + stack);
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Fuel Components");
  }
}

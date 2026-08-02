package mods.railcraft.world.entity.npc;

import java.util.Optional;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.tags.RailcraftTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class RailcraftTradeSets {

  public static final ResourceKey<TradeSet> TRACKMAN_LEVEL_1 = create("trackman/level_1");
  public static final ResourceKey<TradeSet> TRACKMAN_LEVEL_2 = create("trackman/level_2");
  public static final ResourceKey<TradeSet> TRACKMAN_LEVEL_3 = create("trackman/level_3");

  public static final ResourceKey<TradeSet> CARTMAN_LEVEL_1 = create("cartman/level_1");
  public static final ResourceKey<TradeSet> CARTMAN_LEVEL_2 = create("cartman/level_2");
  public static final ResourceKey<TradeSet> CARTMAN_LEVEL_3 = create("cartman/level_3");

  public static void bootstrap(BootstrapContext<TradeSet> context) {
    // The amounts mirror how many offers the pre-26.1 trades added per level.
    register(context, TRACKMAN_LEVEL_1, RailcraftTags.VillagerTrades.TRACKMAN_LEVEL_1, 3);
    register(context, TRACKMAN_LEVEL_2, RailcraftTags.VillagerTrades.TRACKMAN_LEVEL_2, 2);
    register(context, TRACKMAN_LEVEL_3, RailcraftTags.VillagerTrades.TRACKMAN_LEVEL_3, 7);

    register(context, CARTMAN_LEVEL_1, RailcraftTags.VillagerTrades.CARTMAN_LEVEL_1, 2);
    register(context, CARTMAN_LEVEL_2, RailcraftTags.VillagerTrades.CARTMAN_LEVEL_2, 2);
    register(context, CARTMAN_LEVEL_3, RailcraftTags.VillagerTrades.CARTMAN_LEVEL_3, 3);
  }

  private static void register(BootstrapContext<TradeSet> context, ResourceKey<TradeSet> key,
      TagKey<VillagerTrade> trades, int amount) {
    register(context, key, trades, ConstantValue.exactly(amount));
  }

  private static void register(BootstrapContext<TradeSet> context, ResourceKey<TradeSet> key,
      TagKey<VillagerTrade> trades, NumberProvider amount) {
    context.register(key, new TradeSet(
        context.lookup(Registries.VILLAGER_TRADE).getOrThrow(trades),
        amount,
        false,
        Optional.of(key.identifier().withPrefix("trade_set/"))));
  }

  private static ResourceKey<TradeSet> create(String path) {
    return ResourceKey.create(Registries.TRADE_SET, RailcraftConstants.id(path));
  }
}

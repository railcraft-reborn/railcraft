package mods.railcraft.world.entity.npc;

import java.util.List;
import java.util.Optional;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.TradeCost;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredItem;

public class RailcraftVillagerTrades {

  /**
   * How often an offer may be used before the villager needs to restock. Matches the value the
   * pre-26.1 trades used.
   */
  private static final int MAX_USES = 12;
  /** Reduced max uses for the expensive tools, which used to be handed out sparingly. */
  private static final int RARE_MAX_USES = 3;
  private static final int XP = 15;
  /** Vanilla's discount for bulk trades. */
  private static final float BULK_DISCOUNT = 0.05F;
  /** Vanilla's discount for equipment trades. */
  private static final float EQUIPMENT_DISCOUNT = 0.2F;

  private static final List<DeferredItem<?>> TRACK_KITS = List.of(
      RailcraftItems.ACTIVATOR_TRACK_KIT,
      RailcraftItems.BOOSTER_TRACK_KIT,
      RailcraftItems.COUPLER_TRACK_KIT,
      RailcraftItems.CONTROL_TRACK_KIT,
      RailcraftItems.WHISTLE_TRACK_KIT,
      RailcraftItems.BUFFER_STOP_TRACK_KIT,
      RailcraftItems.DETECTOR_TRACK_KIT,
      RailcraftItems.DISEMBARKING_TRACK_KIT,
      RailcraftItems.EMBARKING_TRACK_KIT,
      RailcraftItems.DUMPING_TRACK_KIT,
      RailcraftItems.GATED_TRACK_KIT,
      RailcraftItems.LAUNCHER_TRACK_KIT,
      RailcraftItems.LOCKING_TRACK_KIT,
      RailcraftItems.LOCOMOTIVE_TRACK_KIT,
      RailcraftItems.ONE_WAY_TRACK_KIT,
      RailcraftItems.ROUTING_TRACK_KIT,
      RailcraftItems.THROTTLE_TRACK_KIT,
      RailcraftItems.TRANSITION_TRACK_KIT);

  private static final List<DeferredItem<?>> CHEAP_CARTS = List.of(
      RailcraftItems.CARGO_MINECART,
      RailcraftItems.TANK_MINECART);

  private static final List<DeferredItem<?>> EXPENSIVE_CARTS = List.of(
      RailcraftItems.ELECTRIC_LOCOMOTIVE,
      RailcraftItems.STEAM_LOCOMOTIVE,
      RailcraftItems.WORLD_SPIKE,
      RailcraftItems.VOID_CHEST_MINECART,
      RailcraftItems.TRACK_LAYER,
      RailcraftItems.TRACK_RELAYER,
      RailcraftItems.TRACK_REMOVER,
      RailcraftItems.TRACK_UNDERCUTTER,
      RailcraftItems.TUNNEL_BORE);

  private static final List<Item> VANILLA_CARTS = List.of(
      Items.MINECART,
      Items.CHEST_MINECART,
      Items.HOPPER_MINECART,
      Items.TNT_MINECART);

  // Trackman
  public static final ResourceKey<VillagerTrade> TRACKMAN_1_COAL_EMERALD =
      create("trackman/1/coal_emerald");
  public static final ResourceKey<VillagerTrade> TRACKMAN_1_COAL_COKE_EMERALD =
      create("trackman/1/coal_coke_emerald");
  public static final ResourceKey<VillagerTrade> TRACKMAN_1_EMERALD_RAIL =
      create("trackman/1/emerald_rail");
  public static final List<ResourceKey<VillagerTrade>> TRACKMAN_TRACK_KITS =
      keysFor("trackman/track_kit/", TRACK_KITS);
  public static final ResourceKey<VillagerTrade> TRACKMAN_3_EMERALD_STEEL_CROWBAR =
      create("trackman/3/emerald_steel_crowbar");
  public static final ResourceKey<VillagerTrade> TRACKMAN_3_EMERALD_WHISTLE_TUNER =
      create("trackman/3/emerald_whistle_tuner");
  public static final ResourceKey<VillagerTrade> TRACKMAN_3_EMERALD_SIGNAL_BLOCK_SURVEYOR =
      create("trackman/3/emerald_signal_block_surveyor");
  public static final ResourceKey<VillagerTrade> TRACKMAN_3_EMERALD_SIGNAL_TUNER =
      create("trackman/3/emerald_signal_tuner");
  public static final ResourceKey<VillagerTrade> TRACKMAN_3_EMERALD_GOGGLES =
      create("trackman/3/emerald_goggles");
  public static final ResourceKey<VillagerTrade> TRACKMAN_3_EMERALD_OVERALLS =
      create("trackman/3/emerald_overalls");

  // Cartman
  public static final ResourceKey<VillagerTrade> CARTMAN_1_COAL_EMERALD =
      create("cartman/1/coal_emerald");
  public static final ResourceKey<VillagerTrade> CARTMAN_1_COAL_COKE_EMERALD =
      create("cartman/1/coal_coke_emerald");
  public static final List<ResourceKey<VillagerTrade>> CARTMAN_2_CARTS =
      keysFor("cartman/2/cart/", CHEAP_CARTS);
  public static final List<ResourceKey<VillagerTrade>> CARTMAN_2_VANILLA_CARTS =
      vanillaKeysFor("cartman/2/cart/", VANILLA_CARTS);
  public static final List<ResourceKey<VillagerTrade>> CARTMAN_3_CARTS =
      keysFor("cartman/3/cart/", CHEAP_CARTS);
  public static final List<ResourceKey<VillagerTrade>> CARTMAN_3_VANILLA_CARTS =
      vanillaKeysFor("cartman/3/cart/", VANILLA_CARTS);
  public static final List<ResourceKey<VillagerTrade>> CARTMAN_3_LOCOMOTIVES =
      keysFor("cartman/3/locomotive/", EXPENSIVE_CARTS);

  // Toolsmith
  public static final ResourceKey<VillagerTrade> TOOLSMITH_1_COAL_EMERALD =
      create("toolsmith/1/coal_emerald");
  public static final ResourceKey<VillagerTrade> TOOLSMITH_1_COAL_COKE_EMERALD =
      create("toolsmith/1/coal_coke_emerald");
  public static final ResourceKey<VillagerTrade> TOOLSMITH_1_IRON_EMERALD =
      create("toolsmith/1/iron_emerald");
  public static final ResourceKey<VillagerTrade> TOOLSMITH_2_EMERALD_IRON_STEEL_INGOT =
      create("toolsmith/2/emerald_iron_steel_ingot");
  public static final ResourceKey<VillagerTrade> TOOLSMITH_2_EMERALD_STEEL_INGOT =
      create("toolsmith/2/emerald_steel_ingot");
  public static final ResourceKey<VillagerTrade> TOOLSMITH_2_EMERALD_SLAG =
      create("toolsmith/2/emerald_slag");
  public static final ResourceKey<VillagerTrade> TOOLSMITH_3_EMERALD_STEEL_GEAR =
      create("toolsmith/3/emerald_steel_gear");

  // Armorer
  public static final ResourceKey<VillagerTrade> ARMORER_1_COAL_EMERALD =
      create("armorer/1/coal_emerald");
  public static final ResourceKey<VillagerTrade> ARMORER_1_COAL_COKE_EMERALD =
      create("armorer/1/coal_coke_emerald");
  public static final ResourceKey<VillagerTrade> ARMORER_2_COPPER_EMERALD =
      create("armorer/2/copper_emerald");
  public static final ResourceKey<VillagerTrade> ARMORER_2_TIN_EMERALD =
      create("armorer/2/tin_emerald");
  public static final ResourceKey<VillagerTrade> ARMORER_2_ZINC_EMERALD =
      create("armorer/2/zinc_emerald");
  public static final ResourceKey<VillagerTrade> ARMORER_2_NICKEL_EMERALD =
      create("armorer/2/nickel_emerald");
  public static final ResourceKey<VillagerTrade> ARMORER_3_EMERALD_BRASS_INGOT =
      create("armorer/3/emerald_brass_ingot");
  public static final ResourceKey<VillagerTrade> ARMORER_3_EMERALD_BRONZE_INGOT =
      create("armorer/3/emerald_bronze_ingot");
  public static final ResourceKey<VillagerTrade> ARMORER_3_EMERALD_INVAR_INGOT =
      create("armorer/3/emerald_invar_ingot");
  public static final ResourceKey<VillagerTrade> ARMORER_3_EMERALD_BRONZE_GEAR =
      create("armorer/3/emerald_bronze_gear");
  public static final ResourceKey<VillagerTrade> ARMORER_3_EMERALD_BRASS_GEAR =
      create("armorer/3/emerald_brass_gear");
  public static final ResourceKey<VillagerTrade> ARMORER_3_EMERALD_INVAR_GEAR =
      create("armorer/3/emerald_invar_gear");

  public static void bootstrap(BootstrapContext<VillagerTrade> context) {
    bootstrapTrackman(context);
    bootstrapCartman(context);
    bootstrapToolsmith(context);
    bootstrapArmorer(context);
  }

  private static void bootstrapTrackman(BootstrapContext<VillagerTrade> context) {
    context.register(TRACKMAN_1_COAL_EMERALD, sell(cost(Items.COAL, 16, 24), Items.EMERALD));
    context.register(TRACKMAN_1_COAL_COKE_EMERALD,
        sell(cost(RailcraftItems.COAL_COKE, 16, 24), Items.EMERALD));
    context.register(TRACKMAN_1_EMERALD_RAIL,
        sellStack(cost(Items.EMERALD, 2, 3), Items.RAIL, 30, 34));

    for (int i = 0; i < TRACK_KITS.size(); i++) {
      context.register(TRACKMAN_TRACK_KITS.get(i),
          sell(cost(Items.EMERALD, 2, 6), TRACK_KITS.get(i)));
    }

    context.register(TRACKMAN_3_EMERALD_STEEL_CROWBAR,
        sellRare(cost(Items.EMERALD, 24, 52), RailcraftItems.STEEL_CROWBAR));
    context.register(TRACKMAN_3_EMERALD_WHISTLE_TUNER,
        sell(cost(Items.EMERALD, 1, 2), RailcraftItems.WHISTLE_TUNER));
    context.register(TRACKMAN_3_EMERALD_SIGNAL_BLOCK_SURVEYOR,
        sell(cost(Items.EMERALD, 6, 8), RailcraftItems.SIGNAL_BLOCK_SURVEYOR));
    context.register(TRACKMAN_3_EMERALD_SIGNAL_TUNER,
        sell(cost(Items.EMERALD, 6, 8), RailcraftItems.SIGNAL_TUNER));
    context.register(TRACKMAN_3_EMERALD_GOGGLES,
        sell(cost(Items.EMERALD, 4, 8), RailcraftItems.GOGGLES));
    context.register(TRACKMAN_3_EMERALD_OVERALLS,
        sellRare(cost(Items.EMERALD, 19, 32), RailcraftItems.OVERALLS));
  }

  private static void bootstrapCartman(BootstrapContext<VillagerTrade> context) {
    context.register(CARTMAN_1_COAL_EMERALD, sell(cost(Items.COAL, 16, 24), Items.EMERALD));
    context.register(CARTMAN_1_COAL_COKE_EMERALD,
        sell(cost(RailcraftItems.COAL_COKE, 8, 12), Items.EMERALD));

    registerCarts(context, CARTMAN_2_CARTS, CHEAP_CARTS, 4, 7);
    registerVanillaCarts(context, CARTMAN_2_VANILLA_CARTS, 4, 7);
    registerCarts(context, CARTMAN_3_CARTS, CHEAP_CARTS, 3, 5);
    registerVanillaCarts(context, CARTMAN_3_VANILLA_CARTS, 3, 5);
    registerCarts(context, CARTMAN_3_LOCOMOTIVES, EXPENSIVE_CARTS, 30, 40);
  }

  private static void bootstrapToolsmith(BootstrapContext<VillagerTrade> context) {
    context.register(TOOLSMITH_1_COAL_EMERALD, sell(cost(Items.COAL, 16, 24), Items.EMERALD));
    context.register(TOOLSMITH_1_COAL_COKE_EMERALD,
        sell(cost(RailcraftItems.COAL_COKE, 8, 12), Items.EMERALD));
    context.register(TOOLSMITH_1_IRON_EMERALD,
        sell(cost(Items.IRON_INGOT, 7, 9), Items.EMERALD));

    context.register(TOOLSMITH_2_EMERALD_IRON_STEEL_INGOT,
        new VillagerTrade(cost(Items.EMERALD, 1, 2),
            Optional.of(new TradeCost(Items.IRON_INGOT, 1)),
            new ItemStackTemplate(RailcraftItems.STEEL_INGOT.asItem()),
            MAX_USES, XP, BULK_DISCOUNT, Optional.empty(), List.of()));
    context.register(TOOLSMITH_2_EMERALD_STEEL_INGOT,
        sell(cost(Items.EMERALD, 3, 4), RailcraftItems.STEEL_INGOT));
    context.register(TOOLSMITH_2_EMERALD_SLAG,
        sellStack(cost(Items.EMERALD, 2, 4), RailcraftItems.SLAG, 1, 2));
    context.register(TOOLSMITH_3_EMERALD_STEEL_GEAR,
        sell(cost(Items.EMERALD, 9, 16), RailcraftItems.STEEL_GEAR));
  }

  private static void bootstrapArmorer(BootstrapContext<VillagerTrade> context) {
    context.register(ARMORER_1_COAL_EMERALD, sell(cost(Items.COAL, 16, 24), Items.EMERALD));
    context.register(ARMORER_1_COAL_COKE_EMERALD,
        sell(cost(RailcraftItems.COAL_COKE, 4, 6), Items.EMERALD));

    context.register(ARMORER_2_COPPER_EMERALD,
        sell(cost(Items.COPPER_INGOT, 7, 9), Items.EMERALD));
    context.register(ARMORER_2_TIN_EMERALD,
        sell(cost(RailcraftItems.TIN_INGOT, 7, 9), Items.EMERALD));
    context.register(ARMORER_2_ZINC_EMERALD,
        sell(cost(RailcraftItems.ZINC_INGOT, 7, 9), Items.EMERALD));
    context.register(ARMORER_2_NICKEL_EMERALD,
        sell(cost(RailcraftItems.NICKEL_INGOT, 7, 9), Items.EMERALD));

    context.register(ARMORER_3_EMERALD_BRASS_INGOT,
        sell(cost(Items.EMERALD, 2, 3), RailcraftItems.BRASS_INGOT));
    context.register(ARMORER_3_EMERALD_BRONZE_INGOT,
        sell(cost(Items.EMERALD, 2, 3), RailcraftItems.BRONZE_INGOT));
    context.register(ARMORER_3_EMERALD_INVAR_INGOT,
        sell(cost(Items.EMERALD, 2, 3), RailcraftItems.INVAR_INGOT));
    context.register(ARMORER_3_EMERALD_BRONZE_GEAR,
        sell(cost(Items.EMERALD, 6, 12), RailcraftItems.BRONZE_GEAR));
    context.register(ARMORER_3_EMERALD_BRASS_GEAR,
        sell(cost(Items.EMERALD, 6, 12), RailcraftItems.BRASS_GEAR));
    context.register(ARMORER_3_EMERALD_INVAR_GEAR,
        sell(cost(Items.EMERALD, 6, 12), RailcraftItems.INVAR_GEAR));
  }

  private static void registerCarts(BootstrapContext<VillagerTrade> context,
      List<ResourceKey<VillagerTrade>> keys, List<DeferredItem<?>> carts, int minPrice,
      int maxPrice) {
    for (int i = 0; i < carts.size(); i++) {
      context.register(keys.get(i), sell(cost(Items.EMERALD, minPrice, maxPrice), carts.get(i)));
    }
  }

  private static void registerVanillaCarts(BootstrapContext<VillagerTrade> context,
      List<ResourceKey<VillagerTrade>> keys, int minPrice, int maxPrice) {
    for (int i = 0; i < VANILLA_CARTS.size(); i++) {
      context.register(keys.get(i),
          sell(cost(Items.EMERALD, minPrice, maxPrice), VANILLA_CARTS.get(i)));
    }
  }

  private static VillagerTrade sell(TradeCost wants, ItemLike gives) {
    return sell(wants, gives, MAX_USES, BULK_DISCOUNT);
  }

  private static VillagerTrade sellRare(TradeCost wants, ItemLike gives) {
    return sell(wants, gives, RARE_MAX_USES, EQUIPMENT_DISCOUNT);
  }

  private static VillagerTrade sell(TradeCost wants, ItemLike gives, int maxUses, float discount) {
    return new VillagerTrade(wants, new ItemStackTemplate(gives.asItem()), maxUses, XP, discount,
        Optional.empty(), List.of());
  }

  /** Sells a random amount of {@code gives}, between {@code min} and {@code max}. */
  private static VillagerTrade sellStack(TradeCost wants, ItemLike gives, int min, int max) {
    return new VillagerTrade(wants, new ItemStackTemplate(gives.asItem()), MAX_USES, XP,
        BULK_DISCOUNT, Optional.empty(),
        List.of(SetItemCountFunction.setCount(UniformGenerator.between(min, max)).build()));
  }

  private static TradeCost cost(ItemLike item, int min, int max) {
    return new TradeCost(item, UniformGenerator.between(min, max));
  }

  private static List<ResourceKey<VillagerTrade>> keysFor(String prefix,
      List<DeferredItem<?>> items) {
    return items.stream()
        .map(item -> create(prefix + item.getId().getPath()))
        .toList();
  }

  private static List<ResourceKey<VillagerTrade>> vanillaKeysFor(String prefix, List<Item> items) {
    return items.stream()
        .map(item -> create(prefix + item.builtInRegistryHolder().key().identifier().getPath()))
        .toList();
  }

  private static ResourceKey<VillagerTrade> create(String path) {
    return ResourceKey.create(Registries.VILLAGER_TRADE, RailcraftConstants.id(path));
  }
}

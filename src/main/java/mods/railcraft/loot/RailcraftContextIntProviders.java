package mods.railcraft.loot;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootPredicates;
import net.minecraft.world.level.storage.loot.providers.number.ints.ConditionalValue;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.Quotient;

/**
 * Burn times of Railcraft fuels, referenced by the {@code minecraft:cooking_fuel} item component.
 * Like vanilla fuels, they burn twice as fast in smokers and blast furnaces.
 */
public final class RailcraftContextIntProviders {

  public static final int COAL_COKE_BURN_TIME = 3200;
  public static final int COAL_COKE_BLOCK_BURN_TIME = COAL_COKE_BURN_TIME * 10;
  public static final int CREOSOTE_BUCKET_BURN_TIME = 800;
  public static final int REFINED_FIRESTONE_BURN_TIME = 250;
  public static final int CRACKED_FIRESTONE_BURN_TIME = 100;

  public static final ResourceKey<ContextIntProvider> COOKING_TIME_COAL_COKE =
      createKey("cooking/time_coal_coke");
  public static final ResourceKey<ContextIntProvider> COOKING_TIME_COAL_COKE_BLOCK =
      createKey("cooking/time_coal_coke_block");
  public static final ResourceKey<ContextIntProvider> COOKING_TIME_CREOSOTE_BUCKET =
      createKey("cooking/time_creosote_bucket");
  public static final ResourceKey<ContextIntProvider> COOKING_TIME_REFINED_FIRESTONE =
      createKey("cooking/time_refined_firestone");
  public static final ResourceKey<ContextIntProvider> COOKING_TIME_CRACKED_FIRESTONE =
      createKey("cooking/time_cracked_firestone");

  private RailcraftContextIntProviders() {}

  private static ResourceKey<ContextIntProvider> createKey(String path) {
    return ResourceKey.create(Registries.CONTEXT_INT_PROVIDER, RailcraftConstants.id(path));
  }

  public static void bootstrap(BootstrapContext<ContextIntProvider> context) {
    var predicates = context.lookup(Registries.PREDICATE);
    var providers = context.lookup(Registries.CONTEXT_INT_PROVIDER);
    var fastFurnace = predicates.getOrThrow(LootPredicates.FAST_FURNACE);
    var normalDivisor =
        providers.getOrThrow(ContextIntProviders.COOKING_NORMAL_BURN_TIME_REDUCTION_FACTOR);
    var fastDivisor =
        providers.getOrThrow(ContextIntProviders.COOKING_FAST_BURN_TIME_REDUCTION_FACTOR);

    context.register(COOKING_TIME_COAL_COKE,
        cooking(fastFurnace, normalDivisor, fastDivisor, COAL_COKE_BURN_TIME));
    context.register(COOKING_TIME_COAL_COKE_BLOCK,
        cooking(fastFurnace, normalDivisor, fastDivisor, COAL_COKE_BLOCK_BURN_TIME));
    context.register(COOKING_TIME_CREOSOTE_BUCKET,
        cooking(fastFurnace, normalDivisor, fastDivisor, CREOSOTE_BUCKET_BURN_TIME));
    context.register(COOKING_TIME_REFINED_FIRESTONE, new ChargedValue(Holder.direct(
        cooking(fastFurnace, normalDivisor, fastDivisor, REFINED_FIRESTONE_BURN_TIME))));
    context.register(COOKING_TIME_CRACKED_FIRESTONE, new ChargedValue(Holder.direct(
        cooking(fastFurnace, normalDivisor, fastDivisor, CRACKED_FIRESTONE_BURN_TIME))));
  }

  private static ContextIntProvider cooking(Holder<LootItemCondition> fastFurnace,
      Holder<ContextIntProvider> normalDivisor, Holder<ContextIntProvider> fastDivisor,
      int burnTime) {
    var divisor = Holder.<ContextIntProvider>direct(
        new ConditionalValue(fastFurnace, fastDivisor, normalDivisor));
    return new Quotient(ContextIntProviders.exactly(burnTime), divisor);
  }
}

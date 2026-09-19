package mods.railcraft.world.entity.npc;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.entity.ai.village.poi.RailcraftPoiTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.trading.TradeSet;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RailcraftVillagerProfession {

  private static final DeferredRegister<VillagerProfession> deferredRegister =
      DeferredRegister.create(BuiltInRegistries.VILLAGER_PROFESSION, RailcraftConstants.ID);

  public static final DeferredHolder<VillagerProfession, VillagerProfession> TRACKMAN =
      deferredRegister.register("trackman", () -> new VillagerProfession(
          name("trackman"),
          holder -> holder.is(RailcraftPoiTypes.MANUAL_ROLLING_MACHINE_POI.getKey()),
          holder -> holder.is(RailcraftPoiTypes.MANUAL_ROLLING_MACHINE_POI.getKey()),
          ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_ARMORER,
          tradeSetsByLevel(RailcraftTradeSets.TRACKMAN_LEVEL_1,
              RailcraftTradeSets.TRACKMAN_LEVEL_2, RailcraftTradeSets.TRACKMAN_LEVEL_3)));

  public static final DeferredHolder<VillagerProfession, VillagerProfession> CARTMAN =
      deferredRegister.register("cartman", () -> new VillagerProfession(
          name("cartman"),
          holder -> holder.is(RailcraftPoiTypes.POWERED_ROLLING_MACHINE_POI.getKey()),
          holder -> holder.is(RailcraftPoiTypes.POWERED_ROLLING_MACHINE_POI.getKey()),
          ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_ARMORER,
          tradeSetsByLevel(RailcraftTradeSets.CARTMAN_LEVEL_1, RailcraftTradeSets.CARTMAN_LEVEL_2,
              RailcraftTradeSets.CARTMAN_LEVEL_3)));

  /** Maps the given trade sets to the villager levels 1..n, the way vanilla professions do. */
  @SafeVarargs
  private static Int2ObjectMap<ResourceKey<TradeSet>> tradeSetsByLevel(
      ResourceKey<TradeSet>... byLevel) {
    var tradeSets = new Int2ObjectOpenHashMap<ResourceKey<TradeSet>>(byLevel.length);
    for (int level = 1; level <= byLevel.length; level++) {
      tradeSets.put(level, byLevel[level - 1]);
    }
    return tradeSets;
  }

  private static Component name(String path) {
    return Component.translatable("entity." + RailcraftConstants.ID + ".villager." + path);
  }

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }
}

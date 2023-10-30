package mods.railcraft.world.entity.npc;

import com.google.common.collect.ImmutableSet;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.entity.ai.village.poi.RailcraftPoiTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

public class RailcraftVillagerProfession {

  private static final DeferredRegister<VillagerProfession> deferredRegister =
      DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, RailcraftConstants.ID);

  public static final RegistryObject<VillagerProfession> TRACKMAN = deferredRegister
      .register("trackman", () -> new VillagerProfession("trackman",
          holder -> holder.is(RailcraftPoiTypes.MANUAL_ROLLING_MACHINE_POI.getKey()),
          holder -> holder.is(RailcraftPoiTypes.MANUAL_ROLLING_MACHINE_POI.getKey()),
          ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_ARMORER));

  public static final RegistryObject<VillagerProfession> CARTMAN = deferredRegister
      .register("cartman", () -> new VillagerProfession("cartman",
          holder -> holder.is(RailcraftPoiTypes.POWERED_ROLLING_MACHINE_POI.getKey()),
          holder -> holder.is(RailcraftPoiTypes.POWERED_ROLLING_MACHINE_POI.getKey()),
          ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_ARMORER));

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }
}

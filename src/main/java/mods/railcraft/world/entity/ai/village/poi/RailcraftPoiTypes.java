package mods.railcraft.world.entity.ai.village.poi;

import com.google.common.collect.ImmutableSet;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

public class RailcraftPoiTypes {

  private static final DeferredRegister<PoiType> deferredRegister =
      DeferredRegister.create(ForgeRegistries.POI_TYPES, RailcraftConstants.ID);

  public static final RegistryObject<PoiType> MANUAL_ROLLING_MACHINE_POI = deferredRegister
      .register("manual_rolling_machine_poi",
          () -> new PoiType(ImmutableSet.copyOf(RailcraftBlocks.MANUAL_ROLLING_MACHINE.get()
              .getStateDefinition().getPossibleStates()), 1, 1));

  public static final RegistryObject<PoiType> POWERED_ROLLING_MACHINE_POI = deferredRegister
      .register("powered_rolling_machine_poi",
          () -> new PoiType(ImmutableSet.copyOf(RailcraftBlocks.POWERED_ROLLING_MACHINE.get()
              .getStateDefinition().getPossibleStates()), 1, 1));

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }
}

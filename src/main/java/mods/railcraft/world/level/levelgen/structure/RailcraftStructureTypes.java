package mods.railcraft.world.level.levelgen.structure;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryObject;

public class RailcraftStructureTypes {

  private static final DeferredRegister<StructureType<?>> deferredRegister =
      DeferredRegister.create(Registries.STRUCTURE_TYPE, RailcraftConstants.ID);

  public static final RegistryObject<StructureType<GeodeStructure>> GEODE =
      deferredRegister.register("geode",
          () -> () -> Structure.simpleCodec(GeodeStructure::new));

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }
}

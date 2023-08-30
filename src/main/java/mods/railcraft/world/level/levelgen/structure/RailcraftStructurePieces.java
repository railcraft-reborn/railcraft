package mods.railcraft.world.level.levelgen.structure;

import mods.railcraft.Railcraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftStructurePieces {

  private static final DeferredRegister<StructurePieceType> deferredRegister =
      DeferredRegister.create(Registries.STRUCTURE_PIECE, Railcraft.ID);

  public static final RegistryObject<StructurePieceType.ContextlessType> GEODE =
      deferredRegister.register("geode", () -> GeodeStructurePiece::new);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }
}

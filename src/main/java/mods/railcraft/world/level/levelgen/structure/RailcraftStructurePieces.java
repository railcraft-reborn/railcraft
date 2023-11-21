package mods.railcraft.world.level.levelgen.structure;

import java.util.function.Supplier;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RailcraftStructurePieces {

  private static final DeferredRegister<StructurePieceType> deferredRegister =
      DeferredRegister.create(Registries.STRUCTURE_PIECE, RailcraftConstants.ID);

  public static final Supplier<StructurePieceType.ContextlessType> GEODE =
      deferredRegister.register("geode", () -> GeodeStructurePiece::new);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }
}

package mods.railcraft.world.level.levelgen.structure;

import com.mojang.serialization.MapCodec;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RailcraftStructureProcessorTypes {

  private static final DeferredRegister<MapCodec<? extends StructureProcessor>> deferredRegister =
      DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, RailcraftConstants.ID);

  public static final DeferredHolder<MapCodec<? extends StructureProcessor>,
      MapCodec<BlockSwapProcessor>> BLOCK_SWAP =
      deferredRegister.register("block_swap", () -> BlockSwapProcessor.CODEC);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }
}

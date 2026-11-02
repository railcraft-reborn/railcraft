package mods.railcraft.data.gametest.instances.steam_engine;

import mods.railcraft.world.level.block.entity.SteamEngineBlockEntity;
import mods.railcraft.world.level.material.RailcraftFluids;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.fluids.FluidStack;

final class SteamEngineTestHelper {

  static final BlockPos ENGINE_POS = new BlockPos(3, 2, 3);

  private SteamEngineTestHelper() {}

  static void fillSteam(SteamEngineBlockEntity engine, int amount) {
    engine.getSteamTank().setFluid(new FluidStack(RailcraftFluids.STEAM.get(), amount));
  }
}

package mods.railcraft.data.gametest.instances.steam_engine;

import static mods.railcraft.data.gametest.instances.steam_engine.SteamEngineTestHelper.ENGINE_POS;

import com.mojang.serialization.MapCodec;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.SteamEngineBlock;
import mods.railcraft.world.level.material.RailcraftFluids;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class SteamEngineCapabilitiesFollowFacingTestInstance extends GameTestInstance {

  public static final MapCodec<SteamEngineCapabilitiesFollowFacingTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(SteamEngineCapabilitiesFollowFacingTestInstance::new);

  public SteamEngineCapabilitiesFollowFacingTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  /**
   * The engine exposes energy only on its output face, and fluids on every other face.
   */
  @Override
  public void run(GameTestHelper helper) {
    var level = helper.getLevel();
    var pos = helper.absolutePos(ENGINE_POS);
    var north = Direction.NORTH;
    helper.setBlock(ENGINE_POS, RailcraftBlocks.COMMERCIAL_STEAM_ENGINE.get().defaultBlockState()
        .setValue(SteamEngineBlock.FACING, north));

    helper.assertTrue(level.getCapability(Capabilities.Energy.BLOCK, pos, north) != null,
        "Energy should be exposed on the output face");
    helper.assertTrue(
        level.getCapability(Capabilities.Energy.BLOCK, pos, north.getOpposite()) == null,
        "Energy should not be exposed on the back face");
    helper.assertTrue(level.getCapability(Capabilities.Fluid.BLOCK, pos, north) == null,
        "Fluids should not be exposed on the output face");
    var fluidHandler = level.getCapability(Capabilities.Fluid.BLOCK, pos, north.getOpposite());
    helper.assertTrue(fluidHandler != null, "Fluids should be exposed on the back face");
    // Simulated insertions: the transactions are never committed
    try (var tx = Transaction.openRoot()) {
      helper.assertTrue(fluidHandler.insert(FluidResource.of(Fluids.WATER), 1000, tx) == 0,
          "Engine should not accept water");
    }
    try (var tx = Transaction.openRoot()) {
      helper.assertTrue(
          fluidHandler.insert(FluidResource.of(RailcraftFluids.STEAM.get()), 1000, tx) == 1000,
          "Engine should accept steam");
    }
    helper.succeed();
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Steam Engine Capabilities Follow Facing");
  }
}

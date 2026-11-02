package mods.railcraft.data.gametest.instances.steam_engine;

import com.mojang.serialization.MapCodec;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import static mods.railcraft.data.gametest.instances.steam_engine.SteamEngineTestHelper.ENGINE_POS;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.SteamEngineBlockEntity;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class UnpoweredSteamEngineIsIdleTestInstance extends GameTestInstance {

  public static final MapCodec<UnpoweredSteamEngineIsIdleTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(UnpoweredSteamEngineIsIdleTestInstance::new);

  public UnpoweredSteamEngineIsIdleTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  /**
   * Without a redstone signal the engine must not generate energy.
   */
  @Override
  public void run(GameTestHelper helper) {
    helper.setBlock(ENGINE_POS, RailcraftBlocks.INDUSTRIAL_STEAM_ENGINE.get());
    var engine = helper.getBlockEntity(ENGINE_POS, SteamEngineBlockEntity.class);
    SteamEngineTestHelper.fillSteam(engine, 8000);

    helper.runAtTickTime(40, () -> {
      helper.assertTrue(engine.getEnergyStorage().getAmountAsInt() == 0,
          "Unpowered engine should not generate energy");
      helper.assertTrue(engine.getSteamTank().getFluidAmount() < 8000,
          "Unpowered engine should vent steam");
      helper.succeed();
    });
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Unpowered Steam Engine Is Idle");
  }
}

package mods.railcraft.data.gametest.instances.steam_engine;

import com.mojang.serialization.MapCodec;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import static mods.railcraft.data.gametest.instances.steam_engine.SteamEngineTestHelper.ENGINE_POS;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.SteamEngineBlock;
import mods.railcraft.world.level.block.entity.SteamEngineBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class CommercialSteamEngineGeneratesEnergyTestInstance extends GameTestInstance {

  public static final MapCodec<CommercialSteamEngineGeneratesEnergyTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(CommercialSteamEngineGeneratesEnergyTestInstance::new);

  public CommercialSteamEngineGeneratesEnergyTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  /**
   * A powered commercial steam engine with steam must generate 40 FE/t.
   */
  @Override
  public void run(GameTestHelper helper) {
    helper.setBlock(ENGINE_POS, RailcraftBlocks.COMMERCIAL_STEAM_ENGINE.get().defaultBlockState()
        .setValue(SteamEngineBlock.FACING, Direction.UP));
    var engine = helper.getBlockEntity(ENGINE_POS, SteamEngineBlockEntity.class);
    SteamEngineTestHelper.fillSteam(engine, 8000);
    helper.setBlock(ENGINE_POS.east(), Blocks.REDSTONE_BLOCK);

    int[] energyBefore = {0};
    helper.runAtTickTime(5, () -> {
      helper.assertTrue(engine.isPowered(), "Engine should be powered");
      energyBefore[0] = engine.getEnergyStorage().getAmountAsInt();
    });
    helper.runAtTickTime(45, () -> {
      var generated = engine.getEnergyStorage().getAmountAsInt() - energyBefore[0];
      helper.assertTrue(generated == 40 * 40,
          "Engine should generate 40 FE/t, generated %s FE in 40 ticks".formatted(generated));
      helper.succeed();
    });
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Commercial Steam Engine Generates Energy");
  }
}

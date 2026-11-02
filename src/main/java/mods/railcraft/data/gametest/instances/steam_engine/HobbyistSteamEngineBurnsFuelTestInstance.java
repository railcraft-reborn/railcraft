package mods.railcraft.data.gametest.instances.steam_engine;

import com.mojang.serialization.MapCodec;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import static mods.railcraft.data.gametest.instances.steam_engine.SteamEngineTestHelper.ENGINE_POS;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.HobbyistSteamEngineBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class HobbyistSteamEngineBurnsFuelTestInstance extends GameTestInstance {

  public static final MapCodec<HobbyistSteamEngineBurnsFuelTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(HobbyistSteamEngineBurnsFuelTestInstance::new);

  public HobbyistSteamEngineBurnsFuelTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  /**
   * A hot hobbyist steam engine burns its fuel and turns water into energy, also after being
   * reloaded.
   */
  @Override
  public void run(GameTestHelper helper) {
    helper.setBlock(ENGINE_POS, RailcraftBlocks.HOBBYIST_STEAM_ENGINE.get());
    var engine = helper.getBlockEntity(ENGINE_POS, HobbyistSteamEngineBlockEntity.class);
    engine.getWaterTank().setFluid(new FluidStack(Fluids.WATER, 4000));
    engine.getContainer().setItem(HobbyistSteamEngineBlockEntity.SLOT_FUEL,
        new ItemStack(Items.COAL, 16));
    engine.getBoiler().setTemperature(engine.getBoiler().getMaxTemperature());
    helper.setBlock(ENGINE_POS.east(), Blocks.REDSTONE_BLOCK);

    helper.runAtTickTime(250, () -> {
      helper.assertTrue(engine.getContainer().getItem(HobbyistSteamEngineBlockEntity.SLOT_FUEL)
          .getCount() < 16, "Engine should burn fuel");
      helper.assertTrue(engine.getWaterTank().getFluidAmount() < 4000,
          "Engine should boil water");
      helper.assertTrue(engine.getEnergyStorage().getAmountAsInt() > 0,
          "Engine should generate energy");

      var registries = helper.getLevel().registryAccess();
      var reloaded = (HobbyistSteamEngineBlockEntity) BlockEntity.loadStatic(engine.getBlockPos(),
          engine.getBlockState(), engine.saveWithFullMetadata(registries), registries);
      helper.assertTrue(reloaded.getEnergyStorage().getAmountAsInt()
              == engine.getEnergyStorage().getAmountAsInt(),
          "Reloaded engine should keep its energy");
      helper.assertTrue(reloaded.getWaterTank().getFluidAmount()
              == engine.getWaterTank().getFluidAmount(),
          "Reloaded engine should keep its water");
      helper.succeed();
    });
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Hobbyist Steam Engine Burns Fuel");
  }
}

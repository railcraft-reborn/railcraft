package mods.railcraft;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.steamboiler.SteamBoilerBlockEntity;
import mods.railcraft.world.module.SteamBoilerModule;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(RailcraftConstants.ID)
@PrefixGameTestTemplate(false)
public class SteamBoilerTest {

  private static final int MEASURE_TICKS = 80;

  /**
   * Issue #147, a hot 3x3x4 high pressure boiler must make 20 mB/t of steam per tank, also after
   * being reloaded.
   */
  @GameTest(template = "platform_7x7_tall", timeoutTicks = 200)
  public static void highPressureBoilerSteamOutput(GameTestHelper helper) {
    for (int x = 1; x <= 3; x++) {
      for (int z = 1; z <= 3; z++) {
        helper.setBlock(new BlockPos(x, 2, z), RailcraftBlocks.SOLID_FUELED_FIREBOX.get());
        for (int y = 3; y <= 6; y++) {
          helper.setBlock(new BlockPos(x, y, z),
              RailcraftBlocks.HIGH_PRESSURE_STEAM_BOILER_TANK.get());
        }
      }
    }

    int[] steamBefore = {0};
    helper.runAtTickTime(5, () -> {
      var master = master(helper);
      var tanks = master.getCurrentPattern().get().getMetadata().tanks();
      helper.assertTrue(tanks == 36, "Boiler should have 36 tanks, got " + tanks);
      var module = master.getModule(SteamBoilerModule.class).get();
      var boiler = module.getBoiler();
      helper.assertTrue(boiler.getMaxTemperature() == 1000,
          "High pressure boiler should reach 1000, got " + boiler.getMaxTemperature());
      module.getWaterTank().internalFill(new FluidStack(Fluids.WATER, 100_000),
          FluidAction.EXECUTE);
      boiler.setTemperature(boiler.getMaxTemperature());

      // Reloading the boiler must keep it hot
      var registries = helper.getLevel().registryAccess();
      var reloaded = (SteamBoilerBlockEntity) BlockEntity.loadStatic(master.getBlockPos(),
          master.getBlockState(), master.saveWithId(registries), registries);
      var reloadedTemperature =
          reloaded.getModule(SteamBoilerModule.class).get().getBoiler().getTemperature();
      helper.assertTrue(reloadedTemperature == 1000,
          "Reloaded boiler should still be at 1000, got " + reloadedTemperature);

      steamBefore[0] = module.getSteamTank().getFluidAmount();
    });
    helper.runAtTickTime(5 + MEASURE_TICKS, () -> {
      var module = master(helper).getModule(SteamBoilerModule.class).get();
      var steam = module.getSteamTank().getFluidAmount() - steamBefore[0];
      var perTick = (double) steam / MEASURE_TICKS;
      helper.assertTrue(Math.abs(perTick - 720) <= 720 * 0.1,
          "Boiler should make about 720 mB/t, made %s mB/t".formatted(perTick));
      helper.succeed();
    });
  }

  private static SteamBoilerBlockEntity master(GameTestHelper helper) {
    SteamBoilerBlockEntity firebox = helper.getBlockEntity(new BlockPos(1, 2, 1));
    if (firebox.getMasterBlockEntity().isEmpty()) {
      firebox.evaluate();
    }
    return firebox.getMasterBlockEntity()
        .orElseThrow(() -> new GameTestAssertException("Boiler should be formed"));
  }
}

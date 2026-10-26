package mods.railcraft.data.gametest.instances;

import com.mojang.serialization.MapCodec;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.steamboiler.SteamBoilerBlockEntity;
import mods.railcraft.world.module.SteamBoilerModule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Issue #147, a hot 3x3x4 high pressure boiler must make 20 mB/t of steam per tank, also after
 * being reloaded.
 */
public class SteamBoilerTestInstance extends GameTestInstance {

  public static final MapCodec<SteamBoilerTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(SteamBoilerTestInstance::new);

  private static final int MEASURE_TICKS = 80;

  public SteamBoilerTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    for (int x = 1; x <= 3; x++) {
      for (int z = 1; z <= 3; z++) {
        helper.setBlock(new BlockPos(x, 1, z), RailcraftBlocks.SOLID_FUELED_FIREBOX.get());
        for (int y = 2; y <= 5; y++) {
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
      module.getWaterTank().setFluid(new FluidStack(Fluids.WATER, 100_000));
      boiler.setTemperature(boiler.getMaxTemperature());

      // Reloading the boiler must keep it hot
      var registries = helper.getLevel().registryAccess();
      var reloaded = (SteamBoilerBlockEntity) BlockEntity.loadStatic(master.getBlockPos(),
          master.getBlockState(), master.saveWithFullMetadata(registries), registries);
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
    var firebox = helper.getBlockEntity(new BlockPos(1, 1, 1), SteamBoilerBlockEntity.class);
    if (firebox.getMasterBlockEntity().isEmpty()) {
      firebox.evaluate();
    }
    return firebox.getMasterBlockEntity()
        .orElseThrow(() -> helper.assertionException(Component.literal("Boiler should be formed")));
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Steam Boiler");
  }
}

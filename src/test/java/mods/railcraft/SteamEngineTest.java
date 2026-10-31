package mods.railcraft;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.SteamEngineBlock;
import mods.railcraft.world.level.block.entity.HobbyistSteamEngineBlockEntity;
import mods.railcraft.world.level.block.entity.SteamEngineBlockEntity;
import mods.railcraft.world.level.material.RailcraftFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(RailcraftConstants.ID)
@PrefixGameTestTemplate(false)
public class SteamEngineTest {

  private static final BlockPos ENGINE_POS = new BlockPos(3, 2, 3);

  /**
   * A powered commercial steam engine with steam must generate 40 FE/t.
   */
  @GameTest(template = "platform_7x7", timeoutTicks = 100)
  public static void commercialSteamEngineGeneratesEnergy(GameTestHelper helper) {
    helper.setBlock(ENGINE_POS, RailcraftBlocks.COMMERCIAL_STEAM_ENGINE.get().defaultBlockState()
        .setValue(SteamEngineBlock.FACING, Direction.UP));
    SteamEngineBlockEntity engine = helper.getBlockEntity(ENGINE_POS);
    engine.getSteamTank().internalFill(new FluidStack(RailcraftFluids.STEAM.get(), 8000),
        FluidAction.EXECUTE);
    helper.setBlock(ENGINE_POS.east(), Blocks.REDSTONE_BLOCK);

    int[] energyBefore = {0};
    helper.runAtTickTime(5, () -> {
      helper.assertTrue(engine.isPowered(), "Engine should be powered");
      energyBefore[0] = engine.getEnergyStorage().getEnergyStored();
    });
    helper.runAtTickTime(45, () -> {
      var generated = engine.getEnergyStorage().getEnergyStored() - energyBefore[0];
      helper.assertTrue(generated == 40 * 40,
          "Engine should generate 40 FE/t, generated %s FE in 40 ticks".formatted(generated));
      helper.succeed();
    });
  }

  /**
   * Without a redstone signal the engine must not generate energy.
   */
  @GameTest(template = "platform_7x7", timeoutTicks = 100)
  public static void unpoweredSteamEngineIsIdle(GameTestHelper helper) {
    helper.setBlock(ENGINE_POS, RailcraftBlocks.INDUSTRIAL_STEAM_ENGINE.get());
    SteamEngineBlockEntity engine = helper.getBlockEntity(ENGINE_POS);
    engine.getSteamTank().internalFill(new FluidStack(RailcraftFluids.STEAM.get(), 8000),
        FluidAction.EXECUTE);

    helper.runAtTickTime(40, () -> {
      helper.assertTrue(engine.getEnergyStorage().getEnergyStored() == 0,
          "Unpowered engine should not generate energy");
      helper.assertTrue(engine.getSteamTank().getFluidAmount() < 8000,
          "Unpowered engine should vent steam");
      helper.succeed();
    });
  }

  /**
   * The engine exposes energy only on its output face, and fluids on every other face.
   */
  @GameTest(template = "platform_7x7")
  public static void steamEngineCapabilitiesFollowFacing(GameTestHelper helper) {
    var level = helper.getLevel();
    var pos = helper.absolutePos(ENGINE_POS);
    var north = helper.getTestRotation().rotate(Direction.NORTH);
    helper.setBlock(ENGINE_POS, RailcraftBlocks.COMMERCIAL_STEAM_ENGINE.get().defaultBlockState()
        .setValue(SteamEngineBlock.FACING, north));

    helper.assertTrue(level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, north) != null,
        "Energy should be exposed on the output face");
    helper.assertTrue(
        level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, north.getOpposite()) == null,
        "Energy should not be exposed on the back face");
    helper.assertTrue(level.getCapability(Capabilities.FluidHandler.BLOCK, pos, north) == null,
        "Fluids should not be exposed on the output face");
    var fluidHandler =
        level.getCapability(Capabilities.FluidHandler.BLOCK, pos, north.getOpposite());
    helper.assertTrue(fluidHandler != null, "Fluids should be exposed on the back face");
    helper.assertTrue(fluidHandler.fill(new FluidStack(Fluids.WATER, 1000),
            FluidAction.SIMULATE) == 0, "Engine should not accept water");
    helper.assertTrue(fluidHandler.fill(new FluidStack(RailcraftFluids.STEAM.get(), 1000),
            FluidAction.SIMULATE) == 1000, "Engine should accept steam");
    helper.succeed();
  }

  /**
   * A hot hobbyist steam engine burns its fuel and turns water into energy, also after being
   * reloaded.
   */
  @GameTest(template = "platform_7x7", timeoutTicks = 300)
  public static void hobbyistSteamEngineBurnsFuel(GameTestHelper helper) {
    helper.setBlock(ENGINE_POS, RailcraftBlocks.HOBBYIST_STEAM_ENGINE.get());
    HobbyistSteamEngineBlockEntity engine = helper.getBlockEntity(ENGINE_POS);
    engine.getWaterTank().internalFill(new FluidStack(Fluids.WATER, 4000), FluidAction.EXECUTE);
    engine.getContainer().setItem(HobbyistSteamEngineBlockEntity.SLOT_FUEL,
        new ItemStack(Items.COAL, 16));
    engine.getBoiler().setTemperature(engine.getBoiler().getMaxTemperature());
    helper.setBlock(ENGINE_POS.east(), Blocks.REDSTONE_BLOCK);

    helper.runAtTickTime(250, () -> {
      helper.assertTrue(engine.getContainer().getItem(HobbyistSteamEngineBlockEntity.SLOT_FUEL)
          .getCount() < 16, "Engine should burn fuel");
      helper.assertTrue(engine.getWaterTank().getFluidAmount() < 4000,
          "Engine should boil water");
      helper.assertTrue(engine.getEnergyStorage().getEnergyStored() > 0,
          "Engine should generate energy");

      var registries = helper.getLevel().registryAccess();
      var reloaded = (HobbyistSteamEngineBlockEntity) BlockEntity.loadStatic(engine.getBlockPos(),
          engine.getBlockState(), engine.saveWithId(registries), registries);
      helper.assertTrue(reloaded.getEnergyStorage().getEnergyStored()
              == engine.getEnergyStorage().getEnergyStored(),
          "Reloaded engine should keep its energy");
      helper.assertTrue(reloaded.getWaterTank().getFluidAmount()
              == engine.getWaterTank().getFluidAmount(),
          "Reloaded engine should keep its water");
      helper.succeed();
    });
  }
}

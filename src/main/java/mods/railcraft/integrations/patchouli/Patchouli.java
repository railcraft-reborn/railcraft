package mods.railcraft.integrations.patchouli;

import mods.railcraft.Railcraft;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.tank.IronTankGaugeBlock;
import mods.railcraft.world.level.block.tank.IronTankValveBlock;
import mods.railcraft.world.level.block.tank.IronTankWallBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import vazkii.patchouli.api.IStateMatcher;
import vazkii.patchouli.api.PatchouliAPI;

public class Patchouli {

  public static void setup() {
    var patchouliApi = PatchouliAPI.get();

    var crusherBlock = validBlock(patchouliApi, RailcraftBlocks.CRUSHER.get());
    var crusher = patchouliApi.makeMultiblock(new String[][]{
        {"BB", "BB", "BB"}, //Y:1
        {"BB", "0B", "BB"}  //Y:0
        }, 'B', crusherBlock, '0', crusherBlock
    ).setSymmetrical(false);
    patchouliApi.registerMultiblock(new ResourceLocation(Railcraft.ID, "crusher"), crusher);

    var cokeOvenBlock = validBlock(patchouliApi, RailcraftBlocks.COKE_OVEN_BRICKS.get());
    var cokeOven = patchouliApi.makeMultiblock(new String[][]{
        {"BBB", "BBB", "BBB"}, //Y:2
        {"BBB", "BAB", "BBB"}, //Y:1
        {"BBB", "B0B", "BBB"}  //Y:0
        }, 'B', cokeOvenBlock, '0', cokeOvenBlock, 'A', patchouliApi.airMatcher()
    ).setSymmetrical(true);
    patchouliApi.registerMultiblock(new ResourceLocation(Railcraft.ID, "coke_oven"), cokeOven);

    var blastFurnaceBlock = validBlock(patchouliApi, RailcraftBlocks.BLAST_FURNACE_BRICKS.get());
    var blastFurnace = patchouliApi.makeMultiblock(new String[][]{
        {"BBB", "BBB", "BBB"}, //Y:3
        {"BBB", "BAB", "BBB"}, //Y:2
        {"BBB", "BAB", "BBB"}, //Y:1
        {"BBB", "B0B", "BBB"}  //Y:0
        }, 'B', blastFurnaceBlock, '0', blastFurnaceBlock, 'A', patchouliApi.airMatcher()
    ).setSymmetrical(true);
    patchouliApi
        .registerMultiblock(new ResourceLocation(Railcraft.ID, "blast_furnace"), blastFurnace);

    var steamTurbineBlock = validBlock(patchouliApi, RailcraftBlocks.STEAM_TURBINE.get());
    var steamTurbine = patchouliApi.makeMultiblock(new String[][]{
        {"BB", "BB", "BB"}, //Y:1
        {"BB", "0B", "BB"}  //Y:0
        }, 'B', steamTurbineBlock, '0', steamTurbineBlock
    ).setSymmetrical(false);
    patchouliApi
        .registerMultiblock(new ResourceLocation(Railcraft.ID, "steam_turbine"), steamTurbine);

    var waterTankBlock = validBlock(patchouliApi, RailcraftBlocks.WATER_TANK_SIDING.get());
    var waterTank = patchouliApi.makeMultiblock(new String[][]{
        {"BB", "BB"}, //Y:1
        {"BB", "0B"}  //Y:0
        }, 'B', waterTankBlock, '0', waterTankBlock
    ).setSymmetrical(true);
    patchouliApi
        .registerMultiblock(new ResourceLocation(Railcraft.ID, "water_tank"), waterTank);

    var steamOvenBlock = validBlock(patchouliApi, RailcraftBlocks.STEAM_OVEN.get());
    var steamOven = patchouliApi.makeMultiblock(new String[][]{
        {"BB", "BB"}, //Y:1
        {"BB", "0B"}  //Y:0
        }, 'B', steamOvenBlock, '0', steamOvenBlock
    ).setSymmetrical(true);
    patchouliApi
        .registerMultiblock(new ResourceLocation(Railcraft.ID, "steam_oven"), steamOven);

    var ironTankWallBlock = validTankWallBlock(patchouliApi,
        RailcraftBlocks.IRON_TANK_WALL.variantFor(DyeColor.WHITE).get());
    var ironTankGaugeBlock = validTankGaugeBlock(patchouliApi,
        RailcraftBlocks.IRON_TANK_GAUGE.variantFor(DyeColor.WHITE).get());
    var ironTankValveBlock = validTankValveBlock(patchouliApi,
        RailcraftBlocks.IRON_TANK_VALVE.variantFor(DyeColor.WHITE).get());
    var tank = patchouliApi.makeMultiblock(new String[][]{
        {"BBBBB", "BBBBB", "BBVBB", "BBBBB", "BBBBB"}, //Y:4
        {"BGGGB", "GAAAG", "GAAAG", "GAAAG", "BGGGB"}, //Y:3
        {"BGGGB", "GAAAG", "GAAAG", "GAAAG", "BGGGB"}, //Y:2
        {"BGGGB", "GAAAG", "GAAAG", "GAAAG", "BGGGB"}, //Y:1
        {"BBBBB", "BBBBB", "BBVBB", "BBBBB", "BB0BB"}  //Y:0
        },
        'A', patchouliApi.airMatcher(),
        'B', ironTankWallBlock,
        'G', ironTankGaugeBlock,
        'V', ironTankValveBlock,
        '0', ironTankWallBlock
    ).setSymmetrical(true);
    patchouliApi
        .registerMultiblock(new ResourceLocation(Railcraft.ID, "iron_tank"), tank);
  }

  private static IStateMatcher validBlock(PatchouliAPI.IPatchouliAPI api, Block block) {
    return api.predicateMatcher(block, state -> state.is(block));
  }

  private static IStateMatcher validTankWallBlock(PatchouliAPI.IPatchouliAPI api,
      IronTankWallBlock block) {
    return api.predicateMatcher(block, state -> state.is(RailcraftTags.Blocks.IRON_TANK_WALL));
  }

  private static IStateMatcher validTankGaugeBlock(PatchouliAPI.IPatchouliAPI api,
      IronTankGaugeBlock block) {
    return api.predicateMatcher(block, state -> state.is(RailcraftTags.Blocks.IRON_TANK_GAUGE));
  }

  private static IStateMatcher validTankValveBlock(PatchouliAPI.IPatchouliAPI api,
      IronTankValveBlock block) {
    return api.predicateMatcher(block, state -> state.is(RailcraftTags.Blocks.IRON_TANK_VALVE));
  }
}

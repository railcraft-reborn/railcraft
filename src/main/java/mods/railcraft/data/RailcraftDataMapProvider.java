package mods.railcraft.data;

import java.util.concurrent.CompletableFuture;
import buildcraft.energy.BCEnergy;
import mods.railcraft.datamaps.FluidHeat;
import mods.railcraft.datamaps.RailcraftDataMaps;
import mods.railcraft.datamaps.SteamBoilerTank;
import mods.railcraft.datamaps.TunnelBoreHead;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

public class RailcraftDataMapProvider extends DataMapProvider {

  private static final int COAL_COKE_BURN_TIME = 3200;
  private static final int CREOSOTE_BUCKET_BURN_TIME = 800;

  public RailcraftDataMapProvider(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> registries) {
    super(packOutput, registries);
  }

  @Override
  protected void gather(HolderLookup.Provider registries) {
    this.builder(NeoForgeDataMaps.FURNACE_FUELS)
        .add(RailcraftBlocks.COAL_COKE_BLOCK.getId(), new FurnaceFuel(COAL_COKE_BURN_TIME * 10), false)
        .add(RailcraftItems.COAL_COKE.getId(), new FurnaceFuel(COAL_COKE_BURN_TIME), false)
        .add(RailcraftItems.CREOSOTE_BUCKET.getId(), new FurnaceFuel(CREOSOTE_BUCKET_BURN_TIME), false);

    this.builder(RailcraftDataMaps.TUNNEL_BORE_HEAD)
        .add(RailcraftItems.BRONZE_TUNNEL_BORE_HEAD, new TunnelBoreHead(1.25F), false)
        .add(RailcraftItems.STEEL_TUNNEL_BORE_HEAD, new TunnelBoreHead(1.25F), false)
        .add(RailcraftItems.DIAMOND_TUNNEL_BORE_HEAD, new TunnelBoreHead(1 / 0.6F), false)
        .add(RailcraftItems.NETHERITE_TUNNEL_BORE_HEAD, new TunnelBoreHead(1.875F), false)
        .add(RailcraftItems.IRON_TUNNEL_BORE_HEAD, new TunnelBoreHead(1), false);

    // Heat values ported from Railcraft 1.12.2
    var buildcraft = new ModLoadedCondition(BCEnergy.MODID);
    // TODO Forestry CE 26.1: uncomment when Forestry CE is released for 26.1.x
    // var forestry = new ModLoadedCondition(ForestryConstants.MOD_ID);
    this.builder(RailcraftDataMaps.FLUID_HEAT)
        .add(RailcraftTags.Fluids.CREOSOTE, new FluidHeat(4800), false)
        // TODO Forestry CE 26.1: uncomment when Forestry CE is released for 26.1.x
        // .add(forestry("ethanol"), new FluidHeat(16000), false, forestry)
        .add(buildcraft("oil_heavy"), new FluidHeat(42666), false, buildcraft)
        .add(buildcraft("oil_dense"), new FluidHeat(64000), false, buildcraft)
        .add(buildcraft("oil_distilled"), new FluidHeat(20000), false, buildcraft)
        .add(buildcraft("fuel_dense"), new FluidHeat(192000), false, buildcraft)
        .add(buildcraft("fuel_mixed_heavy"), new FluidHeat(51200), false, buildcraft)
        .add(buildcraft("fuel_light"), new FluidHeat(48000), false, buildcraft)
        .add(buildcraft("fuel_mixed_light"), new FluidHeat(16000), false, buildcraft)
        .add(buildcraft("fuel_gaseous"), new FluidHeat(8000), false, buildcraft);

    this.builder(RailcraftDataMaps.STEAM_BOILER_TANK)
        .add(RailcraftBlocks.LOW_PRESSURE_STEAM_BOILER_TANK, new SteamBoilerTank(1), false)
        .add(RailcraftBlocks.HIGH_PRESSURE_STEAM_BOILER_TANK, new SteamBoilerTank(1), false);
  }

  // TODO Forestry CE 26.1: uncomment when Forestry CE is released for 26.1.x
  // private static Identifier forestry(String path) {
  //   return Identifier.fromNamespaceAndPath(ForestryConstants.MOD_ID, path);
  // }

  private static Identifier buildcraft(String path) {
    return Identifier.fromNamespaceAndPath(BCEnergy.MODID, path);
  }
}

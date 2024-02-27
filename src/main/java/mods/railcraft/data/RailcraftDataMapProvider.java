package mods.railcraft.data;

import java.util.concurrent.CompletableFuture;
import mods.railcraft.datamaps.FluidHeat;
import mods.railcraft.datamaps.RailcraftDataMaps;
import mods.railcraft.datamaps.TunnelBoreHead;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

public class RailcraftDataMapProvider extends DataMapProvider {

  private static final int COAL_COKE_BURN_TIME = 3200;
  private static final int CREOSOTE_BUCKET_BURN_TIME = 800;

  public RailcraftDataMapProvider(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> lookupProvider) {
    super(packOutput, lookupProvider);
  }

  @Override
  protected void gather() {
    this.builder(NeoForgeDataMaps.FURNACE_FUELS)
        .add(RailcraftBlocks.COKE_BLOCK.getId(), new FurnaceFuel(COAL_COKE_BURN_TIME * 10), false)
        .add(RailcraftItems.COAL_COKE.getId(), new FurnaceFuel(COAL_COKE_BURN_TIME), false)
        .add(RailcraftItems.CREOSOTE_BUCKET.getId(), new FurnaceFuel(CREOSOTE_BUCKET_BURN_TIME), false)
        .add(RailcraftItems.CREOSOTE_BOTTLE.getId(), new FurnaceFuel(CREOSOTE_BUCKET_BURN_TIME / 3), false);

    this.builder(RailcraftDataMaps.TUNNEL_BORE_HEAD)
        .add(RailcraftItems.BRONZE_TUNNEL_BORE_HEAD, new TunnelBoreHead(1.25F), false)
        .add(RailcraftItems.STEEL_TUNNEL_BORE_HEAD, new TunnelBoreHead(1.25F), false)
        .add(RailcraftItems.DIAMOND_TUNNEL_BORE_HEAD, new TunnelBoreHead(1 / 0.6F), false)
        .add(RailcraftItems.IRON_TUNNEL_BORE_HEAD, new TunnelBoreHead(1), false);

    this.builder(RailcraftDataMaps.FLUID_HEAT)
        .add(RailcraftTags.Fluids.CREOSOTE, new FluidHeat(4800), false);
  }
}

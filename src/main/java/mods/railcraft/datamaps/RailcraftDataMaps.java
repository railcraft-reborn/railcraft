package mods.railcraft.datamaps;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

public class RailcraftDataMaps {

  public static final DataMapType<Item, TunnelBoreHead> TUNNEL_BORE_HEAD =
      DataMapType.builder(RailcraftConstants.id("tunnel_bore_head"),
          Registries.ITEM, TunnelBoreHead.CODEC).synced(TunnelBoreHead.DIGMODIFIER_CODEC, false).build();

  public static final DataMapType<Fluid, FluidHeat> FLUID_HEAT =
      DataMapType.builder(RailcraftConstants.id("fluid_heat"),
          Registries.FLUID, FluidHeat.CODEC).synced(FluidHeat.HEAT_VALUE_PER_BUCKET_CODEC, false).build();

  public static final DataMapType<Block, SteamBoilerTank> STEAM_BOILER_TANK =
      DataMapType.builder(RailcraftConstants.id("steam_boiler_tank"),
          Registries.BLOCK, SteamBoilerTank.CODEC).synced(SteamBoilerTank.STEAM_MULTIPLIER_CODEC, false).build();

  public static void register(IEventBus modEventBus) {
    modEventBus.addListener(RegisterDataMapTypesEvent.class, event -> {
      event.register(TUNNEL_BORE_HEAD);
      event.register(FLUID_HEAT);
      event.register(STEAM_BOILER_TANK);
    });
  }
}

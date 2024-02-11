package mods.railcraft.util.datamaps;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.util.datamaps.builtin.TunnelBoreHead;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

public class RailcraftDataMaps {

  public static final DataMapType<Item, TunnelBoreHead> TUNNEL_BORE_HEAD =
      DataMapType.builder(RailcraftConstants.rl("tunnel_bore_head"),
          Registries.ITEM, TunnelBoreHead.CODEC).synced(TunnelBoreHead.DIGMODIFIER_CODEC, false).build();

  public static void register(IEventBus modEventBus) {
    modEventBus.addListener(RegisterDataMapTypesEvent.class, event -> {
      event.register(TUNNEL_BORE_HEAD);
    });
  }
}

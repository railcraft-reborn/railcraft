package mods.railcraft.data.loot;

import java.util.List;
import java.util.Set;
import mods.railcraft.data.loot.packs.RailcraftBlockLoot;
import mods.railcraft.data.loot.packs.RailcraftChestLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class RailcraftLootTableProvider extends LootTableProvider {

  public RailcraftLootTableProvider() {
    super(Set.of(), List.of(
        new LootTableProvider.SubProviderEntry(RailcraftBlockLoot::new, LootContextParamSets.BLOCK),
        new LootTableProvider.SubProviderEntry(RailcraftChestLoot::new, LootContextParamSets.CHEST)));
  }
}

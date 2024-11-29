package mods.railcraft.data.loot;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import mods.railcraft.data.loot.packs.RailcraftBlockLoot;
import mods.railcraft.data.loot.packs.RailcraftChestLoot;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class RailcraftLootTableProvider extends LootTableProvider {

  public RailcraftLootTableProvider(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> registries) {
    super(packOutput, Set.of(), List.of(
        new LootTableProvider.SubProviderEntry(RailcraftBlockLoot::new, LootContextParamSets.BLOCK),
        new LootTableProvider.SubProviderEntry(RailcraftChestLoot::new, LootContextParamSets.CHEST))
    , registries);
  }
}

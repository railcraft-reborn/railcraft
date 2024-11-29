package mods.railcraft.data.loot;

import java.util.concurrent.CompletableFuture;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.data.loot.packs.RailcraftChestLoot;
import mods.railcraft.loot.DungeonLootModifier;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

public class RailcraftLootModifierProvider extends GlobalLootModifierProvider {

  public RailcraftLootModifierProvider(PackOutput output,
      CompletableFuture<HolderLookup.Provider> registries) {
    super(output, registries, RailcraftConstants.ID);
  }

  @Override
  protected void start() {
    this.add(BuiltInLootTables.ABANDONED_MINESHAFT, RailcraftChestLoot.ABANDONED_MINESHAFT);
    this.add(BuiltInLootTables.SIMPLE_DUNGEON, RailcraftChestLoot.SIMPLE_DUNGEON);
    this.add(BuiltInLootTables.STRONGHOLD_CORRIDOR, RailcraftChestLoot.SIMPLE_DUNGEON);
    this.add(BuiltInLootTables.STRONGHOLD_CROSSING, RailcraftChestLoot.SIMPLE_DUNGEON);
    this.add(BuiltInLootTables.VILLAGE_ARMORER, RailcraftChestLoot.SIMPLE_DUNGEON);
  }

  private void add(ResourceKey<LootTable> targetLootTable, ResourceKey<LootTable> customLootTable) {
    this.add(targetLootTable.location().getPath(),
        new DungeonLootModifier(getCondition(targetLootTable.location()), customLootTable));
  }

  private LootItemCondition[] getCondition(ResourceLocation lootTable) {
    var condition = LootTableIdCondition.builder(lootTable);
    return new LootItemCondition[]{condition.build()};
  }
}

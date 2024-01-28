package mods.railcraft.data.loot;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.data.loot.packs.RailcraftChestLoot;
import mods.railcraft.loot.DungeonLootModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class RailcraftLootModifierProvider extends GlobalLootModifierProvider {

  public RailcraftLootModifierProvider(PackOutput output) {
    super(output, RailcraftConstants.ID);
  }

  private void add(ResourceLocation targetLootTable, ResourceLocation customLootTable) {
    this.add(targetLootTable.getPath(),
        new DungeonLootModifier(getCondition(targetLootTable), customLootTable));
  }

  @Override
  protected void start() {
    this.add(BuiltInLootTables.ABANDONED_MINESHAFT, RailcraftChestLoot.ABANDONED_MINESHAFT);
    this.add(BuiltInLootTables.SIMPLE_DUNGEON, RailcraftChestLoot.SIMPLE_DUNGEON);
    this.add(BuiltInLootTables.STRONGHOLD_CORRIDOR, RailcraftChestLoot.SIMPLE_DUNGEON);
    this.add(BuiltInLootTables.STRONGHOLD_CROSSING, RailcraftChestLoot.SIMPLE_DUNGEON);
    this.add(BuiltInLootTables.VILLAGE_ARMORER, RailcraftChestLoot.SIMPLE_DUNGEON);
  }

  private LootItemCondition[] getCondition(ResourceLocation lootTable) {
    var condition = LootTableIdCondition.builder(lootTable);
    return new LootItemCondition[]{condition.build()};
  }
}

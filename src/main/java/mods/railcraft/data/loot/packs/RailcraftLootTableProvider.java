package mods.railcraft.data.loot.packs;

import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootDataId;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class RailcraftLootTableProvider extends LootTableProvider {

  public RailcraftLootTableProvider(PackOutput packOutput) {
    super(packOutput, Set.of(), List.of());
  }

  @Override
  public List<LootTableProvider.SubProviderEntry> getTables() {
    return List.of(new LootTableProvider.SubProviderEntry(
        RailcraftBlockLoot::new, LootContextParamSets.BLOCK
    ));
  }

  @Override
  protected void validate(Map<ResourceLocation, LootTable> map,
      ValidationContext validationcontext) {
    map.forEach((location, lootTable) ->
        lootTable.validate(validationcontext
            .setParams(lootTable.getParamSet())
            .enterElement("{" + location + "}", new LootDataId<>(LootDataType.TABLE, location))));
  }
}

package mods.railcraft.data.loot;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import com.mojang.datafixers.util.Pair;
import mods.railcraft.data.loot.packs.RailcraftBlockLoot;
import mods.railcraft.data.loot.packs.RailcraftChestLoot;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class RailcraftLootTableProvider extends LootTableProvider {
  public RailcraftLootTableProvider(DataGenerator dataGenerator) {
    super(dataGenerator);
  }

  @Override
  protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>,
      LootContextParamSet>> getTables() {
    return List.of(
        Pair.of(RailcraftBlockLoot::new, LootContextParamSets.BLOCK),
        Pair.of(RailcraftChestLoot::new, LootContextParamSets.CHEST)
    );
  }

  @Override
  protected void validate(Map<ResourceLocation, LootTable> map,
                          ValidationContext validationcontext) {
    map.forEach((location, lootTable) ->
        lootTable.validate(validationcontext
            .setParams(lootTable.getParamSet())
            .enterTable("{" + location + "}", location)));
  }
}

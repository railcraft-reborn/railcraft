package mods.railcraft.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class RailcraftLootTableProvider extends LootTableProvider {

  public RailcraftLootTableProvider(DataGenerator dataGenerator) {
    super(dataGenerator);
  }

  @Override
  protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
    ImmutableList.Builder<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> builder =
        ImmutableList.builder();
    builder.add(Pair.of(RailcraftBlockLoot::new, LootContextParamSets.BLOCK));
    return builder.build();
  }

  @Override
  protected void validate(Map<ResourceLocation, LootTable> map,
      ValidationContext validationTracker) {
    map.forEach((location, lootTable) -> LootTables.validate(validationTracker,
        location, lootTable));
  }

  @Override
  public String getName() {
    return "Railcraft Loot Tables";
  }
}

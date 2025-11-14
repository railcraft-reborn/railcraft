package mods.railcraft.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mods.railcraft.RailcraftConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class DungeonLootModifier extends LootModifier {

  public static final MapCodec<DungeonLootModifier> CODEC =
      RecordCodecBuilder.mapCodec(inst -> codecStart(inst)
          .and(ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("lootTable").forGetter((m) -> m.lootTable))
          .apply(inst, DungeonLootModifier::new));
  private final ResourceKey<LootTable> lootTable;

  public DungeonLootModifier(LootItemCondition[] conditionsIn, ResourceKey<LootTable> lootTable) {
    super(conditionsIn);
    this.lootTable = lootTable;
  }

  @SuppressWarnings("deprecation")
  @Override
  protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot,
      LootContext context) {
    if (RailcraftConfig.SERVER.changeDungeonLoot.get()) {
      context.getResolver().get(Registries.LOOT_TABLE, this.lootTable).ifPresent(extraTable -> {
        extraTable.value().getRandomItemsRaw(context,
            LootTable.createStackSplitter(context.getLevel(), generatedLoot::add));
      });
    }
    return generatedLoot;
  }

  @Override
  public MapCodec<? extends IGlobalLootModifier> codec() {
    return RailcraftLootModifiers.DUNGEON_LOOT_MODIFIER.get();
  }
}

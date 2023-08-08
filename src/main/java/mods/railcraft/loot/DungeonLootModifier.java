package mods.railcraft.loot;

import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mods.railcraft.RailcraftConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

public class DungeonLootModifier extends LootModifier {

  public static final Supplier<Codec<DungeonLootModifier>> CODEC = Suppliers.memoize(() ->
      RecordCodecBuilder.create(inst -> codecStart(inst)
          .and(ResourceLocation.CODEC.fieldOf("lootTable").forGetter((m) -> m.lootTable))
          .apply(inst, DungeonLootModifier::new)));
  private final ResourceLocation lootTable;

  public DungeonLootModifier(LootItemCondition[] conditionsIn, ResourceLocation lootTable) {
    super(conditionsIn);
    this.lootTable = lootTable;
  }

  @Override
  @NotNull
  protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot,
      LootContext context) {
    if (RailcraftConfig.SERVER.changeDungeonLoot.get()) {
      var extraTable = context.getResolver().getLootTable(this.lootTable);
      extraTable.getRandomItemsRaw(context,
          LootTable.createStackSplitter(context.getLevel(), generatedLoot::add));
    }
    return generatedLoot;
  }

  @Override
  public Codec<? extends IGlobalLootModifier> codec() {
    return CODEC.get();
  }
}

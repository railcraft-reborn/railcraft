package mods.railcraft.data.loot.packs;

import java.util.function.BiConsumer;
import mods.railcraft.Railcraft;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import vazkii.patchouli.api.PatchouliAPI;

public class RailcraftAdvancementRewardLoot implements LootTableSubProvider {

  public static final ResourceLocation PATCHOULI_BOOK =
      Railcraft.rl("advancements/patchouli_book");

  @Override
  public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
    var bookId = Railcraft.rl("guide_book");
    var book = PatchouliAPI.get().getBookStack(bookId).getItem();
    var tag = new CompoundTag();
    tag.putString("patchouli:book", bookId.toString());
    consumer.accept(PATCHOULI_BOOK, LootTable.lootTable()
        .withPool(LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1))
            .add(LootItem.lootTableItem(book).apply(SetNbtFunction.setTag(tag)))
        )
    );
  }
}

package mods.railcraft.data.loot.packs;

import java.util.function.BiConsumer;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

public class RailcraftAdvancementRewardLoot implements LootTableSubProvider {

  public static final ResourceLocation PATCHOULI_BOOK =
      RailcraftConstants.rl("advancements/patchouli_book");

  @Override
  public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
    /*var bookId = Railcraft.rl("guide_book");
    var book = PatchouliAPI.get().getBookStack(bookId).getItem();
    var tag = new CompoundTag();
    tag.putString("patchouli:book", bookId.toString());
    consumer.accept(PATCHOULI_BOOK, LootTable.lootTable()
        .withPool(LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1))
            .add(LootItem.lootTableItem(book).apply(SetNbtFunction.setTag(tag)))
        )
    );*/
  }
}

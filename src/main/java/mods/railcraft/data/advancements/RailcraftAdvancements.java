package mods.railcraft.data.advancements;

import java.util.function.Consumer;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.data.loot.packs.RailcraftAdvancementRewardLoot;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

class RailcraftAdvancements implements AdvancementProvider.AdvancementGenerator {

  @Override
  public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> consumer,
      ExistingFileHelper fileHelper) {
    Advancement.Builder.advancement()
        .addCriterion("tick", PlayerTrigger.TriggerInstance.tick())
        .rewards(AdvancementRewards.Builder.loot(RailcraftAdvancementRewardLoot.PATCHOULI_BOOK))
        .save(consumer, RailcraftConstants.rl("grant_book_on_first_join"), fileHelper);
  }
}
